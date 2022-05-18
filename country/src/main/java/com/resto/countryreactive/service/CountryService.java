package com.resto.countryreactive.service;


import com.resto.countryreactive.dto.CountryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CountryService {

    public Flux<CountryDto> getAllCountry();

    public Mono<CountryDto> getCountryById(String id);

    public Mono<CountryDto> getCountryByCountryId(Integer countryId);

    public Mono<CountryDto> getCountryByCountryCode(String countryCode);

    public Mono<CountryDto> saveCountry(Mono<CountryDto> countryDto);

    public Flux<CountryDto> saveAllCountry(Flux<CountryDto> countries);

    public Mono<CountryDto> updateCountry(Mono<CountryDto> countryDtoMono,String id);

    public Mono<Void> deleteCountry(String id);

}
