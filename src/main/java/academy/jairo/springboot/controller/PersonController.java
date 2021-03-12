package academy.jairo.springboot.controller;

import academy.jairo.springboot.domain.Person;
import academy.jairo.springboot.request.person.PersonPostRequestBody;
import academy.jairo.springboot.request.person.PersonPutRequestBody;
import academy.jairo.springboot.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor //personService thanks with final declaration
public class PersonController {

    private final PersonService personService;

    @PreAuthorize(("hasRole('ADMIN')"))
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Return a success operation"),
            @ApiResponse(responseCode = "400", description = "When a Person does not exist")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        log.info("Delete a Person");
        personService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "List all Person",
            tags = {"list-person"})
    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        log.info("Listing Person");
        return new ResponseEntity<>(personService.listAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "List all Person paginated",
            description = "The default size is 20, use the parameter size do change the value",
            tags = {"list-person"})
    @GetMapping("/admin/pg")
    public ResponseEntity<Page<Person>> findAllWithPaging(
            @ParameterObject Pageable pageable) {
        log.info("Listing Person");
        return ResponseEntity.ok(personService.listAllWithPaging(pageable));
    }

    @Operation(
            summary = "Get a Person by ID",
            tags = {"get-person"})
    @GetMapping(path = "/{id}")
    public ResponseEntity<Person> findById(@PathVariable long id) {
        log.info("Find a Person");
        return ResponseEntity.ok(personService.findByIdOrThrowBadRequestException(id));
    }

    @Operation(
            summary = "Get a Person by authentication",
            tags = {"get-person"})
    @GetMapping(path = "/find-by-auth/{id}")
    public ResponseEntity<Person> findByIdWithAuthentication(
            @PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Find a Person with authentication -> {}", userDetails );
        return ResponseEntity.ok(personService.findByIdOrThrowBadRequestException(id));
    }

    @Operation(
            summary = "Lit Person by name",
            tags = {"list-person"})
    @GetMapping("/find/{name}")
    public ResponseEntity<List<Person>> findByName(@PathVariable String name) {
        log.info("Listing Person by name");
        return ResponseEntity.ok(personService.findByName(name));
    }

    @Operation(
            summary = "Lit Person by param name",
            tags = {"list-person"})
    @GetMapping("/find")
    public ResponseEntity<List<Person>> findByNameParam(@RequestParam String name) {
        log.info("Listing Person by name");
        return ResponseEntity.ok(personService.findByName(name));
    }

}
