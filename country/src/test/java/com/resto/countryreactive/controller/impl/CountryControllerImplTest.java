package com.resto.countryreactive.controller.impl;

import com.resto.countryreactive.controller.CountryController;
import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.exceptionHandler.CustomerNotFoundException;
import com.resto.countryreactive.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;


@WebFluxTest(controllers = CountryController.class, excludeAutoConfiguration = ReactiveSecurityAutoConfiguration.class)
@AutoConfigureWebTestClient
class CountryControllerImplTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private CountryService countryService;

    static String COUNTRY_URL = "/country";


    @Test
    void getAllCountries() {
        var counties = List.of(CountryDto.builder().countryId(1).countryName("България").countryNameEu("Bulgaria").countryCode("BG").build(),
                CountryDto.builder().countryId(2).countryName("Швеция").countryNameEu("Sweden").countryCode("SW").build());
        when(countryService.getAllCountry()).thenReturn(Flux.fromIterable(counties));

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

        when(countryService.getAllCountry()).thenReturn(Flux.error(new CustomerNotFoundException("In DB missing any records ")));

        webTestClient
                .get()
                .uri(COUNTRY_URL + "/all")
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void getCountryById() {
        var country = CountryDto.builder().countryId(1).countryName("България").countryNameEu("Bulgaria").countryCode("BG").build();
        when(countryService.getCountryById(anyString())).thenReturn(Mono.just(country));

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

        when(countryService.getCountryById(anyString())).thenReturn(Mono.error(new CustomerNotFoundException("In DB missing any records ")));

        webTestClient
                .get()
                .uri(COUNTRY_URL + "/234")
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void getCountryByCountryCode() {
        var counties = CountryDto.builder().countryId(1).countryName("България").countryNameEu("Bulgaria").countryCode("SW").build();
        when(countryService.getCountryByCountryCode(anyString())).thenReturn(Mono.just(counties));

        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(COUNTRY_URL + "/country-code")
                        .queryParam("countryCode", "DE")
                        .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(CountryDto.class)
                .hasSize(1);
    }

    @Test
    void getCountryByCountryCodeMissingResult() {

        when(countryService.getCountryByCountryCode(anyString())).thenReturn(Mono.error(new CustomerNotFoundException("In DB missing any records ")));

        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(COUNTRY_URL + "/country-code")
                        .queryParam("countryCode", "DE")
                        .build())
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void saveProduct() {
        var country = CountryDto.builder().countryId(1).countryName("България").countryNameEu("Bulgaria").countryCode("SW").build();
        Mono<CountryDto> just = Mono.just(CountryDto.builder().id("1234f65ht").countryId(1).countryName("България").countryNameEu("Bulgaria").countryCode("SW").build());
        when(countryService.saveCountry(isA(Mono.class))).thenReturn(just);

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
                    assert responseBody != null;
                    assert responseBody.getId() != null;
                    assertEquals("1234f65ht", responseBody.getId());
                });
    }

    @Test
    void updateProduct() {
        var country = CountryDto.builder().countryId(1).countryName("България").countryNameEu("Bulgaria").countryCode("SW").build();
        Mono<CountryDto> just = Mono.just(CountryDto.builder().id("1234f65ht").countryId(1).countryName("България1").countryNameEu("Bulgaria").countryCode("SW").build());
        final String id = just.block().getId();

        when(countryService.updateCountry(isA(Mono.class), isA(String.class))).thenReturn(just);

        webTestClient
                .put()
                .uri(COUNTRY_URL + "/update/{id}", id)
                .bodyValue(country)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CountryDto.class)
                .consumeWith(countryDtoEntityExchangeResult -> {
                    var responseBody = countryDtoEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assert responseBody.getId() != null;
                    assertEquals("България1", responseBody.getCountryName());
                });
    }

    @Test
    void deleteProduct() {
        var id = "123";
        when(countryService.deleteCountry(anyString())).thenReturn(Mono.empty());
        webTestClient
                .delete()
                .uri(COUNTRY_URL + "/delete/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}