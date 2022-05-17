package com.resto.countryreactive.service;

import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.exceptionHandler.CustomerNotFoundException;
import com.resto.countryreactive.repository.CountryRepository;
import com.resto.countryreactive.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.function.BiConsumer;

@Service
public class CountryServiceImpl implements CountryService{

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Flux<CountryDto> getAllCountry() {
        return countryRepository.findAll()
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Flux.error(new CustomerNotFoundException("In DB missing any records ")));
    }

    @Override
    public Mono<CountryDto> getCountryById(String id) {
        return countryRepository.findById(id).map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("In DB doesnt have record with this Id")));
    }

    @Override
    public Mono<CountryDto> getCountryByCountryId(Integer countryId) {
        return countryRepository.findCountryEntityByCountryId(countryId).map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("In DB doesnt have record with this Id")));
    }

    @Override
    public Mono<CountryDto> getCountryByCountryCode(String countryCode) {
        return countryRepository.findCountryEntityByCountryCode(countryCode).map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("In DB doesnt have record with this country code")));
    }

    @Override
    public Mono<CountryDto> saveCountry(Mono<CountryDto> countryDtoMono) {
        return countryDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(countryRepository::insert)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("It has a problem with data insert!")));

    }

    @Override
    public Mono<CountryDto> updateCountry(Mono<CountryDto> countryDtoMono, String id) {
        return countryRepository.findById(id)
            .flatMap(p->countryDtoMono.map(AppUtils::dtoToEntity)
            .doOnNext(e-> e.setId(id))
            .flatMap(countryRepository::save))
            .map(AppUtils::entityToDto)
            .switchIfEmpty(Mono.error(new CustomerNotFoundException("It has a problem with data update!")));
    }

    @Override
    public Mono<Void> deleteCountry(String id) {
        return countryRepository.deleteById(id);
    }
}
