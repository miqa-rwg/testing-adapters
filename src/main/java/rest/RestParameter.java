package rest;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestParameter {

    private String key;
    private Object value;
}
