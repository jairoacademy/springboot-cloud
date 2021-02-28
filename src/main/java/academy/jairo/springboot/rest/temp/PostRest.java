package academy.jairo.springboot.rest.temp;

import academy.jairo.springboot.domain.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class PostRest {
    public static final String url = "http://localhost:8080/persons/";
    public static void main(String[] args) {

        Person kingdom = Person.builder().name("Kingdom 3").build();
        Person kingdomSaved = new RestTemplate().postForObject(url, kingdom, Person.class);
        log.info("Saved person {}", kingdomSaved);

    }
}
