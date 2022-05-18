package com.resto.countryreactive.controller.impl;

import com.resto.countryreactive.controller.CountryController;
import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CountryControllerImpl implements CountryController {

    public final CountryService countryService;

    @Autowired
    public CountryControllerImpl(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public Flux<CountryDto> getAllCountries() {
        return countryService.getAllCountry();
    }

    @Override
    public Mono<CountryDto> getCountryById(String id) {
        return countryService.getCountryById(id);
    }

    @Override
    public Mono<CountryDto> getCountryByCountryCode(String countryCode) {
        return countryService.getCountryByCountryCode(countryCode);
    }

    @Override
    public Mono<CountryDto> saveProduct(Mono<CountryDto> countryDtoMono) {
        return countryService.saveCountry(countryDtoMono);
    }

    @Override
    public Mono<CountryDto> updateProduct(Mono<CountryDto> countryDtoMono, String id) {
        return countryService.updateCountry(countryDtoMono,id);
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return countryService.deleteCountry(id);
    }
}
