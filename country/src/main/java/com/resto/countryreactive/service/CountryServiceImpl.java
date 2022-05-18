package com.resto.countryreactive.service;

import com.mongodb.MongoException;
import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.exceptionHandler.CustomerNotFoundException;
import com.resto.countryreactive.repository.CountryRepository;
import com.resto.countryreactive.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CountryServiceImpl implements CountryService{

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Flux<CountryDto> getAllCountry() {
        log.info("Start GET ALL request to CountryService");
        return countryRepository.findAll()
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Flux.error(new CustomerNotFoundException("In DB missing any records ")));
    }

    @Override
    public Mono<CountryDto> getCountryById(String id) {
        log.info("Start GET byId request to CountryService");
        return countryRepository.findById(id).map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("In DB doesnt have record with this Id")));
    }

    @Override
    public Mono<CountryDto> getCountryByCountryId(Integer countryId) {
        log.info("Start GET ByCountryId request to CountryService");
        return countryRepository.findCountryEntityByCountryId(countryId).map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("In DB doesnt have record with this Id")));
    }

    @Override
    public Mono<CountryDto> getCountryByCountryCode(String countryCode) {
        log.info("Start GET byCountryCode request to CountryService");
        return countryRepository.findCountryEntityByCountryCode(countryCode).map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("In DB doesnt have record with this country code")));
    }

    @Override
    public Mono<CountryDto> saveCountry(Mono<CountryDto> countryDtoMono) {
        log.info("Start POST request to CountryService");
        return countryDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(countryRepository::save)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("It has a problem with data insert!"))).log();

    }

    @Override
    public Flux<CountryDto> saveAllCountry(Flux<CountryDto> countries) {
        log.info("Start to saveAll POST request to CountryService");
        return countries.map(AppUtils::dtoToEntity)
                .flatMap(countryRepository::save)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Flux.error(new MongoException("Problem with save records to DB")));
    }

    @Override
    public Mono<CountryDto> updateCountry(Mono<CountryDto> countryDtoMono, String id) {
        log.info("Start PUT request to CountryService");
        return countryRepository.findById(id)
            .flatMap(p->countryDtoMono.map(AppUtils::dtoToEntity)
            .doOnNext(e-> e.setId(id))
            .flatMap(countryRepository::save))
            .map(AppUtils::entityToDto)
            .switchIfEmpty(Mono.error(new CustomerNotFoundException("It has a problem with data update!")));
    }

    @Override
    public Mono<Void> deleteCountry(String id) {
        log.info("Start DELETE request to CountryService");
        return countryRepository.deleteById(id);
    }
}
