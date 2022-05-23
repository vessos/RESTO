package com.area.areaservice.controller;

import com.area.areaservice.dto.AreaDto;
import com.area.areaservice.dto.CountryDataDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/area")
public interface AreaController {

    @Operation(
            summary = "Get all area from DB",
            description= "Return Area Objects containing all area data."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "All of area was taken successful",
                    content={@Content(mediaType = "application/json",schema=@Schema(implementation = AreaDto.class))}),
                    @ApiResponse(responseCode = "403",description = "Currently authenticated principal is not permitted to update this country data",
                        content = @Content)}
    )
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    Flux<AreaDto> getAllArea();

    @Operation(
            summary = "Get All area by countryId",
            description = "Get all area objects which connected with country object by country Id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "All of area was taken successful",
                            content={@Content(mediaType = "application/json",schema=@Schema(implementation = AreaDto.class))}),
                    @ApiResponse(responseCode = "403",description = "Currently authenticated principal is not permitted to update this country data",
                            content = @Content)}
    )
    @GetMapping("/countryId")
    Mono<CountryDataDto> getAllAreaByCountryId(@RequestParam String countryId);

    @Operation(
            summary = "Get Area By area Id",
            description = "return area object by area Id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "All of area was taken successful",
                            content={@Content(mediaType = "application/json",schema=@Schema(implementation = AreaDto.class))}),
                    @ApiResponse(responseCode = "403",description = "Currently authenticated principal is not permitted to update this country data",
                            content = @Content)}
    )
    @GetMapping("/{id}")
    Mono<AreaDto> getAreaById(@PathVariable String id);

    @Operation(
            summary = "Search Areas By Name",
            description = "return Areas there are same name"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "All of area was taken successful",
                            content={@Content(mediaType = "application/json",schema=@Schema(implementation = AreaDto.class))}),
                    @ApiResponse(responseCode = "403",description = "Currently authenticated principal is not permitted to update this country data",
                            content = @Content)}
    )
    @GetMapping("/name")
    Flux<AreaDto> getAreaByName(@RequestParam String name);

    @Operation(
            summary = "Create new Area",
            description = "Create Only new area"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "All of area was taken successful",
                            content={@Content(mediaType = "application/json",schema=@Schema(implementation = AreaDto.class))}),
                    @ApiResponse(responseCode = "403",description = "Currently authenticated principal is not permitted to update this country data",
                            content = @Content)}
    )
    @PostMapping("/save")
    Mono<AreaDto> saveArea(@RequestBody Mono<AreaDto> areaDtoMono);

    @Operation(
            summary = "Create new Areas and Country if not exist",
            description = "Check and Create Areas and Country if not exists"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "All of area was taken successful",
                            content={@Content(mediaType = "application/json",schema=@Schema(implementation = AreaDto.class))}),
                    @ApiResponse(responseCode = "403",description = "Currently authenticated principal is not permitted to update this country data",
                            content = @Content)}
    )
    @PostMapping("/saveAllData")
    Mono<CountryDataDto> saveAreaAndCountry(@RequestBody Mono<CountryDataDto> countryDataDtoMono);

    @Operation(
            summary = "Update Area record to DB",
            description = "Update an existing Area and overwrite all values in dB "
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Country updated successful ",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AreaDto.class))}),
                    @ApiResponse(responseCode = "403", description = "The currently authenticated principal is not permitted to update this countries data",
                            content = @Content)
            }
    )
    @PutMapping("/update/{id}")
    Mono<AreaDto> updateArea(@RequestBody Mono<AreaDto> areaDto,@PathVariable String id);

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> deleteProduct(@PathVariable String id);

    //TO DO - when implemented validation in controllers should implemented test for them
}
