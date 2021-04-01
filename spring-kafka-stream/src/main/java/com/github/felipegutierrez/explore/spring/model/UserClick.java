package com.github.felipegutierrez.explore.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = {@JsonSchemaString(path = "javaType", value = "com.github.felipegutierrez.explore.spring.model.UserClick")})
public class UserClick {
    @JsonProperty("UserID")
    private String userID;
    @JsonProperty("CreatedTime")
    private Long createdTime;
    @JsonProperty("CurrentLink")
    private String currentLink;
    @JsonProperty("NextLink")
    private String nextLink;
}
