package excel;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class ExcelReader {

    private final Workbook workbook;
    @Getter
    private final String filepath;


    public ExcelReader(String filepath) {
        this.filepath = filepath;
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            this.workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException ioex) {
            throw new RuntimeException(ioex.getMessage());
        }
    }


        public int getSheetsNumber() {
        return workbook.getNumberOfSheets();
    }

    public String getSheetNameBySheetIndex(int index) {
        return workbook.getSheetName(index);
    }

    public List<List<Object>> readSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        return getContent(sheet);
    }

    public List<Map<String, Object>> readTestingDataFromSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        List<List<Object>> content = getContent(sheet);
        return convertContentToMapList(content);
    }

    public List<String> readTestingDataHeadersFromSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        List<List<Object>> content = getContent(sheet);
        return getContentHeaders(content);
    }

    private List<List<Object>> getContent(Sheet sheet) {
        List<List<Object>> data = new LinkedList<>();
        for (Row currentRow : sheet) {
            List<Object> rowData = new LinkedList<>();
            for (Cell currentCell : currentRow) {
                switch (currentCell.getCellType()) {
                    case STRING:
                    case BLANK:
                        rowData.add(currentCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        double value = currentCell.getNumericCellValue();
                        rowData.add(value);
                        break;
                    case FORMULA:
                        rowData.add(currentCell.getCellFormula());
                        break;
                    case BOOLEAN:
                        rowData.add(currentCell.getBooleanCellValue());
                        break;
                    default:
                        throw new RuntimeException("\nUnexpected value: " + currentCell.getCellType() + ".\nPlease check 'cellType' value.");
                }
            }
            data.add(rowData);
        }
        return data;
    }

    private List<String> getContentHeaders(List<List<Object>> sheetContent) {
        if(!sheetContent.isEmpty())
            return sheetContent.get(0).stream().map(i -> (String) i).toList();
        return new LinkedList<>();
    }

    public List<Map<String, Object>> convertContentToMapList(List<List<Object>> sheetContent) {
        List<Map<String, Object>> result = new LinkedList<>();

        if(!sheetContent.isEmpty()) {
            // split headers and values of testing data
            List<String> headers = sheetContent.get(0).stream().map(i -> (String) i).toList();
            // values is a list of rows that contain values for testing purposes
            List<List<Object>> values = new LinkedList<>();
            for (int row = 1; row < sheetContent.size(); row++)
                values.add(sheetContent.get(row));

            for (List<Object> row : values) {
                Map<String, Object> map = new HashMap<>();
                for (int index = 0; index < row.size(); index++) {
                    map.put(headers.get(index), row.get(index));
                }
                result.add(map);
            }
        }
        return result;
    }

    public boolean matchData(ExcelReader other) {
        for (int sheetIndex = 0; sheetIndex < getSheetsNumber(); sheetIndex++) {
            String sheetName = getSheetNameBySheetIndex(sheetIndex);
            String sheetNameOfOtherReader = other.getSheetNameBySheetIndex(sheetIndex);

            if (!sheetName.equals(sheetNameOfOtherReader))
                return false;

            List<List<Object>> content = readSheet(sheetName);
            List<List<Object>> contentOfOtherReader = other.readSheet(sheetName);
            if (!content.equals(contentOfOtherReader))
                return false;
        }
        return true;
    }
}
