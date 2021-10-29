package com.github.felipegutierrez.explore.spring.cloud.aws;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore("this test is under development")
public class DynamoDbUtilServiceTest extends BaseIntegrationTest {

    @Autowired
    private DynamoDbUtilService dynamoDbUtilService;

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Test
    public void foo() {
        dynamoDbUtilService.createDynamoDbTable("foo");

        assertThat(dynamoDbClient.listTables().tableNames()).contains("foo");
    }
}