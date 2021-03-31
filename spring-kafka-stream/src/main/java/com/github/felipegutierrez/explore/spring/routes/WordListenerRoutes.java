package com.github.felipegutierrez.explore.spring.routes;

import com.github.felipegutierrez.explore.spring.handlers.WordListenerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class WordListenerRoutes {
    @Bean
    public RouterFunction route(WordListenerHandler wordListenerHandler) {
        return RouterFunctions
                .route(GET("/functional/wordcountstream").and(accept(MediaType.APPLICATION_STREAM_JSON)),
                        wordListenerHandler::fluxWordCountStream);
    }
}
