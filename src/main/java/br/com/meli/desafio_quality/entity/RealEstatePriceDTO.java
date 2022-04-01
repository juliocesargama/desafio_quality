package br.com.meli.desafio_quality.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RealEstatePriceDTO {
    private String propName;
    private BigDecimal price;

    public RealEstatePriceDTO(String propName, BigDecimal price) {
        this.propName = propName;
        this.price = price.setScale(2); // apenas 2 casas decimais
    }
}
