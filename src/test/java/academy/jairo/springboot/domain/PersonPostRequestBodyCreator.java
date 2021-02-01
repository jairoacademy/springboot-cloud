package academy.jairo.springboot.domain;

import academy.jairo.springboot.request.person.PersonPostRequestBody;

public class PersonPostRequestBodyCreator {

    public static PersonPostRequestBody create() {
        return PersonPostRequestBody.builder()
                .name(PersonCreator.create().getName())
                .build();
    }

}
