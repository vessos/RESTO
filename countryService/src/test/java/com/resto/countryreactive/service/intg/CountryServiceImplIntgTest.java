package com.resto.countryreactive.service.intg;

import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.exceptionHandler.CustomerNotFoundException;
import com.resto.countryreactive.model.CountryEntity;
import com.resto.countryreactive.repository.CountryRepository;
import com.resto.countryreactive.service.CountryServiceImpl;
import com.resto.countryreactive.utils.AppUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ActiveProfiles("test")
public class CountryServiceImplIntgTest {

    CountryServiceImpl countryService;

    @Autowired
    CountryRepository countryRepository;

    CountryEntity countryEntity;
    CountryEntity countryEntity1;

    @BeforeEach
    void setUp() {
        countryEntity = CountryEntity.builder()
                .countryId(1)
                .countryName("България")
                .countryNameEu("Bulgaria")
                .countryCode("BG").build();
        countryEntity1 = CountryEntity.builder()
                .countryId(2)
                .countryName("Гърция")
                .countryNameEu("Greece")
                .countryCode("GR").build();

        countryService = new CountryServiceImpl(countryRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        countryRepository.deleteAll().block();
    }

    @Test
    void getAllCountry() {
        var countries = List.of(countryEntity, countryEntity1);
        countryRepository.saveAll(countries).blockLast();
        Flux<CountryDto> allCountry = countryService.getAllCountry();
        StepVerifier.create(allCountry)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getAllWhenMissingValue() {
        Flux<CountryDto> allCountry = countryService.getAllCountry();
        StepVerifier.create(allCountry)
                .expectError(CustomerNotFoundException.class);
    }

    @Test
    void getCountryById() {
        var countries = List.of(countryEntity, countryEntity1);
        CountryEntity countryEntity = countryRepository.saveAll(countries).blockLast();
        Mono<CountryDto> countryById1 = countryService.getCountryById(countryEntity.getId());
        StepVerifier.create(countryById1)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getCountryByIdMissingValue() {
        Mono<CountryDto> countryById = countryService.getCountryById("1");
        StepVerifier.create(countryById)
                .expectError(CustomerNotFoundException.class);
    }

    @Test
    void getCountryByCountryId() {
        var countries = List.of(countryEntity, countryEntity1);
        countryRepository.saveAll(countries).blockLast();
        Mono<CountryDto> countryById1 = countryService.getCountryByCountryId(1);
        StepVerifier.create(countryById1)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getCountryByCountryIdMissingValue() {
        Mono<CountryDto> countryById = countryService.getCountryByCountryId(1);
        StepVerifier.create(countryById)
                .expectError(CustomerNotFoundException.class);
    }

    @Test
    void getCountryByCountryCode() {
        var countries = List.of(countryEntity, countryEntity1);
        countryRepository.saveAll(countries).blockLast();
        Mono<CountryDto> countryById1 = countryService.getCountryByCountryCode("GR");
        StepVerifier.create(countryById1)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getCountryByCountryCodeMissingValue() {
        Mono<CountryDto> countryById = countryService.getCountryByCountryCode("1");
        StepVerifier.create(countryById)
                .expectError(CustomerNotFoundException.class);
    }

    @Test
    void saveCountry() {
        CountryEntity ce = CountryEntity.builder().countryCode("EU").countryName("Europe").build();
        CountryDto countryDto = AppUtils.entityToDto(ce);

        CountryDto block = countryService.saveCountry(Mono.just(countryDto)).block();
        assertNotNull(block.getId());
    }

    @Test
    void updateCountry() {
        CountryEntity ce = CountryEntity.builder().countryCode("EU").countryName("Europe").build();
        CountryDto countryDto = AppUtils.entityToDto(ce);

        CountryDto block = countryService.saveCountry(Mono.just(countryDto)).block();
        block.setCountryCode("EUR");
        block.setCountryName("Европа");
        block.setCountryNameEu("Europe");
        CountryDto updatedCountry = countryService.updateCountry(Mono.just(block), block.getId()).block();
        assertNotNull(updatedCountry);
        assertNotNull(updatedCountry.getCountryName());
        assertNotNull(updatedCountry.getCountryNameEu());
        assertNotNull(updatedCountry.getCountryCode());
        assertEquals(block.getCountryName(), updatedCountry.getCountryName());
        assertEquals(block.getCountryNameEu(), updatedCountry.getCountryNameEu());
        assertEquals(block.getCountryCode(), updatedCountry.getCountryCode());
    }

    @Test
    void deleteCountry() {
        CountryEntity ce = CountryEntity.builder().countryCode("EU").countryName("Europe").build();
        CountryDto countryDto = AppUtils.entityToDto(ce);

        CountryDto block = countryService.saveCountry(Mono.just(countryDto)).block();
        countryService.deleteCountry(block.getId());
        Mono<CountryDto> countryById = countryService.getCountryById(block.getId());
        StepVerifier.create(countryById)
                .expectError(ClassNotFoundException.class);
    }
}
