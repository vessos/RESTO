package com.resto.countryreactive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryDto {

    private String id;
    private Integer countryId;
    private String countryName;
    private String countryNameEu;
    private String countryCode;
}
