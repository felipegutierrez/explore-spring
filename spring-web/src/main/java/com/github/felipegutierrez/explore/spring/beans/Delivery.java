package com.github.felipegutierrez.explore.spring.beans;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
@Builder
public class Delivery {

    private UUID id;
    private UUID orderId;
    private ZonedDateTime createdAt;
    private ZonedDateTime plannedAt;
    private ZonedDateTime shippedAt;
}
