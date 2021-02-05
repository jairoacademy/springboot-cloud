package academy.jairo.springboot.integration;

import academy.jairo.springboot.domain.AppUser;
import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.domain.PersonCreator;
import academy.jairo.springboot.domain.PersonPostRequestBodyCreator;
import academy.jairo.springboot.repository.AppUserRepository;
import academy.jairo.springboot.repository.PersonRepository;
import academy.jairo.springboot.request.person.PersonPostRequestBody;
import academy.jairo.springboot.wrapper.PageableResponse;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersonControllerIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    AppUserRepository appUserRepository;

    static final AppUser USER = AppUser.builder()
            .name("Mary")
            .username("mary")
            .password("{bcrypt}$2a$10$6XaK5xZdCSATyXY3BJWTYuhvYg.qWBUjnWrNCYx5AL6Ndqo272OsW")
            .authorities("ROLE_USER,USER")
            .build();

    static final AppUser ADMIN = AppUser.builder()
            .name("Jairo")
            .username("jairo")
            .password("{bcrypt}$2a$10$6XaK5xZdCSATyXY3BJWTYuhvYg.qWBUjnWrNCYx5AL6Ndqo272OsW")
            .authorities("ROLE_USER,ROLE_ADMIN,USER,ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(
                @Value("${local.server.port}") int port ) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("mary", "321654");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(
                @Value("${local.server.port}") int port ) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("jairo", "321654");
            return new TestRestTemplate(restTemplateBuilder);
        }

    }

    @Autowired
    PersonRepository personRepository;

    @Test
    @DisplayName("List of Person inside page object when success")
    void listAllWithPagingWhenSuccess() {
        Person savedPerson = personRepository.save(PersonCreator.create());
        appUserRepository.save(ADMIN);

        String expectedName = savedPerson.getName();

        PageableResponse<Person> personPage = testRestTemplateRoleAdmin
                .exchange("/persons/admin/pg", HttpMethod.GET, null,
                        new ParameterizedTypeReference<PageableResponse<Person>>() {
                        }).getBody();

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
        Person savedPerson = personRepository.save(PersonCreator.create());
        appUserRepository.save(USER);

        String expectedName = savedPerson.getName();

        List<Person> personList = testRestTemplateRoleUser
                .exchange("/persons", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Person>>() {
                        }).getBody();

        Assertions.assertThat(personList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(personList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById and return Person when success")
    void findByIdOrThrowBadRequestExceptionWhenSuccess() {
        Person savedPerson = personRepository.save(PersonCreator.create());
        appUserRepository.save(USER);

        Long expectedId = savedPerson.getId();

        Person person = testRestTemplateRoleUser.getForObject("/persons/{id}", Person.class, expectedId);

        Assertions.assertThat(person).isNotNull();

        Assertions.assertThat(person.getId()).isEqualTo(expectedId);
    }


    @Test
    @DisplayName("findByName and return Person when success")
    void findByNameWhenSuccess() {
        Person savedPerson = personRepository.save(PersonCreator.create());
        appUserRepository.save(USER);

        String expectedName = savedPerson.getName();

        String url = String.format("//persons/find/%s", expectedName);

        List<Person> personList = testRestTemplateRoleUser
                .exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Person>>() {
                        }).getBody();


        Assertions.assertThat(personList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(personList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Save a Person and return Person when success")
    void saveWhenSuccess() {
        PersonPostRequestBody personPostRequestBody = PersonPostRequestBodyCreator.create();
        appUserRepository.save(ADMIN);

        ResponseEntity<Person> personResponseEntity = testRestTemplateRoleAdmin
                .postForEntity("/persons", personPostRequestBody, Person.class);

        Assertions.assertThat(personResponseEntity).isNotNull();

        Assertions.assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(personResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(personResponseEntity.getBody().getId()).isNotNull();
   }

   @Test
   @DisplayName("Replace a Person and return Person when success")
   void replaceWhenSuccess() {
        Person savedPerson = personRepository.save(PersonCreator.createValidUpdated());
        appUserRepository.save(USER);
        savedPerson.setName("New Name");

        ResponseEntity<Void> personResponseEntity = testRestTemplateRoleUser.exchange("/persons",
                HttpMethod.PUT,
                new HttpEntity<>(savedPerson),
                Void.class);

        Assertions.assertThat(personResponseEntity).isNotNull();

        Assertions.assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete a Person when success")
    void deleteWhenSuccess() {
        Person savedPerson = personRepository.save(PersonCreator.create());
        appUserRepository.save(USER);

        ResponseEntity<Void> personResponseEntity = testRestTemplateRoleUser.exchange("/persons/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedPerson.getId());

        Assertions.assertThat(personResponseEntity).isNotNull();

        Assertions.assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
