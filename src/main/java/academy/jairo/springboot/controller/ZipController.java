package academy.jairo.springboot.controller;

import academy.jairo.springboot.rest.body.ZipBody;
import academy.jairo.springboot.rest.feign.ZipClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/zip")
@RequiredArgsConstructor
public class ZipController {

    private final ZipClient zipClient;

    @GetMapping("/{zipCode}")
    ResponseEntity<ZipBody> findAddressByZipCode(@PathVariable("zipCode") String zipCode) {
        return ResponseEntity.ok(zipClient.getAddressByZipCodeJustSimple(zipCode));
    }

}
