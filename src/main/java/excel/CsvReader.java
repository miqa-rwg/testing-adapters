package excel;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
public class CsvReader {

    private final List<List<Object>> content = new LinkedList<>();


    public CsvReader(String filePath) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<Object> lineContent = new ArrayList<>();
                Collections.addAll(lineContent, line.split(","));
                content.add(lineContent);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading csv:\n" + e.getMessage());
        }
    }
}
