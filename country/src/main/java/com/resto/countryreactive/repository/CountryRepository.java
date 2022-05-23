package com.resto.countryreactive.repository;

import com.resto.countryreactive.model.CountryEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


public interface CountryRepository extends ReactiveMongoRepository<CountryEntity, String> {

    Mono<CountryEntity> findCountryEntityByCountryCode(String countryCode);

    Mono<CountryEntity> findCountryEntityByCountryId(Integer id);
}
