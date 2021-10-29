package com.github.felipegutierrez.explore.spring.cloud.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Service
public class DynamoDbUtilService {

    @Autowired
    private final DynamoDbClient dynamoDbClient;

    public DynamoDbUtilService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void createDynamoDbTable(String tableName) {
        CreateTableRequest createTableRequest =
                CreateTableRequest.builder()
                        .tableName(tableName)
                        .keySchema(KeySchemaElement.builder()
                                .keyType(KeyType.HASH)
                                .attributeName("id")
                                .build())
                        .attributeDefinitions(AttributeDefinition.builder()
                                .attributeName("id")
                                .attributeType(ScalarAttributeType.S)
                                .build())
                        .provisionedThroughput(ProvisionedThroughput.builder()
                                .writeCapacityUnits(5L)
                                .readCapacityUnits(5L)
                                .build())
                        .build();
        dynamoDbClient.createTable(createTableRequest);
        dynamoDbClient.listTables()
                .tableNames()
                .forEach(System.out::println);
    }
}
