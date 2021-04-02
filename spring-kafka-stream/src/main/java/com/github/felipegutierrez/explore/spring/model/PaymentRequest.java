package com.github.felipegutierrez.explore.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = {@JsonSchemaString(path = "javaType", value = "com.github.felipegutierrez.explore.spring.model.PaymentRequest")})
public class PaymentRequest {
    @JsonProperty("TransactionID")
    private String transactionID;
    @JsonProperty("CreatedTime")
    private Long createdTime;
    @JsonProperty("SourceAccountID")
    private String sourceAccountID;
    @JsonProperty("TargetAccountID")
    private String targetAccountID;
    @JsonProperty("Amount")
    private Double amount;
    @JsonProperty("OTP")
    private String OTP;
}
