package excel;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;


public class ExcelReaderTest {

    private static final String FILEPATH = "src/test/resources/endpoints_config/zenith_marketo_pro_api/candidate_registration_post.xlsx";
    private static final ExcelReader FILE = new ExcelReader(FILEPATH);
    //
    private static final String COMMON_SHEET_NAME = "common";
    private static final String METHOD = "method";
    private static final String ENDPOINT = "endpoint";
    //
    private static final String HEADERS_SHEET_NAME = "headers";
    private static final String HEADER_KEY = "headerName";
    private static final String HEADER_VALUE = "headerValue";
    //
    private static final String PATH_PARAMS_SHEET_NAME = "path_params";
    private static final String QUERY_PARAMS_SHEET_NAME = "query_params";


    @Test
    public void validateReadingExcel() {
        // get common data
        List<Map<String, Object>> common = FILE.readTestingDataFromSheet(COMMON_SHEET_NAME);
        for(Map<String, Object> testingData : common)
            System.out.println(testingData.get(METHOD) + " : " + testingData.get(ENDPOINT));
        System.out.println("----------------");

        // get headers
        List<Map<String, Object>> headers = FILE.readTestingDataFromSheet(HEADERS_SHEET_NAME);
        for(Map<String, Object> testingData : headers)
            System.out.println(testingData.get(HEADER_KEY) + " : " + testingData.get(HEADER_VALUE));
        System.out.println("----------------");

        // get path params
        List<Map<String, Object>> pathParams = FILE.readTestingDataFromSheet(PATH_PARAMS_SHEET_NAME);
        for(Map<String, Object> testingData : pathParams)
            System.out.println("applicationName : " + testingData.get("applicationName"));
        System.out.println("----------------");

        // get query params
        List<Map<String, Object>> queryParams = FILE.readTestingDataFromSheet(QUERY_PARAMS_SHEET_NAME);
        for(Map<String, Object> testingData : queryParams)
            System.out.println("isActive : " + testingData.get("isActive"));
    }

    @Test
    public void validateMatchingFiles() {
        ExcelReader other = new ExcelReader(FILEPATH);
        System.out.println(FILE.matchData(other));
    }
}
