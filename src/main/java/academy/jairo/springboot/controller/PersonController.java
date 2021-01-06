package academy.jairo.springboot.controller;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController("/person")
@Log4j2
@AllArgsConstructor
public class PersonController {

    private DateUtil dateUtil;

    @GetMapping
    public List<Person> list() {
        log.info(dateUtil.formatLocalDate(LocalDateTime.now()));
        return List.of(new Person("John"), new Person("Mary"), new Person("Peter"));
    }

}
