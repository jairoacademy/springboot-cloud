package academy.jairo.springboot.controller;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.domain.PersonCreator;
import academy.jairo.springboot.domain.PersonPostRequestBodyCreator;
import academy.jairo.springboot.domain.PersonPutRequestBodyCreator;
import academy.jairo.springboot.request.person.PersonPostRequestBody;
import academy.jairo.springboot.request.person.PersonPutRequestBody;
import academy.jairo.springboot.service.PersonService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class PersonControllerTest {

    @InjectMocks
    PersonController personController;

    @Mock
    PersonService personServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Person> personPage = new PageImpl<>(List.of(PersonCreator.create()));
        BDDMockito.when(personServiceMock.listAllWithPaging(ArgumentMatchers.any()))
                .thenReturn(personPage);

        BDDMockito.when(personServiceMock.listAll())
                .thenReturn(List.of(PersonCreator.create()));

        BDDMockito.when(personServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(PersonCreator.createValid());

        BDDMockito.when(personServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PersonCreator.create()));

        BDDMockito.when(personServiceMock.save(ArgumentMatchers.any(PersonPostRequestBody.class)))
                .thenReturn(PersonCreator.createValid());

        //When a void method is call, use doNothing
        BDDMockito.doNothing().when(personServiceMock).replace(ArgumentMatchers.any(PersonPutRequestBody.class));

        BDDMockito.doNothing().when(personServiceMock).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("List of Person inside page object when success")
    void listAllWithPagingWhenSuccess() {
        String expectedName = PersonCreator.createValid().getName();

        Page<Person> personPage = personController.findAllWithPaging(null).getBody();

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

        List<Person> listPerson = personController.findAll().getBody();

        Assertions.assertThat(listPerson)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(listPerson.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById and return Person when success")
    void findByIdOrThrowBadRequestExceptionWhenSuccess() {
        Long expectedId = PersonCreator.createValid().getId();

        Person person = personController.findById(1).getBody();

        Assertions.assertThat(person).isNotNull();

        Assertions.assertThat(person.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName and return Person when success")
    void findByNameWhenSuccess() {
        String expectedName = PersonCreator.createValid().getName();

        List<Person> listPerson = personController.findByName("Samba").getBody();

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

        Person savedPerson = personController.save(PersonPostRequestBodyCreator.create()).getBody();

        Assertions.assertThat(savedPerson).isNotNull();

        Assertions.assertThat(savedPerson.getId()).isNotNull();

        Assertions.assertThat(savedPerson.getName()).isEqualTo(createPerson.getName());
    }

    @Test
    @DisplayName("Update a Person and return Person when success")
    void replaceWhenSuccess() {
        Assertions.assertThatCode(() ->
                personController.replace(PersonPutRequestBodyCreator.create()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> replace = personController.replace(PersonPutRequestBodyCreator.create());

        Assertions.assertThat(replace).isNotNull();
    }

    @Test
    @DisplayName("Delete a Person when success")
    void deleteWhenSuccess() {
        Assertions.assertThatCode(() ->
                personController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> delete = personController.delete(1);

        Assertions.assertThat(delete).isNotNull();
    }

}