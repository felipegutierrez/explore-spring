package com.github.felipegutierrez.explore.circuitbreaker;

import com.github.felipegutierrez.explore.circuitbreaker.model.Flight;
import com.github.felipegutierrez.explore.circuitbreaker.model.SearchRequest;
import com.github.felipegutierrez.explore.circuitbreaker.services.FlightSearchService;
import com.github.felipegutierrez.explore.circuitbreaker.services.failures.SucceedXTimesFailYTimesAndThenSucceed;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.Data;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

/**
 * Testing using code from https://github.com/thombergs/code-examples/tree/master/resilience4j/circuitbreaker
 */
@Data
public class CircuitBreakerDemo {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
    private CircuitBreaker circuitBreaker;

    public static void main(String[] args) {
        CircuitBreakerDemo circuitBreakerDemo = new CircuitBreakerDemo();
        System.out.println("------------------- displayDefaultValues ------------------------------");
        circuitBreakerDemo.displayDefaultValues();
        System.out.println("------------------- circuitBreakerOpenAndThenClose --------------------");
        circuitBreakerDemo.circuitBreakerOpenAndThenClose(50);
        System.out.println("-----------------------------------------------------------------------");
    }

    public void displayDefaultValues() {
        CircuitBreakerConfig config = CircuitBreakerConfig.ofDefaults();
        System.out.println("failureRateThreshold = " + config.getFailureRateThreshold());
        System.out.println("minimumNumberOfCalls = " + config.getMinimumNumberOfCalls());
        System.out.println("permittedNumberOfCallsInHalfOpenState = " + config.getPermittedNumberOfCallsInHalfOpenState());
        System.out.println("maxWaitDurationInHalfOpenState = " + config.getMaxWaitDurationInHalfOpenState());
        System.out.println("slidingWindowSize = " + config.getSlidingWindowSize());
        System.out.println("slidingWindowType = " + config.getSlidingWindowType());
        System.out.println("slowCallRateThreshold = " + config.getSlowCallRateThreshold());
        System.out.println("slowCallDurationThreshold = " + config.getSlowCallDurationThreshold());
        System.out.println("automaticTransitionFromOpenToHalfOpenEnabled = " + config.isAutomaticTransitionFromOpenToHalfOpenEnabled());
        System.out.println("writableStackTraceEnabled = " + config.isWritableStackTraceEnabled());
    }

    public void circuitBreakerOpenAndThenClose(int maximum) {
        CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(25.0f)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(4)
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        circuitBreaker = registry.circuitBreaker("flightSearchService");

        circuitBreaker.getEventPublisher().onCallNotPermitted(e -> {
            System.out.println(e);
            // just to simulate lag so the circuitbreaker can change state
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        circuitBreaker.getEventPublisher().onError(e -> System.out.println(e));
        circuitBreaker.getEventPublisher().onStateTransition(e -> System.out.println(e));

        FlightSearchService service = new FlightSearchService();
        SearchRequest request = new SearchRequest("NYC", "LAX", "12/31/2020");
        service.setPotentialFailure(new SucceedXTimesFailYTimesAndThenSucceed(4, 4));

        Supplier<List<Flight>> flightsSupplier = circuitBreaker.decorateSupplier(() -> service.searchFlights(request));

        for (int i = 0; i < maximum; i++) {
            try {
                var flightList = flightsSupplier.get();
                System.out.println(flightList);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
