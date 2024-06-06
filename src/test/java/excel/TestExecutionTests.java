package excel;

import api_tests_execution.ApiTestExecution;
import api_tests_execution.ApiTestResult;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.RestParameter;
import test_data.TestingDataForEndpoint;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestExecutionTests {

    private static final String apiFilepath = "src/test/resources/properties/api_wheatherstack_com.properties";
    private static final String endpointFilepath = "src/test/resources/endpoints_config/weatherstack/current_get.xlsx";
    private static final TestingDataForEndpoint data = new TestingDataForEndpoint(apiFilepath, endpointFilepath);
    private static ApiTestExecution testsExecution;

    @BeforeAll
    public static void setData() {
        // preparing data
        for(List<RestParameter> queryParams : data.getQueryParams())
            queryParams.add(new RestParameter("access_key", data.getApiKey()));
        testsExecution = new ApiTestExecution(data, null, null, null);
    }

    @Test
    public void correctData() {
        // test execution
        ApiTestResult apiTestResult = testsExecution.executeRequestWhenUsingCorrectData();

        // validating results
        apiTestResult.response().prettyPrint();
        assertEquals(200, apiTestResult.response().statusCode());
        assertEquals("Ontario", apiTestResult.response().jsonPath().getString("location.region"));
        assertEquals("en", apiTestResult.response().jsonPath().getString("request.language"));
    }

    @Test
    public void nonSecuredHttpProtocol() {
        // test execution
        ApiTestResult apiTestResult = testsExecution.executeRequestWhenNonSecuredHttpProtocol();

        // validating results
        apiTestResult.response().prettyPrint();
        assertEquals(200, apiTestResult.response().statusCode());
    }

    @Test
    public void iIncorrectEndpoint() {
        // test execution
        ApiTestResult apiTestResult = testsExecution.executeRequestWhenIncorrectEndpoint();

        // validating results
        System.out.println(apiTestResult.endpoint());
        apiTestResult.response().prettyPrint();
        assertEquals(200, apiTestResult.response().statusCode());
    }

    @Test
    public void incorrectRestMethod() {
        // test execution
        List<ApiTestResult> apiTestResults = testsExecution.executeRequestWhenIncorrectRestMethod();

        // validating results
        SoftAssertions softAssert = new SoftAssertions();
        for(ApiTestResult result : apiTestResults) {
            System.out.println(result.method());
            softAssert.assertThat(result.response().statusCode()).isEqualTo(200);
        }
        softAssert.assertAll();
    }

    @Test
    public void incorrectApiKey() {
        // test execution
        List<ApiTestResult> apiTestResults = testsExecution.executeRequestWhenIncorrectApiKey();

        // validating results
        SoftAssertions softAssert = new SoftAssertions();
        for(ApiTestResult result : apiTestResults) {
            System.out.println("----------------");
            for(RestParameter header : result.headers())
                System.out.println(header.getKey() + ": " + header.getValue());
            softAssert.assertThat(result.response().statusCode()).isEqualTo(200);
        }
        softAssert.assertAll();
    }

    @Test
    public void incorrectToken() {
        // test execution
        List<ApiTestResult> apiTestResults = testsExecution.executeRequestWhenIncorrectToken();

        // validating results
        SoftAssertions softAssert = new SoftAssertions();
        for(ApiTestResult result : apiTestResults) {
            System.out.println(result.token());
            softAssert.assertThat(result.response().statusCode()).isEqualTo(200);
        }
        softAssert.assertAll();
    }

    @Test
    public void incorrectPathParams() {
        // test execution
        Exception exception = assertThrows(RuntimeException.class, () -> testsExecution.executeRequestWhenIncorrectPathParams());

        // validating results
        assertEquals("You cannot execute 'incorrect path params' test when no path parameters in test data", exception.getMessage());
    }

    @Test
    public void incorrectQueryParams() {
        // test execution
        List<ApiTestResult> apiTestResults = testsExecution.executeRequestWhenIncorrectQueryParams();

        // validating results
        SoftAssertions softAssert = new SoftAssertions();
        for(ApiTestResult result : apiTestResults) {
            for(RestParameter queryParam : result.queryParams())
                System.out.println(queryParam.getKey() + ": " + queryParam.getValue());
            System.out.println("--------------");
            result.response().prettyPrint();
            System.out.println("==============");
            softAssert.assertThat(result.response().statusCode()).isEqualTo(200);
        }
        softAssert.assertAll();
    }
}
