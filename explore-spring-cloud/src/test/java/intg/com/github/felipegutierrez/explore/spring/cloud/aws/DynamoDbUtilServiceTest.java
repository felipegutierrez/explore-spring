package com.github.felipegutierrez.explore.spring.cloud.aws;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DirtiesContext
@SpringBootTest
public class DynamoDbUtilServiceTest extends BaseIntegrationTest {

    @Autowired
    private DynamoDbUtilService dynamoDbUtilService;

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @BeforeAll
    public static void setUp() {
        System.setProperty(SdkSystemSetting.SYNC_HTTP_SERVICE_IMPL.property(), "software.amazon.awssdk.http.apache.ApacheSdkHttpService");
    }

    @Test
    public void foo() {
        dynamoDbUtilService.createDynamoDbTable("foo");

        assertThat(dynamoDbClient.listTables().tableNames()).contains("foo");
    }
}
