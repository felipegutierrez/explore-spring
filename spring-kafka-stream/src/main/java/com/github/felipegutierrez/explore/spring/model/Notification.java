package com.github.felipegutierrez.explore.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = {@JsonSchemaString(path = "javaType", value = "com.github.felipegutierrez.explore.spring.model.Notification")})
public class Notification {

    @JsonProperty("InvoiceNumber")
    private String InvoiceNumber;
    @JsonProperty("CustomerCardNo")
    private String CustomerCardNo;
    @JsonProperty("TotalAmount")
    private Double TotalAmount;
    @JsonProperty("EarnedLoyaltyPoints")
    private Double EarnedLoyaltyPoints;
    @JsonProperty("TotalLoyaltyPoints")
    private Double TotalLoyaltyPoints = 0.0;
}
