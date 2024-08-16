package stock.tracker.stock_statistics.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import stock.tracker.stock_statistics.models.StatsBigDecimal;
import stock.tracker.stock_statistics.models.SymbolAndValue;
import stock.tracker.stock_statistics.models.SymbolBatch;
import stock.tracker.stock_statistics.repositories.InMemoryRepositoryImplDoubleAsync;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class StatsHandlerTest {
    @Mock
    private InMemoryRepositoryImplDoubleAsync repository;

    @InjectMocks
    private StatsHandler myService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStats_Success() {
        // Przygotowanie danych testowych
        SymbolAndValue symbAndVal = new SymbolAndValue();
        symbAndVal.setSymbol("AAPL");
        symbAndVal.setK(5);

        StatsBigDecimal statsBigDecimal = StatsBigDecimal.empty();

        // Mockowanie repozytorium
        Mockito.when(repository.select(any(Mono.class))).thenReturn(Mono.just(statsBigDecimal));

        // Mockowanie ServerRequest
        ServerRequest request = MockServerRequest.builder()
                .queryParam("symbol", "AAPL").queryParam("k", "5").build();

        // Wywołanie metody
        Mono<ServerResponse> responseMono = myService.getStats(request);

        // Weryfikacja
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    System.out.println(response.headers());
                    assert response.statusCode().equals(HttpStatus.OK);
                    assert statsBigDecimal.getVersion().equals(response.headers().getETag());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testGetStats_NotFound() {
        Mockito.when(repository.select(any(Mono.class))).thenReturn(Mono.empty());

        ServerRequest request = mock(MockServerRequest.class);
        Mockito.when(request.queryParam("symbol")).thenReturn(Optional.empty());
        Mockito.when(request.queryParam("k")).thenReturn(Optional.empty());

        Mono<ServerResponse> responseMono = myService.getStats(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    public void testGetStats_NotModified() {
        SymbolAndValue symbAndVal = new SymbolAndValue();
        symbAndVal.setSymbol("AAPL");
        symbAndVal.setK(5);

        StatsBigDecimal statsBigDecimal = StatsBigDecimal.empty();

        Mockito.when(repository.select(any(Mono.class))).thenReturn(Mono.just(statsBigDecimal));

        ServerRequest request = MockServerRequest.builder().header(HttpHeaders.IF_NONE_MATCH, statsBigDecimal.getVersion())
                        .queryParam("symbol", "AAPL").queryParam("k", "5").build();

        Mono<ServerResponse> responseMono = myService.getStats(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    assert response.statusCode().equals(HttpStatus.NOT_MODIFIED);
                    return true;
                })
                .verifyComplete();

    }

    @Test
    public void testGetStats_InvalidParameters() {
        Mockito.when(repository.select(any(Mono.class))).thenReturn(Mono.empty());

        ServerRequest request = mock(MockServerRequest.class);
        Mockito.when(request.queryParam("symbol")).thenReturn(Optional.of("AAA"));
        Mockito.when(request.queryParam("k")).thenReturn(Optional.of("one"));

        Mono<ServerResponse> responseMono = myService.getStats(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        assertDoesNotThrow(() -> new NumberFormatException());
    }

    @Test
    public void testUploadBatch_Success() {
        SymbolBatch batch = new SymbolBatch();
        batch.setSymbol("AAA");
        batch.setValues(List.of(1., 2., 3.));

        Mockito.when(repository.upsert(any(Mono.class))).thenReturn(Mono.just(batch));

        ServerRequest request = mock(ServerRequest.class);
        Mockito.when(request.bodyToMono(SymbolBatch.class)).thenReturn(Mono.just(batch));

        Mono<ServerResponse> responseMono = myService.uploadBatch(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    System.out.println(response.statusCode());
                    assert response.statusCode().equals(HttpStatus.OK);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testUploadBatch_BadRequest() {
        Mockito.when(repository.upsert(any(Mono.class))).thenReturn(Mono.empty());

        ServerRequest request = mock(ServerRequest.class);
        Mockito.when(request.bodyToMono(SymbolBatch.class)).thenReturn(Mono.just(new SymbolBatch()));

        Mono<ServerResponse> responseMono = myService.uploadBatch(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    System.out.println(response.statusCode());
                    assert response.statusCode().equals(HttpStatus.BAD_REQUEST);
                    return true;
                })
                .verifyComplete();
    }

}
