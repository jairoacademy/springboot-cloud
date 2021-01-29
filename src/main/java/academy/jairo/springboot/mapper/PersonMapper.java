package academy.jairo.springboot.mapper;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.request.person.PersonPostRequestBody;
import academy.jairo.springboot.request.person.PersonPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class PersonMapper {
    public static final PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
    public abstract Person toPerson(PersonPostRequestBody personPostRequestBody);
    public abstract Person toPerson(PersonPutRequestBody personPutRequestBody);

}
