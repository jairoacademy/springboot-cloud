package academy.jairo.springboot.rest;

import academy.jairo.springboot.domain.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class GetRest {
    public static final String url = "http://localhost:8080/persons/";

    public static void main(String[] args) {

        ResponseEntity<Person> entity = new RestTemplate().getForEntity(url + "2" , Person.class);
        log.info(entity);

        //Get a simple Person
        Person object = new RestTemplate().getForObject(url + "2", Person.class);
        log.info(object);

        //Get a list of Person
        ResponseEntity<List<Person>> exchange = new RestTemplate()
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Person>>() {
                });
        log.info(exchange.getBody());

    }
}
