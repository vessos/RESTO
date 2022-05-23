package com.area.areaservice.service.client;

import com.area.areaservice.dto.CountryDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@Component
@ReactiveFeignClient(value="country",url = "${movie.service.url}")
public interface CountryFeignClient {

    @RequestMapping(method = RequestMethod.GET,value = "/country/{id}",consumes = "application/json")
    Mono<CountryDto> getCountryById(@PathVariable("id") String countryId);
    @RequestMapping(method = RequestMethod.POST,value = "/country",consumes = "application/json")
    Mono<CountryDto> saveCountry(@RequestBody Mono<CountryDto> countryDto);
}
