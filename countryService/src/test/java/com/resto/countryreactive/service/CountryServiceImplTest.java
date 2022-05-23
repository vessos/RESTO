package com.resto.countryreactive.service;

import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.exceptionHandler.CustomerNotFoundException;
import com.resto.countryreactive.model.CountryEntity;
import com.resto.countryreactive.repository.CountryRepository;
import com.resto.countryreactive.utils.AppUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(CountryServiceImpl.class)
class CountryServiceImplTest {

    CountryServiceImpl countryService;

    @MockBean
    CountryRepository countryRepository;

    CountryEntity countryEntity;
    CountryEntity countryEntity1;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        countryService = new CountryServiceImpl(countryRepository);
        countryEntity = CountryEntity.builder()
                .countryId(1)
                .countryName("България")
                .countryNameEu("Bulgaria")
                .countryCode("BG").build();
        countryEntity1 = CountryEntity.builder()
                .countryId(1)
                .countryName("Гърция")
                .countryNameEu("Greece")
                .countryCode("GR").build();
    }

    @AfterEach
    void tearDown() throws Exception {
        countryEntity = null;
        countryEntity1 = null;
        autoCloseable.close();
    }

    @Test
    void getAllCountry() {
        Flux<CountryEntity> countryEntities = Flux.fromIterable(Arrays.asList(countryEntity, countryEntity1));
        when(countryRepository.findAll()).thenReturn(countryEntities);

        Flux<CountryDto> allCountry = countryService.getAllCountry();
        StepVerifier.create(allCountry)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getAllWhenMissingValue() {
        Flux<CountryEntity> countryEntities = Flux.empty();
        when(countryRepository.findAll()).thenReturn(countryEntities);
        Flux<CountryDto> allCountry = countryService.getAllCountry();
        StepVerifier.create(allCountry)
                .expectError(CustomerNotFoundException.class);
    }

    @Test
    void getCountryById() {
        Mono<CountryEntity> countryById = Mono.just(countryEntity);
        when(countryRepository.findById(anyString())).thenReturn(countryById);
        Mono<CountryDto> countryById1 = countryService.getCountryById("1");
        StepVerifier.create(countryById1)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getCountryByIdMissingValue() {
        Mono<CountryEntity> ce = Mono.empty();
        when(countryRepository.findById(anyString())).thenReturn(ce);
        Mono<CountryDto> countryById = countryService.getCountryById("1");
        StepVerifier.create(countryById)
                .expectError(CustomerNotFoundException.class);
    }

    @Test
    void getCountryByCountryId() {
        Mono<CountryEntity> countryById = Mono.just(countryEntity);
        when(countryRepository.findCountryEntityByCountryId(anyInt())).thenReturn(countryById);
        Mono<CountryDto> countryById1 = countryService.getCountryByCountryId(1);
        StepVerifier.create(countryById1)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getCountryByCountryIdMissingValue() {
        Mono<CountryEntity> ce = Mono.empty();
        when(countryRepository.findCountryEntityByCountryId(anyInt())).thenReturn(ce);
        Mono<CountryDto> countryById = countryService.getCountryByCountryId(1);
        StepVerifier.create(countryById)
                .expectError(CustomerNotFoundException.class);
    }

    @Test
    void getCountryByCountryCode() {
        Mono<CountryEntity> countryById = Mono.just(countryEntity);
        when(countryRepository.findCountryEntityByCountryCode(anyString())).thenReturn(countryById);
        Mono<CountryDto> countryById1 = countryService.getCountryByCountryCode("GR");
        StepVerifier.create(countryById1)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getCountryByCountryCodeMissingValue() {
        Mono<CountryEntity> ce = Mono.empty();
        when(countryRepository.findCountryEntityByCountryCode(anyString())).thenReturn(ce);
        Mono<CountryDto> countryById = countryService.getCountryByCountryCode("1");
        StepVerifier.create(countryById)
                .expectError(CustomerNotFoundException.class);
    }

    @Test
    void saveCountry() {
        countryEntity.setId("abcd");
        CountryDto countryDto = AppUtils.entityToDto(countryEntity);
        Mono<CountryEntity> country = Mono.just(countryEntity);
        when(countryRepository.save(any())).thenReturn(country);
        Mono<CountryDto> countryDtoMono = countryService.saveCountry(Mono.just(countryDto)).log();

        StepVerifier.create(countryDtoMono)
                .assertNext(c -> {
                    assertNotNull(c.getId());
                    assertEquals("abcd", c.getId());
                })
                .verifyComplete();
    }
}