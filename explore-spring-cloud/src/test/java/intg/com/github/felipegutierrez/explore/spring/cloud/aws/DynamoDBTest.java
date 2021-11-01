package com.github.felipegutierrez.explore.spring.cloud.aws;

import org.junit.jupiter.api.*;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DynamoDBTest {

    private static DynamoDbClient ddb;

    @BeforeAll
    public static void setUp() {

        System.setProperty(SdkSystemSetting.SYNC_HTTP_SERVICE_IMPL.property(), "software.amazon.awssdk.http.apache.ApacheSdkHttpService");

        // Run tests on Real AWS Resources
        Region region = Region.EU_WEST_2;
        ddb = DynamoDbClient.builder().region(region).build();
        try (InputStream input = DynamoDBTest.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void whenInitializingAWSService_thenNotNull() {
        assertNotNull(ddb);
        System.out.println("Test 1 passed");
    }
}