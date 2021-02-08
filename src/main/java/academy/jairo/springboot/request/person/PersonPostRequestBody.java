package academy.jairo.springboot.request.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonPostRequestBody {
    @NotEmpty(message = "The Person name cannot be empty")
    @Schema(description = "Hi, this a name!", example = "Gabriel Angel")
    private String name;

    @URL
    private String url;
}
