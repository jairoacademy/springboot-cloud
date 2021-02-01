package academy.jairo.springboot.request.person;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class PersonPostRequestBody {
    @NotEmpty(message = "The Person name cannot be empty")
    private String name;

    @URL
    private String url;
}
