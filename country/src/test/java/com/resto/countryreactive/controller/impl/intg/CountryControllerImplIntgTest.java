package com.resto.countryreactive.controller.impl.intg;

import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.repository.CountryRepository;
import com.resto.countryreactive.service.CountryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureDataMongo
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@AutoConfigureWebTestClient
public class CountryControllerImplIntgTest {

    @Autowired
    CountryService countryService;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    WebTestClient webTestClient;

    static String COUNTRY_URL = "/country";
    CountryDto countryDto;
    CountryDto countryDto1;

    @BeforeEach
    void setUp(){
        countryDto = CountryDto.builder()
                .countryId(1)
                .countryName("България")
                .countryNameEu("Bulgaria")
                .countryCode("BG").build();
        countryDto1 = CountryDto.builder()
                .countryId(2)
                .countryName("Гърция")
                .countryNameEu("Greece")
                .countryCode("GR").build();
        CountryDto savedDto = countryService.saveAllCountry(Flux.fromIterable(List.of(this.countryDto, countryDto1)))
                .blockLast();
        countryDto1.setId(savedDto.getId());
    }

    @AfterEach
    void tearDown(){
        countryRepository.deleteAll().block();
    }

    @Test
    void getAllCountries() {

        webTestClient
                .get()
                .uri(COUNTRY_URL + "/all")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(CountryDto.class)
                .hasSize(2);
    }

    @Test
    void getAllCountriesMissingResult() {
        countryRepository.deleteAll().block();
        webTestClient
                .get()
                .uri(COUNTRY_URL + "/all")
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void getCountryById() {

        webTestClient
                .get()
                .uri(COUNTRY_URL + "/" + countryDto1.getId())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(CountryDto.class)
                .hasSize(1);
    }

    @Test
    void getCountryByIdMissingResult() {

        webTestClient
                .get()
                .uri(COUNTRY_URL + "/234")
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void getCountryByCountryCode() {

        webTestClient
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path(COUNTRY_URL + "/country-code")
                        .queryParam("countryCode",countryDto.getCountryCode())
                        .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(CountryDto.class)
                .hasSize(1);
    }

    @Test
    void getCountryByCountryCodeMissingResult() {

        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(COUNTRY_URL + "/country-code")
                        .queryParam("countryCode","DE")
                        .build())
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void saveProduct() {
        var country = CountryDto.builder().countryId(6).countryName("Норвегия").countryNameEu("Norway").countryCode("NR").build();
        CountryDto countryDtoMono = countryService.saveCountry(Mono.just(country)).block();
        webTestClient
                .post()
                .uri(COUNTRY_URL)
                .bodyValue(countryDtoMono)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(CountryDto.class)
                .consumeWith(countryDtoEntityExchangeResult -> {
                    var responseBody = countryDtoEntityExchangeResult.getResponseBody();
                    assert responseBody !=null;
                    assert responseBody.getId() !=null;
                    assertEquals(countryDtoMono.getId(),responseBody.getId());
                });
    }

    @Test
    void updateProduct() {
        var country =  CountryDto.builder()
                .countryId(2)
                .countryName("Гърция1")
                .countryNameEu("Greece1")
                .countryCode("GR").build();
        final String id = countryDto1.getId();
        countryService.updateCountry(Mono.just(country),id).block();
        Mono<CountryDto> countryById = countryService.getCountryById(id);

        webTestClient
                .put()
                .uri(COUNTRY_URL + "/update/{id}",id)
                .bodyValue(countryById.block())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CountryDto.class)
                .consumeWith(countryDtoEntityExchangeResult -> {
                    var responseBody = countryDtoEntityExchangeResult.getResponseBody();
                    assert responseBody !=null;
                    assert responseBody.getId() !=null;
                    assertEquals(country.getCountryName(),responseBody.getCountryName());
                });
    }

    @Test
    void deleteProduct() {
        var id = countryDto1.getId();
        countryService.deleteCountry(id).block();

        webTestClient
                .get()
                .uri(COUNTRY_URL + "/" + id)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }
}
