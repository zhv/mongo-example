package mongoexample.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author zhv
 * @since 24.01.2018
 */
@Data
@Builder
@Document
public class Address {

    private String city;
    private String street;
    private int apartment;
}
