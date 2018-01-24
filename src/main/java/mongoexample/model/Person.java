package mongoexample.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author zhv
 * @since 24.01.2018
 */
@Data
@Builder
@Document
public class Person {

    private String firstname;
    private String lastname;

    private Address address;
}
