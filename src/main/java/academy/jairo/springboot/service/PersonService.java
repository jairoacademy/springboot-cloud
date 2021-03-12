package academy.jairo.springboot.service;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.exception.AppBadRequestException;
import academy.jairo.springboot.mapper.PersonMapper;
import academy.jairo.springboot.repository.PersonRepository;
import academy.jairo.springboot.request.person.PersonPostRequestBody;
import academy.jairo.springboot.request.person.PersonPutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //i am a spring bean
@RequiredArgsConstructor //inject dependency
public class PersonService {

    private final PersonRepository personRepository;

    @Cacheable(cacheNames = Person.CACHE_NAME, key="#root.method.name")
    public List<Person> listAll(){
        return personRepository.findAll();
    }

    public Page<Person> listAllWithPaging(Pageable pageable){
        return personRepository.findAll(pageable);
    }

    @Cacheable(value = "person-single", key="#id")
    public Person findByIdOrThrowBadRequestException(long id){
        return personRepository.findById(id)
                .orElseThrow(() -> new AppBadRequestException("Person not found"));
    }

    public List<Person> findByName(String name){
        return personRepository.findByName(name);
    }

    @CacheEvict(cacheNames = Person.CACHE_NAME, allEntries = true)
    public Person save(PersonPostRequestBody personPostRequestBody) {
        Person person = PersonMapper.INSTANCE.toPerson(personPostRequestBody);
        return personRepository.save(person);
    }

    public void delete(long id) {
        personRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    @CacheEvict(cacheNames = Person.CACHE_NAME, allEntries = true)
    public void replace(PersonPutRequestBody personPutRequestBody) {
        findByIdOrThrowBadRequestException(personPutRequestBody.getId());
        personRepository.save(PersonMapper.INSTANCE.toPerson(personPutRequestBody));
    }

}
