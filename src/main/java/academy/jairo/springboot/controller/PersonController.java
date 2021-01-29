package academy.jairo.springboot.controller;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.request.person.PersonPostRequestBody;
import academy.jairo.springboot.request.person.PersonPutRequestBody;
import academy.jairo.springboot.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor //personService thanks with final declaration
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<Person> save(
            @RequestBody PersonPostRequestBody personPostRequestBody) {
        log.info("Save a Person");
        return new ResponseEntity<>(personService.save(personPostRequestBody), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody PersonPutRequestBody personPutRequestBody) {
        log.info("Update a Person");
        personService.replace(personPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.info("Delete a Person");
        personService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        log.info("Listing Person");
        return new ResponseEntity<>(personService.listAll(), HttpStatus.OK);
    }

    @GetMapping("/pg")
    public ResponseEntity<Page<Person>> findAllWithPaging(Pageable pageable) {
        log.info("Listing Person");
        return ResponseEntity.ok(personService.listAllWithPaging(pageable));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Person> findById(@PathVariable long id) {
        log.info("Find a Person");
        return ResponseEntity.ok(personService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<List<Person>> findByName(@PathVariable String name) {
        log.info("Listing Person by name");
        return ResponseEntity.ok(personService.findByName(name));
    }

    @GetMapping("/find")
    public ResponseEntity<List<Person>> findByNameParam(@RequestParam String name) {
        log.info("Listing Person by name");
        return ResponseEntity.ok(personService.findByName(name));
    }

}
