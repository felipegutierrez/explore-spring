package com.github.felipegutierrez.explore.spring.cloud.aws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("dynamodb")
@Data
public class DynamoDbProperties {

    private URI endpointURI;
}
