package com.github.felipegutierrez.explore.spring.service;

import com.github.felipegutierrez.explore.spring.beans.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderWithDateService {

    private final Clock clock;

    /**
     * Places a new order.
     *
     * @param content the content to order
     * @return the order
     */
    public Order place(String content) {
        return Order.builder()
                .id(UUID.randomUUID())
                .createdAt(ZonedDateTime.now(clock))
                .content(content)
                .build();
    }
}