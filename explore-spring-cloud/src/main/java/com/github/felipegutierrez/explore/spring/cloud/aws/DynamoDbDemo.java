package com.github.felipegutierrez.explore.spring.cloud.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * First use start the Localstack docker image
 * <p>
 * $ cd localstack/
 * $ SERVICES=dynamodb docker-compose up
 * $ telnet 127.0.0.1 4566
 */
@SpringBootApplication
@EnableConfigurationProperties(DynamoDbProperties.class)
public class DynamoDbDemo implements ApplicationRunner {

    @Autowired
    private DynamoDbUtilService dynamoDbUtilService;

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(DynamoDbDemo.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        dynamoDbUtilService.createDynamoDbTable("book_table_1");
    }

    @Bean
    public DynamoDbClient dynamoDbClient(DynamoDbProperties dynamoDbProperties) {
        return appply(dynamoDbProperties, DynamoDbClient.builder()).build();
    }

    @Bean
    public S3Client s3Client(DynamoDbProperties dynamoDbProperties) {
        return appply(dynamoDbProperties, S3Client.builder()).build();
    }

    public <BuilderT extends AwsClientBuilder<BuilderT, ClientT>, ClientT> AwsClientBuilder<BuilderT, ClientT> appply(
            DynamoDbProperties dynamoDbProperties,
            AwsClientBuilder<BuilderT, ClientT> builder
    ) {
        if (dynamoDbProperties.getEndpointURI() != null) {
            builder.endpointOverride(dynamoDbProperties.getEndpointURI());
        }
        return builder;
    }
}
