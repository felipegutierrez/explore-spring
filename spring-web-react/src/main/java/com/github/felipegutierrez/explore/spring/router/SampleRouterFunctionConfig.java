package com.github.felipegutierrez.explore.spring.router;

import com.github.felipegutierrez.explore.spring.handler.SampleHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * comment implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive' at build.gradle in order to this application work
 */
@Configuration
public class SampleRouterFunctionConfig {

    @Bean
    public RouterFunction route(SampleHandlerFunction sampleHandlerFunction) {
        return RouterFunctions
                .route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)),
                        sampleHandlerFunction::flux)
                .andRoute(GET("/functional/fluxstream").and(accept(MediaType.APPLICATION_STREAM_JSON)),
                        sampleHandlerFunction::fluxStream)
                .andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)),
                        sampleHandlerFunction::mono);
    }
}
