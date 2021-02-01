package academy.jairo.springboot.request.person;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonPutRequestBody {
    private Long id;
    private String name;
}
