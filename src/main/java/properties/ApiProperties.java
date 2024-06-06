package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ApiProperties {

    private final Properties properties = new Properties();

    private enum Key {
        BASE_URL    ("base_url"),
        API_KEY     ("api_key"),
        ;
        public final String field;

        Key(String field) {
            this.field = field;
        }
    }

    public ApiProperties(String filepath) {
        try {
            this.properties.load(new FileInputStream(filepath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String baseUrl() {
        return properties.getProperty(Key.BASE_URL.field);
    }

    public String apiKey() {
        return properties.getProperty(Key.API_KEY.field);
    }
}
