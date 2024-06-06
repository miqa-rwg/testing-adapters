package test_data;

import data.DataGenerator;
import excel.ExcelReader;
import lombok.Getter;
import properties.ApiProperties;
import rest.Rest;
import rest.RestMethod;
import rest.RestParameter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
public class TestingDataForEndpoint {

    // sheet names of Excel file for endpoint testing
    private static final String COMMON_SHEET_NAME = "common";
    private static final String HEADERS_SHEET_NAME = "headers";
    private static final String PATH_PARAMS_SHEET_NAME = "path_params";
    private static final String QUERY_PARAMS_SHEET_NAME = "query_params";

    // key for testing data
    private static final String METHOD = "method";
    private static final String ENDPOINT = "endpoint";
    private static final String HEADER_KEY = "headerName";
    private static final String HEADER_VALUE = "headerValue";

    private final String baseUrl;
    private final Rest api;
    private final String apiKey;
    private final String endpoint;
    private final RestMethod method;
    private List<RestParameter> headers;
    private final List<List<RestParameter>> pathParams;
    private final List<List<RestParameter>> queryParams;


    public TestingDataForEndpoint(String apiDataFilePath, String endpointDataFilePath) {
        ApiProperties apiProperties = new ApiProperties(apiDataFilePath);
        this.baseUrl = apiProperties.baseUrl();
        this.apiKey = apiProperties.apiKey();
        this.api = new Rest(baseUrl);

        ExcelReader excelFile = new ExcelReader(endpointDataFilePath);
        this.method = method(excelFile);
        this.endpoint = endpoint(excelFile);
        this.headers = headers(excelFile);

        this.pathParams = params(excelFile, PATH_PARAMS_SHEET_NAME);
        this.queryParams = params(excelFile, QUERY_PARAMS_SHEET_NAME);
    }

    public void setApiKey() {
        if(headers == null)
            headers = new LinkedList<>();
        headers.add(new RestParameter("api-key", apiKey));
    }

    public void setApiKey(String key) {
        if(headers == null)
            headers = new LinkedList<>();
        headers.add(new RestParameter(key, apiKey));
    }

    public void addHeader(String key, String value) {
        if(headers == null)
            headers = new LinkedList<>();
        headers.add(new RestParameter(key, value));
    }

    private RestMethod method(ExcelReader excelFile) {
        Map<String, Object> commonData = validateCommonData(excelFile);
        String methodLabel = String.valueOf(commonData.get(METHOD));
        return RestMethod.getValueByString(methodLabel);
    }

    private String endpoint(ExcelReader excelFile) {
        Map<String, Object> commonData = validateCommonData(excelFile);
        return String.valueOf(commonData.get(ENDPOINT));
    }

    private List<RestParameter> headers(ExcelReader excelFile) {
        List<Map<String, Object>> headersData = excelFile.readTestingDataFromSheet(HEADERS_SHEET_NAME);
        if(headersData.isEmpty())
            return null;
        List<RestParameter> headers = new LinkedList<>();
        for(Map<String, Object> testingData : headersData)
            headers.add(new RestParameter(String.valueOf(testingData.get(HEADER_KEY)), testingData.get(HEADER_VALUE)));
        return headers;
    }

    private List<List<RestParameter>> params(ExcelReader excelFile, String sheetName) {
        List<String> keys = excelFile.readTestingDataHeadersFromSheet(sheetName);
        List<Map<String, Object>> paramsData = excelFile.readTestingDataFromSheet(sheetName);
        if(paramsData.isEmpty())
            return null;
        List<List<RestParameter>> dataSet = new LinkedList<>();

        for(Map<String, Object> testingData : paramsData) {
            List<RestParameter> params = new LinkedList<>();
            for(String key : keys)
                params.add(new RestParameter(key, DataGenerator.getValue(testingData.get(key).toString())));
            dataSet.add(params);
        }
        return dataSet;
    }

    private Map<String, Object> validateCommonData(ExcelReader excelFile) {
        List<Map<String, Object>> commonData = excelFile.readTestingDataFromSheet(COMMON_SHEET_NAME);
        if(commonData.size() != 1)
            throw new RuntimeException("Please check file: " + excelFile.getFilepath() + ";\n the testing data in " + COMMON_SHEET_NAME + " tab. It should have the only value!");
        return commonData.get(0);
    }

    public List<RestParameter> getPathParams(int index) {
        if(pathParams == null)
            return null;
        if(pathParams.size() <= index)
            throw new RuntimeException("Please check row index for path parameters. Index out of bound");
        return pathParams.get(index);
    }

    public List<RestParameter> getQueryParams(int index) {
        if(queryParams == null)
            return null;
        if(queryParams.size() <= index)
            throw new RuntimeException("Please check row index for query parameters. Index out of bound");
        return queryParams.get(index);
    }
}
