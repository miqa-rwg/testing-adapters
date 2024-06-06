package api_tests_execution;

import data.DataGenerator;
import io.restassured.response.Response;
import rest.FileToUpload;
import rest.Rest;
import rest.RestMethod;
import rest.RestParameter;
import test_data.TestingDataForEndpoint;

import java.util.LinkedList;
import java.util.List;


public class ApiTestExecution {

    private final String baseUrl;
    private final RestMethod method;
    private final String endpoint;
    private final List<RestParameter> headers;
    private final List<List<RestParameter>> pathParams;
    private final List<List<RestParameter>> queryParams;
    private final String token;
    private final FileToUpload fileToUpload;
    private final String body;


    public ApiTestExecution(TestingDataForEndpoint data, String token, FileToUpload file, String body) {
        this.baseUrl = data.getBaseUrl();
        this.method = data.getMethod();
        this.endpoint = data.getEndpoint();
        data.setApiKey();
        this.headers = data.getHeaders();
        this.pathParams = data.getPathParams();
        this.queryParams = data.getQueryParams();
        this.token = token;
        this.fileToUpload = file;
        this.body = body;
    }

    public ApiTestExecution(TestingDataForEndpoint data, String apiKey, String token, FileToUpload file, String body) {
        this.baseUrl = data.getBaseUrl();
        this.method = data.getMethod();
        this.endpoint = data.getEndpoint();
        data.setApiKey(apiKey);
        this.headers = data.getHeaders();
        this.pathParams = data.getPathParams();
        this.queryParams = data.getQueryParams();
        this.token = token;
        this.fileToUpload = file;
        this.body = body;
    }


    public ApiTestResult executeRequestWhenUsingCorrectData() {
       Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams(), queryParams(), fileToUpload, body);
       return new ApiTestResult(baseUrl, endpoint, method, headers, pathParams(), queryParams(), token, fileToUpload, body, response);
    }

    public ApiTestResult executeRequestWhenNonSecuredHttpProtocol() {
        String baseUrl = this.baseUrl.replace("https", "http");
        Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams(), queryParams(), fileToUpload, body);
        return new ApiTestResult(baseUrl, endpoint, method, headers, pathParams(), queryParams(), token, fileToUpload, body, response);
    }

    public ApiTestResult executeRequestWhenIncorrectEndpoint() {
        String endpoint = this.endpoint + "1";
        Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams(), queryParams(), fileToUpload, body);
        return new ApiTestResult(baseUrl, endpoint, method, headers, pathParams(), queryParams(), token, fileToUpload, body, response);
    }

    public List<ApiTestResult> executeRequestWhenIncorrectRestMethod() {
        List<ApiTestResult> result = new LinkedList<>();
        List<RestMethod> incorrectMethods = RestMethod.getValuesExcept(method);
        for(RestMethod method : incorrectMethods) {
            Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams(), queryParams(), fileToUpload, body);
            result.add(new ApiTestResult(baseUrl, endpoint, method, headers, pathParams(), queryParams(), token, fileToUpload, body, response));
        }
        return result;
    }

    public List<ApiTestResult> executeRequestWhenIncorrectApiKey() {
        List<ApiTestResult> result = new LinkedList<>();
        String apiKey = "api-key";
        List<String> incorrectApiKeys = List.of("", " ", String.valueOf(DataGenerator.getValue("random string of size 10 using English characters and numbers")));
        for(String incorrectApiKey : incorrectApiKeys) {
            List<RestParameter> headers = new LinkedList<>(this.headers);
            headers.removeIf(header -> header.getKey().equals(apiKey));
            headers.add(new RestParameter(apiKey, incorrectApiKey));
            Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams(), queryParams(), fileToUpload, body);
            result.add(new ApiTestResult(baseUrl, endpoint, method, headers, pathParams(), queryParams(), token, fileToUpload, body, response));
        }
        return result;
    }

    public List<ApiTestResult> executeRequestWhenIncorrectToken() {
        List<ApiTestResult> result = new LinkedList<>();
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSIsImtpZCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSJ9.eyJhdWQiOiI0MjcyMmI3MS05NmI4LTRkNTEtOTNiMC1jNWNhYWY1MTQyYzgiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9mNGNlYjQzZS1hYjZiLTQ1MTEtOWFhYy1lODYxM2NhNTc2MGMvIiwiaWF0IjoxNjU2MTYzMTAxLCJuYmYiOjE2NTYxNjMxMDEsImV4cCI6MTY1NjE2NzAwMSwiYWlvIjoiQVZRQXEvOFRBQUFBTFpVUDA5LzFpVjN4REV3ekRiZ0dZaXU4dkRlV2FYYVYrS05TMUlwcStEVFIxWC9GbEUybHkvcGV6UlQrY3UwM3RJUUh4b2RyMy9YUmJKWVdBaWFRQW5nWFJCWERYQkc3QURhQjFjUUMrWUk9IiwiYW1yIjpbInB3ZCIsIm1mYSJdLCJjYyI6IkNnRUFFaEZ5YjJKbGNuUjNZV3gwWlhKekxtTnZiUm9TQ2hBNTdNSjRYSHJIUVpPb1o4eWxvZ0R6SWhJS0VLdHRYak9CbWNCQXZoZEo0R0RtQmdBeUFrVlZPQUE9IiwiZmFtaWx5X25hbWUiOiJJdmFub3YiLCJnaXZlbl9uYW1lIjoiTWFrc3ltIiwiaXBhZGRyIjoiOTEuMjE4Ljg4LjIyMCIsIm5hbWUiOiJNYWtzeW0gSXZhbm92Iiwibm9uY2UiOiIwYjJjMDExOGVmMmM0Nzc1YmUzYTNkYjYwYzkwODc3NCIsIm9pZCI6ImE5NDFiYmVlLTg0NWYtNDE5Yy1hOGFiLWJmYTBkMmVhM2QzZiIsIm9ucHJlbV9zaWQiOiJTLTEtNS0yMS0xNjIzNTkzNzExLTE5MDc3NjcyNjQtMTA0OTg0NTYtMjU5MTE2IiwicmgiOiIwLkFWd0FQclRPOUd1ckVVV2FyT2hoUEtWMkRIRXJja0s0bGxGTms3REZ5cTlSUXNoY0FQRS4iLCJyb2xlcyI6WyJST0xFX0RBVEFIVUIiLCJST0xFX1JFRkVSRU5DRSIsIlJPTEVfVVNFUiJdLCJzdWIiOiJsWWtISzRpd1pLd1hvblNwcDlNdktENi1IZXl6T1BnVWFrVERYaEg2OUpzIiwidGlkIjoiZjRjZWI0M2UtYWI2Yi00NTExLTlhYWMtZTg2MTNjYTU3NjBjIiwidW5pcXVlX25hbWUiOiJNYWtzeW0uSXZhbm92LWNvbnRyYWN0b3JAcm9iZXJ0d2FsdGVycy5jb20iLCJ1cG4iOiJNYWtzeW0uSXZhbm92LWNvbnRyYWN0b3JAcm9iZXJ0d2FsdGVycy5jb20iLCJ1dGkiOiJxMjFlTTRHWndFQy1GMG5nWU9ZR0FBIiwidmVyIjoiMS4wIn0.LCxSaXMSUsQKEQ6kxpfI00-uVRvfJ1OBdba_fXiQFTSJU2eRi9J2EtUDhwa3ssvlXqxbWUz5ke9RQPtTvQYja24utl1komHveiKMQUn-wfG37X04jyZiOlOrXKAhZg4AoiqUrzKMN9Dpa8mMH32SRFBF4sdYblPOXjLgY005ifAFQyahxAFT-z757y2BmYyf7TZ54T3oLdzIcSajAq_TA7jxRFxPwnQPExAhh0rsL1Te2b-nvnJf2WPJnfx8ZiiAr4omjQgWFTJ00woAwQN__GRljFRNJsja-FZCwUNYFZ5-DhjG_4epH7GjRLq_AbHoqFAp56vUfP1MbNDmignURw";
        List<String> incorrectTokens = List.of("", " ", expiredToken, String.valueOf(DataGenerator.getValue("random string of size 10 using English characters and numbers")));
        for(String token : incorrectTokens) {
            Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams(), queryParams(), fileToUpload, body);
            result.add(new ApiTestResult(baseUrl, endpoint, method, headers, pathParams(), queryParams(), token, fileToUpload, body, response));
        }
        return result;
    }
    
    public List<ApiTestResult> executeRequestWhenIncorrectPathParams() {
        List<ApiTestResult> result = new LinkedList<>();
        if(pathParams != null) {
            for (int index = 1; index < this.pathParams.size(); index++) {
                List<RestParameter> pathParams = this.pathParams.get(index);
                Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams, queryParams(), fileToUpload, body);
                result.add(new ApiTestResult(baseUrl, endpoint, method, headers, pathParams, queryParams(), token, fileToUpload, body, response));
            }
        } else throw new RuntimeException("You cannot execute 'incorrect path params' test when no path parameters in test data");
        return result;
    }

    public List<ApiTestResult> executeRequestWhenIncorrectQueryParams() {
        List<ApiTestResult> result = new LinkedList<>();
        if(queryParams != null) {
            for (int index = 1; index < this.queryParams.size(); index++) {
                List<RestParameter> queryParams = this.queryParams.get(index);
                Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams(), queryParams, fileToUpload, body);
                result.add(new ApiTestResult(baseUrl, endpoint, method, headers, pathParams(), queryParams, token, fileToUpload, body, response));
            }
        } else throw new RuntimeException("You cannot execute 'incorrect query params' test when no query parameters in test data");
        return result;
    }

    public static ApiTestResult execute(
            String baseUrl,
            String endpoint,
            RestMethod method,
            List<RestParameter> headers,
            String token,
            List<RestParameter> pathParams,
            List<RestParameter> queryParams,
            FileToUpload fileToUpload,
            String body
    ) {
        Response response = new Rest(baseUrl).executeRequest(endpoint, method, headers, token, pathParams, queryParams, fileToUpload, body);
        return new ApiTestResult(baseUrl, endpoint, method, headers, pathParams, queryParams, token, fileToUpload, body, response);
    }

    private List<RestParameter> pathParams() {
        return pathParams == null ? null : pathParams.get(0);
    }

    private List<RestParameter> queryParams() {
        return queryParams == null ? null : queryParams.get(0);
    }
}
