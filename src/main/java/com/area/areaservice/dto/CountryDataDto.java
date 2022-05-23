package com.area.areaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDataDto {
    private CountryDto countryDto;
    private List<AreaDto> areaList;
}
