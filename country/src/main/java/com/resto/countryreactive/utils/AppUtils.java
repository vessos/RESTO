package com.resto.countryreactive.utils;

import com.resto.countryreactive.dto.CountryDto;
import com.resto.countryreactive.model.CountryEntity;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static CountryDto entityToDto(CountryEntity country){
        CountryDto countryDto = new CountryDto();
        BeanUtils.copyProperties(country,countryDto);
        return countryDto;
    }

    public static CountryEntity dtoToEntity(CountryDto countryDto){
        CountryEntity countryEntity = new CountryEntity();
        BeanUtils.copyProperties(countryDto,countryEntity);
        return countryEntity;
    }
}
