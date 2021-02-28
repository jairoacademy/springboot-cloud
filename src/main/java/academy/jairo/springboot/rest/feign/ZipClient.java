package academy.jairo.springboot.rest.feign;

import academy.jairo.springboot.rest.body.ZipBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "zipCodeClient", url = "http://viacep.com.br")
public interface ZipClient {

    @GetMapping(value = "/ws/{zipCode}/json/", consumes = "application/json")
    ZipBody getAddressByZipCodeJustSimple(@PathVariable("zipCode") String zipCode);

    @RequestMapping(method = RequestMethod.GET,
            value = "/ws/{zipCode}/json/",
            produces = "application/json")
    ZipBody getAddressByZipCodeJustVerbose(@PathVariable("zipCode") String zipCode);

}
