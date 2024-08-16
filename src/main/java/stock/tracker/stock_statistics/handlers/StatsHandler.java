package stock.tracker.stock_statistics.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import stock.tracker.stock_statistics.models.SymbolAndValue;
import stock.tracker.stock_statistics.models.SymbolBatch;
import stock.tracker.stock_statistics.repositories.InMemoryRepositoryImplDoubleAsync;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

public class StatsHandler {

    Logger logger = LoggerFactory.getLogger(StatsHandler.class);
    private final InMemoryRepositoryImplDoubleAsync repository;

    public StatsHandler(InMemoryRepositoryImplDoubleAsync repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> uploadBatch(ServerRequest request) {
        return request.bodyToMono(SymbolBatch.class).filter(symbolBatch -> symbolBatch != null
                        && symbolBatch.getSymbol() != null
                        && symbolBatch.getValues() != null).flatMap(sb -> ok().build(repository.upsert(Mono.just(sb))))
                .switchIfEmpty(status(HttpStatus.BAD_REQUEST).build());
    }

    public Mono<ServerResponse> getStats(ServerRequest request) {
        var symbol = request.queryParam("symbol");
        var k = request.queryParam("k");
        var symbAndVal = new SymbolAndValue();
        symbol.ifPresent(symbAndVal::setSymbol);

        try {
            k.ifPresent((kk) -> symbAndVal.setK(Integer.parseInt(kk)));
        } catch (NumberFormatException ex) {
            return ServerResponse.badRequest().build();
        }

        var result = repository.select(Mono.just(symbAndVal));

        return result.flatMap(stats -> {
                    String eTag = stats.getVersion();
                    String ifNoneMatch = request.headers().header(HttpHeaders.IF_NONE_MATCH).stream().findFirst().orElse(null);
                    if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
                        logger.info("Value for {} wasn't updated sine previous request", symbAndVal.getSymbol());
                        return ServerResponse.status(HttpStatus.NOT_MODIFIED).build();
                    }
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.ETAG, eTag)
                            .bodyValue(stats);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
