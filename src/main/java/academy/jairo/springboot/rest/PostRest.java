package academy.jairo.springboot.rest;

import academy.jairo.springboot.domain.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class PostRest {
    public static final String url = "http://localhost:8080/persons/";
    public static void main(String[] args) {

        Person kingdom = Person.builder().name("Kingdom 3").build();
        Person kingdomSaved = new RestTemplate().postForObject(url, kingdom, Person.class);
        log.info("Saved person {}", kingdomSaved);

    }
}
