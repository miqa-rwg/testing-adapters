package rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public class Rest {

    // baseURL = scheme (protocol) + authority (domain)
    private final String baseUrl;


    public Rest(String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public Response executeRequest(String endpoint,
                                   RestMethod method,
                                   List<RestParameter> headers,
                                   String token,
                                   List<RestParameter> pathParams,
                                   List<RestParameter> queryParams,
                                   FileToUpload filepathForUpload,
                                   String body) {
        RestAssured.baseURI = baseUrl;
        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification requestSpecification = RestAssured.given();

        setHeaders(requestSpecification, headers);
        setAuth2Token(requestSpecification, token);
        setPathParameters(requestSpecification, pathParams);
        setQueryParameters(requestSpecification, queryParams);
        setFileToUpload(requestSpecification, filepathForUpload);
        setBody(requestSpecification, body);
        return setEndpoint(requestSpecification, endpoint, method);
    }

    private void setHeaders(RequestSpecification requestSpecification, List<RestParameter> headers) {
        if (headers != null && !headers.isEmpty())
            for (RestParameter headerItem : headers)
                requestSpecification.header(headerItem.getKey(), headerItem.getValue());
    }

    private void setAuth2Token(RequestSpecification requestSpecification, String token) {
        if (token != null)
            requestSpecification.auth().oauth2(token);
    }

    private void setPathParameters(RequestSpecification requestSpecification, List<RestParameter> pathParams) {
        if (pathParams != null && !pathParams.isEmpty()) {
            for (RestParameter parameter : pathParams)
                requestSpecification.pathParam(parameter.getKey(), parameter.getValue());
        }
    }

    private void setQueryParameters(RequestSpecification requestSpecification, List<RestParameter> queryParams) {
        if (queryParams != null && !queryParams.isEmpty()) {
            for (RestParameter parameter : queryParams)
                requestSpecification.queryParam(parameter.getKey(), parameter.getValue());
        }
    }

    private void setFileToUpload(RequestSpecification requestSpecification, FileToUpload fileToUpload) {
        if (fileToUpload != null)
            requestSpecification.multiPart(fileToUpload.getMultipartName(), new File(fileToUpload.getFilePath()));
    }

    private void setBody(RequestSpecification requestSpecification, String data) {
        if (data != null)
            requestSpecification.body(data);
    }

    private Response setEndpoint(RequestSpecification requestSpecification, String endpoint, RestMethod method) {
        if (endpoint != null) {
            if (method.equals(RestMethod.POST))     return requestSpecification.post(endpoint);
            if (method.equals(RestMethod.PUT))      return requestSpecification.put(endpoint);
            if (method.equals(RestMethod.PATCH))    return requestSpecification.patch(endpoint);
            if (method.equals(RestMethod.DELETE))   return requestSpecification.delete(endpoint);
            if (method.equals(RestMethod.GET))      return requestSpecification.get(endpoint);
            throw new RuntimeException("Pay attention! You are trying to use incorrect Request Method");
        }
        throw new RuntimeException("Pay attention! You are trying to use empty endpoint");
    }
}
