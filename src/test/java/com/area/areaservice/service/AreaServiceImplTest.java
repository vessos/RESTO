package com.area.areaservice.service;

import com.area.areaservice.dto.AreaDto;
import com.area.areaservice.dto.CountryDataDto;
import com.area.areaservice.dto.CountryDto;
import com.area.areaservice.exceptionHandler.AreaNotFoundException;
import com.area.areaservice.model.AreaEntity;
import com.area.areaservice.repository.AreaRepository;
import com.area.areaservice.service.client.CountryFeignClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(AreaServiceImpl.class)
class AreaServiceImplTest {

    AreaService areaService;
    @MockBean
    AreaRepository areaRepository;
    @MockBean
    CountryFeignClient countryFeignClient;

    AreaEntity areaEntity;
    AreaEntity areaEntity1;
    CountryDto countryDto;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        areaService = new AreaServiceImpl(areaRepository,countryFeignClient);
        areaEntity = AreaEntity.builder().countryId("testId").areaName("testName").areaNameEN("testNameEn").build();
        areaEntity1 = AreaEntity.builder().countryId("testId1").areaName("testName1").areaNameEN("testNameEn1").build();
        countryDto = CountryDto.builder().id("testId").countryCode("BG").countryId(1).countryName("Bulgaria").countryNameEu("bulgaria").build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findAll() {
        Flux<AreaEntity> areaEntityFlux = Flux.fromIterable(List.of(areaEntity,areaEntity1));
        when(areaRepository.findAll()).thenReturn(areaEntityFlux);

        Flux<AreaDto> all = areaService.findAll();
        StepVerifier.create(all)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findAllWhenWeHaveMissingValue() {
        Flux<AreaEntity> areaEntityFlux = Flux.empty();
        when(areaRepository.findAll()).thenReturn(areaEntityFlux);

        Flux<AreaDto> all = areaService.findAll();
        StepVerifier.create(all)
                .expectError(AreaNotFoundException.class);
    }

    @Test
    void findAreaById() {
        Mono<AreaEntity> areaEntityMono = Mono.just(areaEntity);

        when(areaRepository.findById(anyString())).thenReturn(areaEntityMono);

        Mono<AreaDto> areaById = areaService.findAreaById("1");
        StepVerifier.create(areaById)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findAreaByIdMissingValue() {
        Mono<AreaEntity> areaEntityMono = Mono.empty();

        when(areaRepository.findById(anyString())).thenReturn(areaEntityMono);

        Mono<AreaDto> areaById = areaService.findAreaById("1");
        StepVerifier.create(areaById)
                .expectError(AreaNotFoundException.class);
    }

    @Test
    void findAllAreaByCountryId() {
        Flux<AreaEntity> areaEntityMono = Flux.fromIterable(List.of(areaEntity,areaEntity1));
        Mono<CountryDto> countryDtoMono = Mono.just(countryDto);

        when(areaRepository.findAllByCountryId(anyString())).thenReturn(areaEntityMono);
        when(countryFeignClient.getCountryById(anyString())).thenReturn(countryDtoMono);

        Mono<CountryDataDto> allAreaByCountryId = areaService.findAllAreaByCountryId("1");
        StepVerifier.create(allAreaByCountryId)
                .assertNext(a -> {
                    assertNotNull(a);
                    assertNotNull(a.getCountryDto());
                    assertEquals(countryDto.getId(),a.getCountryDto().getId());
                    assertNotNull(a.getAreaList());
                    assertEquals(2,a.getAreaList().size());
                })
                .verifyComplete();
    }

    @Test
    void findAllByAreaName() {
        Flux<AreaEntity> areaDtoFlux = Flux.fromIterable(List.of(areaEntity,areaEntity1));

        when(areaRepository.findAllByAreaName(anyString())).thenReturn(areaDtoFlux);
        Flux<AreaDto> testName = areaService.findAllByAreaName("testName");
        StepVerifier.create(testName)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findAllByAreaNameMissingValue() {
        Flux<AreaEntity> areaDtoFlux = Flux.empty();

        when(areaRepository.findAllByAreaName(anyString())).thenReturn(areaDtoFlux);
        Flux<AreaDto> testName = areaService.findAllByAreaName("testName");
        StepVerifier.create(testName)
                .expectError(AreaNotFoundException.class);
    }

    @Test
    void saveArea() {
    }

    @Test
    void saveAllData() {
    }

    @Test
    void updateArea() {
    }

    @Test
    void deleteArea() {
    }
}