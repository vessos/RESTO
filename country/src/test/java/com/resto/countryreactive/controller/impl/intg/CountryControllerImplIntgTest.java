package com.resto.countryreactive.controller.impl.intg;

import com.resto.countryreactive.controller.CountryController;
import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.repository.CountryRepository;
import com.resto.countryreactive.service.CountryService;
import com.resto.countryreactive.service.CountryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
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
        countryService.saveAllCountry(Flux.fromIterable(List.of(countryDto,countryDto1)))
        .blockLast();
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
                .uri(COUNTRY_URL + "/1234")
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
                        .queryParam("countryCode","DE")
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
        var country = CountryDto.builder().countryId(1).countryName("България").countryNameEu("Bulgaria").countryCode("SW").build();

        webTestClient
                .post()
                .uri(COUNTRY_URL)
                .bodyValue(country)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(CountryDto.class)
                .consumeWith(countryDtoEntityExchangeResult -> {
                    var responseBody = countryDtoEntityExchangeResult.getResponseBody();
                    assert responseBody !=null;
                    assert responseBody.getId() !=null;
                    assertEquals("1234f65ht",responseBody.getId());
                });
    }

    @Test
    void updateProduct() {
        var country = CountryDto.builder().countryId(1).countryName("България").countryNameEu("Bulgaria").countryCode("SW").build();
        final String id = "";

        webTestClient
                .put()
                .uri(COUNTRY_URL + "/update/{id}",id)
                .bodyValue(country)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CountryDto.class)
                .consumeWith(countryDtoEntityExchangeResult -> {
                    var responseBody = countryDtoEntityExchangeResult.getResponseBody();
                    assert responseBody !=null;
                    assert responseBody.getId() !=null;
                    assertEquals("България1",responseBody.getCountryName());
                });
    }

    @Test
    void deleteProduct() {
        var id = "123";
        webTestClient
                .delete()
                .uri(COUNTRY_URL +"/delete/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}
