package academy.jairo.springboot.repository;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.domain.PersonCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Test for a Person Repository")
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("Save Person when success")
    void savePersonWhenSuccess() {
        Person createPerson = PersonCreator.create();

        Person savedPerson = this.personRepository.save(createPerson);

        Assertions.assertThat(savedPerson).isNotNull();

        Assertions.assertThat(savedPerson.getId()).isNotNull();

        Assertions.assertThat(savedPerson.getName()).isEqualTo(createPerson.getName());
    }

    @Test
    @DisplayName("Save Person throw Exception where name is empty")
    void savePersonThrowsExceptionWhenNameIsEmpty() {
        Person createPerson = new Person();

        Assertions.assertThatThrownBy(() -> this.personRepository.save(createPerson))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save Person throw Exception where name is empty 2")
    void savePersonThrowsExceptionWhenNameIsEmpty2() {
        Person createPerson = new Person();

        //Is tha same result of different way
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.personRepository.save(createPerson));
    }

    @Test
    @DisplayName("Delete Person when success")
    void deletePersonWhenSuccess() {
        Person createPerson = PersonCreator.create();

        Person savedPerson = this.personRepository.save(createPerson);

        this.personRepository.delete(savedPerson);

        Optional<Person> optionalPerson = this.personRepository.findById(savedPerson.getId());

        Assertions.assertThat(optionalPerson.isEmpty());
    }

    @Test
    @DisplayName("Find Person by name when success")
    void findByNameReturnListPersonWhenSuccess() {
        Person createPerson = PersonCreator.create();

        Person savedPerson = this.personRepository.save(createPerson);

        var name = savedPerson.getName();

        List<Person> listPerson = this.personRepository.findByName(name);

        Assertions.assertThat(listPerson)
                .isNotEmpty()
        ;

        Assertions.assertThat(listPerson);
    }

    @Test
    @DisplayName("Find a Person when is not exist")
    void findByNameReturnEmptyListPersonWhenIsNotFound() {
        List<Person> listPerson = this.personRepository.findByName("Gabriel Angel");

        Assertions.assertThat(listPerson).isEmpty();
    }

}