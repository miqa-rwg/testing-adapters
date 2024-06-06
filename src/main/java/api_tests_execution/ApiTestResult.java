package api_tests_execution;

import io.restassured.response.Response;
import rest.FileToUpload;
import rest.RestMethod;
import rest.RestParameter;

import java.util.List;


public record ApiTestResult(String baseUrl,
                            String endpoint,
                            RestMethod method,
                            List<RestParameter> headers,
                            List<RestParameter> pathParams,
                            List<RestParameter> queryParams,
                            String token,
                            FileToUpload fileToUpload,
                            String body,
                            Response response) {
}
