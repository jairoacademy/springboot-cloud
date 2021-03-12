package academy.jairo.springboot.rest.temp;

import academy.jairo.springboot.domain.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class PutRest {

    public static final String url = "http://localhost:8080/persons/";

    public static void main(String[] args) {

        Person createPerson = Person.builder().name("Samurai MyEggs").build();

        ResponseEntity<Person> savedPerson = new RestTemplate().exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(createPerson, createJsonHeader()),
                Person.class);
        log.info("Saved Person {}", savedPerson);

        if (savedPerson.getBody() != null) {
            Person putPerson = savedPerson.getBody();
            putPerson.setName("Samurai MyEggs II");
            ResponseEntity<Void> updatedPerson = new RestTemplate().exchange(url,
                    HttpMethod.PUT,
                    new HttpEntity<>(putPerson, createJsonHeader()),
                    Void.class);

            log.info("updatedPerson {}", updatedPerson);

            ResponseEntity<Void> deletePerson = new RestTemplate().exchange(url + "{id}",
                    HttpMethod.DELETE,
                    null,
                    Void.class,
                    putPerson.getId());

            log.info("deletePerson {}", deletePerson);
        }

    }

    public static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
