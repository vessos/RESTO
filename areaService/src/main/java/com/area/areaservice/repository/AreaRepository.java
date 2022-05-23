package com.area.areaservice.repository;


import com.area.areaservice.model.AreaEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AreaRepository extends ReactiveMongoRepository<AreaEntity,String> {

    Flux<AreaEntity> findAllByCountryId(String countryId);
    Flux<AreaEntity> findAllByAreaName(String areaName);
}
