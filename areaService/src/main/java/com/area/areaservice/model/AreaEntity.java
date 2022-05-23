package com.area.areaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "area")
public class AreaEntity {

    @Id
    private String id;
    private String countryId;
    private String areaName;
    private String areaNameEN;
}
