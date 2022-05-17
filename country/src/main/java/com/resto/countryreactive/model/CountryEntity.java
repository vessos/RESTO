package com.resto.countryreactive.model;

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
@Document(collection = "country")
public class CountryEntity {

    @Id
    private String id;
    private Integer countryId;
    private String countryName;
    private String countryNameEu;
    private String countryCode;
}
