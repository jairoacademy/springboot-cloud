package academy.jairo.springboot.service;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.domain.PersonCreator;
import academy.jairo.springboot.domain.PersonPostRequestBodyCreator;
import academy.jairo.springboot.domain.PersonPutRequestBodyCreator;
import academy.jairo.springboot.exception.AppBadRequestException;
import academy.jairo.springboot.repository.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class PersonServiceTest {

    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepository personRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Person> personPage = new PageImpl<>(List.of(PersonCreator.create()));
        BDDMockito.when(personRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(personPage);

        BDDMockito.when(personRepositoryMock.findAll())
                .thenReturn(List.of(PersonCreator.create()));

        BDDMockito.when(personRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PersonCreator.createValid()));

        BDDMockito.when(personRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PersonCreator.create()));

        BDDMockito.when(personRepositoryMock.save(ArgumentMatchers.any(Person.class)))
                .thenReturn(PersonCreator.createValid());

        BDDMockito.doNothing().when(personRepositoryMock).delete(ArgumentMatchers.any(Person.class));

    }

    @Test
    @DisplayName("List of Person inside page object when success")
    void listAllWithPagingWhenSuccess() {
        String expectedName = PersonCreator.createValid().getName();

        Page<Person> personPage = personService.listAllWithPaging(PageRequest.of(1,1));

        Assertions.assertThat(personPage).isNotNull();

        Assertions.assertThat(personPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(personPage.toList()
                .get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("List all Person when success")
    void listAllWhenSuccess() {
        String expectedName = PersonCreator.createValid().getName();

        List<Person> listPerson = personService.listAll();

        Assertions.assertThat(listPerson)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(listPerson.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException and return Person when success")
    void findByIdOrThrowBadRequestExceptionWhenSuccess() {
        Long expectedId = PersonCreator.createValid().getId();

        Person person = personService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(person).isNotNull();

        Assertions.assertThat(person.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException when Person is not found")
    void findByIdOrThrowBadRequestExceptionWhenPersonIsNotFound() {
        BDDMockito.when(personRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(AppBadRequestException.class)
                .isThrownBy(() -> personService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("findByName and return Person when success")
    void findByNameWhenSuccess() {
        String expectedName = PersonCreator.createValid().getName();

        List<Person> listPerson = personService.findByName("Someone");

        Assertions.assertThat(listPerson)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(listPerson.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Save a Person and return Person when success")
    void saveWhenSuccess() {
        Person createPerson = PersonCreator.createValid();

        Person savedPerson = personService.save(PersonPostRequestBodyCreator.create());

        Assertions.assertThat(savedPerson).isNotNull();

        Assertions.assertThat(savedPerson.getId()).isNotNull();

        Assertions.assertThat(savedPerson.getName()).isEqualTo(createPerson.getName());
    }

    @Test
    @DisplayName("Update a Person and return Person when success")
    void replaceWhenSuccess() {
        Assertions.assertThatCode(() ->
                personService.replace(PersonPutRequestBodyCreator.create()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Delete a Person when success")
    void deleteWhenSuccess() {
        Assertions.assertThatCode(() ->
                personService.delete(1))
                .doesNotThrowAnyException();
    }
}