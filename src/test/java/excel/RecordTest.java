package excel;

import api_tests_execution.ApiTestResult;
import io.restassured.internal.RestAssuredResponseImpl;
import org.junit.jupiter.api.Test;
import rest.RestMethod;
import rest.RestParameter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordTest {

    @Test
    public void validateRecord() {
        ApiTestResult result = new ApiTestResult(
                "base_url",
                "endpoint",
                RestMethod.POST,
                List.of(new RestParameter("key1", "value1")),
                List.of(new RestParameter("key2", "value2")),
                List.of(new RestParameter("key3", "value3")),
                "token",
                null,
                "body",
                new RestAssuredResponseImpl()
        );

        System.out.println(result.baseUrl());
        System.out.println(result.endpoint());
        System.out.println(result.method());
        System.out.println(result.headers().get(0));
        System.out.println(result.pathParams().get(0));
        System.out.println(result.queryParams().get(0));
        System.out.println(result.token());
        System.out.println(result.fileToUpload());
        System.out.println(result.body());
        System.out.println(result.response().statusCode());

        assertEquals(-1, result.response().statusCode());
    }
}
