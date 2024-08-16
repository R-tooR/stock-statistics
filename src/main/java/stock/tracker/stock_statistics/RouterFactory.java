package stock.tracker.stock_statistics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import stock.tracker.stock_statistics.handlers.StatsHandler;
import stock.tracker.stock_statistics.repositories.InMemoryRepositoryImplDoubleAsync;

import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFactory {

    @Bean
    public RouterFunction<ServerResponse> routerTestFunction(StatsHandler statsHandler) {
        return route()
                .POST("/add_batch", statsHandler::uploadBatch)
                .GET("/stats", queryParam("symbol", t -> true).and(queryParam("k", t -> true)),  statsHandler::getStats)
                .build();
    }

    @Bean
    public StatsHandler statsHandler(InMemoryRepositoryImplDoubleAsync inMemoryRepositoryAsync) {
        return new StatsHandler(inMemoryRepositoryAsync);
    }
}
