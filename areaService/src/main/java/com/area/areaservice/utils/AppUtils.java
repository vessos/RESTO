package com.area.areaservice.utils;

import com.area.areaservice.dto.AreaDto;
import com.area.areaservice.model.AreaEntity;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static AreaDto entityToDto(AreaEntity areaEntity){
        AreaDto areaDto = new AreaDto();
        BeanUtils.copyProperties(areaEntity,areaDto);
        return areaDto;
    }

    public static AreaEntity DtoToEntity(AreaDto areaDto){
        AreaEntity areaEntity = new AreaEntity();
        BeanUtils.copyProperties(areaDto,areaEntity);
        return areaEntity;
    }
}
