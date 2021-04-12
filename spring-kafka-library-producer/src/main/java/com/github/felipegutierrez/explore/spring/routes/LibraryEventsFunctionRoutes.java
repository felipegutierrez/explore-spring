package com.github.felipegutierrez.explore.spring.routes;

import com.github.felipegutierrez.explore.spring.handlers.LibraryEventsFunctionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static com.github.felipegutierrez.explore.spring.util.LibraryProducerConstants.LIBRARY_FUNC_V1_ENDPOINT;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class LibraryEventsFunctionRoutes {
    @Bean
    public RouterFunction route(LibraryEventsFunctionHandler libraryEventsFunctionHandler) {
        return RouterFunctions
                .route(GET(LIBRARY_FUNC_V1_ENDPOINT).and(accept(MediaType.APPLICATION_JSON)),
                        libraryEventsFunctionHandler::postLibraryEvent);
    }
}
