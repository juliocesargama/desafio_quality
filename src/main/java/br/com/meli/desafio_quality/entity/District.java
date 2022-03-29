package br.com.meli.desafio_quality.entity;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class District {

    private String propDistrict;
    private BigDecimal valueDistrictM2;
    
}
