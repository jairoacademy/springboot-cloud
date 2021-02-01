package academy.jairo.springboot.domain;

import academy.jairo.springboot.request.person.PersonPostRequestBody;
import academy.jairo.springboot.request.person.PersonPutRequestBody;

public class PersonPutRequestBodyCreator {

    public static PersonPutRequestBody create() {
        return PersonPutRequestBody.builder()
                .id(PersonCreator.createValidUpdated().getId())
                .name(PersonCreator.createValidUpdated().getName())
                .build();
    }

}
