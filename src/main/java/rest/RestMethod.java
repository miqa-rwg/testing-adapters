package rest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum RestMethod {
    DELETE,
    GET,
    PATCH,
    POST,
    PUT;


    public static List<RestMethod> getValues() {
        return new LinkedList<>(Arrays.asList(RestMethod.values()));
    }

    public static RestMethod getValueByString(String value) {
        for (RestMethod item : RestMethod.values()) {
            if (item.toString().equalsIgnoreCase(value))
                return item;
        }
        throw new RuntimeException("\nUnexpected value: " + value + ".\nPlease check api method value you use.");
    }

    public static List<RestMethod> getValuesExcept(RestMethod given) {
        List<RestMethod> result = new LinkedList<>();
        for (RestMethod item : RestMethod.values()) {
            if (item != given)
                result.add(item);
        }
        return result;
    }
}
