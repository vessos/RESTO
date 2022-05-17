package com.resto.countryreactive.controller;

import com.resto.countryreactive.dto.CountryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/country")
public interface CountryController {

    @Operation(
            summary="Get all countries from DB",
            description="Return Country objects containing all country data."
    )
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200",description = "All of countries was taken successful ",
                    content = {@Content(mediaType = "application/json",schema = @Schema(implementation = CountryDto.class))}),
                    @ApiResponse(responseCode = "403",description = "The currently authenticated principal is not permitted to update this countries data",
                            content = @Content)}

    )
    @GetMapping("/all")
    Flux<CountryDto> getCountries();

    @Operation(
            summary = "Get country by country Id",
            description = "Return CountryDto object by specific Id with all country data"
    )
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200",description = "Country by id was taken successful ",
                            content = {@Content(mediaType = "application/json",schema = @Schema(implementation = CountryDto.class))}),
                    @ApiResponse(responseCode = "403",description = "The currently authenticated principal is not permitted to update this countries data",
                            content = @Content)}

    )
    @GetMapping("/{id}")
    Mono<CountryDto> getCountryById(@PathVariable String id);

    @Operation(
            summary = "Get country by country code",
            description = "Return CountryDto object by country code with all country data"
    )
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200",description = "Country by country code was taken successful ",
                            content = {@Content(mediaType = "application/json",schema = @Schema(implementation = CountryDto.class))}),
                    @ApiResponse(responseCode = "403",description = "The currently authenticated principal is not permitted to update this countries data",
                            content = @Content)})
    @GetMapping("/country-code")
    Mono<CountryDto> getCountryByCountryCode(@RequestParam String countryCode);

    @Operation(
            summary = "Create new Country record to DB",
            description = "Fill above schema and create new customer"
    )
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200",description = "Country was created successful ",
                            content = {@Content(mediaType = "application/json",schema = @Schema(implementation = CountryDto.class))}),
                    @ApiResponse(responseCode = "403",description = "The currently authenticated principal is not permitted to update this countries data",
                            content = @Content)
            }
    )
    @PostMapping
    Mono<CountryDto> saveProduct(@RequestBody Mono<CountryDto> countryDtoMono);

    @Operation(
            summary = "Update Country record to DB",
            description = "Update an existing country and overwrite all values in dB "
    )
    @ApiResponses(
            value={
                    @ApiResponse(responseCode = "200",description = "Country updated successful ",
                            content = {@Content(mediaType = "application/json",schema = @Schema(implementation = CountryDto.class))}),
                    @ApiResponse(responseCode = "403",description = "The currently authenticated principal is not permitted to update this countries data",
                            content = @Content)
            }
    )
    @PutMapping("/update/{id}")
    Mono<CountryDto> updateProduct(@RequestBody Mono<CountryDto> countryDtoMono,@PathVariable String id);

    @DeleteMapping("/delete/{id}")
    Mono<Void> deleteProduct(@PathVariable String id);
}
