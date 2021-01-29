package academy.jairo.springboot.request.person;

import lombok.Data;

@Data
public class PersonPutRequestBody {
    private Long id;
    private String name;
}
