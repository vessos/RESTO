package com.area.areaservice.service;

import com.area.areaservice.dto.AreaDto;
import com.area.areaservice.dto.CountryDataDto;
import com.area.areaservice.dto.CountryDto;
import com.area.areaservice.exceptionHandler.AreaNotFoundException;

import com.area.areaservice.repository.AreaRepository;
import com.area.areaservice.service.client.CountryFeignClient;
import com.area.areaservice.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;
    private final CountryFeignClient countryFeignClient;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository, CountryFeignClient countryFeignClient) {
        this.areaRepository = areaRepository;
        this.countryFeignClient = countryFeignClient;
    }

    @Override
    public Flux<AreaDto> findAll() {
        log.info("Start GET all Areas from AreaService");
        return areaRepository.findAll()
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Flux.error(new AreaNotFoundException("DB doesn't contains any records")));
    }

    @Override
    public Mono<AreaDto> findAreaById(String id) {
        log.info("Start GET area by ID from AreaService");
        return areaRepository.findById(id)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new AreaNotFoundException("In DB doesn't have record with this ID : " + id)));
    }

    @Override
    public Mono<CountryDataDto> findAllAreaByCountryId(String countryId) {
        log.info("Start GET Areas By countryId from AreaService");
        return countryFeignClient.getCountryById(countryId)
                .flatMap(country -> {
                   var areasByCountryId = areaRepository.findAllByCountryId(countryId)
                           .map(AppUtils::entityToDto)
                           .collectList();
                   return areasByCountryId.map(area -> new CountryDataDto(country,area));
                })
                .switchIfEmpty(Mono.error(new AreaNotFoundException("In DB missing areas with given country id")));
    }

    @Override
    public Flux<AreaDto> findAllByAreaName(String name) {
        log.info("Start GET Areas By areaName from AreaService");
        return areaRepository.findAllByAreaName(name)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Flux.error(new AreaNotFoundException("DB doesn't contains any records")));
    }

    @Override
    public Mono<AreaDto> saveArea(Mono<AreaDto> areaDto) {
        log.info("Start POST request from AreaService");
        return areaDto.map(AppUtils::DtoToEntity)
                .flatMap(areaRepository::save)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new AreaNotFoundException("It has a problem with data insert!"))).log();
    }

    @Override
    public Mono<CountryDataDto> saveAllData(Mono<CountryDataDto> countryDataDto) {
        log.info("Start POST STRONG operation from AreaService");
        return countryDataDto.flatMap(cdo -> {
                    CountryDataDto cddto = new CountryDataDto();
                    Mono<CountryDto> countryDtoMonoSaved = countryFeignClient.saveCountry(Mono.just(cdo.getCountryDto()));
                    countryDtoMonoSaved.doOnNext(i -> cdo.setCountryDto(i));
                    Mono<List<AreaDto>> listMono = Flux.fromIterable(cdo.getAreaList()).map(AppUtils::DtoToEntity)
                            .flatMap(areaRepository::save)
                            .map(AppUtils::entityToDto)
                            .collectList();
                    listMono.doOnNext(cdo::setAreaList);
                    return Mono.just(cdo);
                }
        );
    }

    @Override
    public Mono<AreaDto> updateArea(Mono<AreaDto> areaDto, String id) {
        log.info("Start PUT request from AreaService");
        return areaRepository.findById(id)
                .flatMap(a-> areaDto.map(AppUtils::DtoToEntity)
                .doOnNext(e->e.setId(id))
                .flatMap(areaRepository::save))
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new AreaNotFoundException("It has a problem with data update!")));
    }

    @Override
    public Mono<Void> deleteArea(String id) {
        log.info("Start DELETE operation in AreaDB");
        return areaRepository.deleteById(id);
    }
}
