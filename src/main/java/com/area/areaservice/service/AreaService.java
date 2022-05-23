package com.area.areaservice.service;

import com.area.areaservice.dto.AreaDto;
import com.area.areaservice.dto.CountryDataDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AreaService {

    Flux<AreaDto> findAll();
    Mono<AreaDto> findAreaById(String id);
    Mono<CountryDataDto> findAllAreaByCountryId(String countryId);
    Flux<AreaDto> findAllByAreaName(String name);
    Mono<AreaDto> saveArea(Mono<AreaDto> areaDto);
    Mono<CountryDataDto> saveAllData(Mono<CountryDataDto> countryDataDto);
    Mono<AreaDto> updateArea(Mono<AreaDto> areaDto,String id);
    Mono<Void> deleteArea(String id);
}
