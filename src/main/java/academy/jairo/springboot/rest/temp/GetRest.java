package academy.jairo.springboot.rest.temp;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.rest.body.ZipBody;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class GetRest {
    public static final String url = "http://localhost:8080/persons/";

    public static void main(String[] args) {
        loadZipCodeService();
    }

    //Use Feign, to makes writing java http clients easier
    private static void loadZipCodeService() {
        loading();
        String uri = "http://viacep.com.br/ws/38400386/json/";
        ZipBody entity = new RestTemplate().getForObject(uri  , ZipBody.class);
        log.info(entity);
    }

    private static void loading() {
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
                        new ParameterizedTypeReference<>() {
                        });
        log.info(exchange.getBody());
    }

}
