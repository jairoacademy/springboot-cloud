package academy.jairo.springboot.service;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.exception.AppBadRequestException;
import academy.jairo.springboot.mapper.PersonMapper;
import academy.jairo.springboot.repository.PersonRepository;
import academy.jairo.springboot.request.person.PersonPostRequestBody;
import academy.jairo.springboot.request.person.PersonPutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service //i am a spring bean
@RequiredArgsConstructor //inject dependency
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> listAll(){
        return personRepository.findAll();
    };

    public Page<Person> listAllWithPaging(Pageable pageable){
        return personRepository.findAll(pageable);
    };

    public List<Person> findByName(String name){
        return personRepository.findByName(name);
    };

    public Person findByIdOrThrowBadRequestException(long id){
        return personRepository.findById(id)
                .orElseThrow(() -> new AppBadRequestException("Person not found"));
    };

    public Person save(PersonPostRequestBody personPostRequestBody) {
        Person person = PersonMapper.INSTANCE.toPerson(personPostRequestBody);
        if (person.getName() == null) {
            throw new AppBadRequestException("MapStruct not work " + person);
        }
        return personRepository.save(person);
    }

    public void delete(long id) {
        personRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(PersonPutRequestBody personPutRequestBody) {
        findByIdOrThrowBadRequestException(personPutRequestBody.getId());
        personRepository.save(PersonMapper.INSTANCE.toPerson(personPutRequestBody));
    }
}
