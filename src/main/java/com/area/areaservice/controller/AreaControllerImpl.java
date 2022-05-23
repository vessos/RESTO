package com.area.areaservice.controller;

import com.area.areaservice.dto.AreaDto;
import com.area.areaservice.dto.CountryDataDto;
import com.area.areaservice.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AreaControllerImpl implements AreaController {

    private final AreaService areaService;

    public AreaControllerImpl(AreaService areaService) {
        this.areaService = areaService;
    }

    @Override
    public Flux<AreaDto> getAllArea() {
        return areaService.findAll();
    }

    @Override
    public Mono<CountryDataDto> getAllAreaByCountryId(String countryId) {
        return areaService.findAllAreaByCountryId(countryId);
    }

    @Override
    public Mono<AreaDto> getAreaById(String id) {
        return areaService.findAreaById(id);
    }

    @Override
    public Flux<AreaDto> getAreaByName(String name) {
        return areaService.findAllByAreaName(name);
    }

    @Override
    public Mono<AreaDto> saveArea(Mono<AreaDto> areaDtoMono) {
        return areaService.saveArea(areaDtoMono);
    }

    @Override
    public Mono<CountryDataDto> saveAreaAndCountry(Mono<CountryDataDto> countryDataDtoMono) {
        return areaService.saveAllData(countryDataDtoMono);
    }

    @Override
    public Mono<AreaDto> updateArea(Mono<AreaDto> areaDto, String id) {
        return areaService.updateArea(areaDto,id);
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return areaService.deleteArea(id);
    }
}
