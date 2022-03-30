package br.com.meli.desafio_quality.entity;

import lombok.*;

import java.math.BigDecimal;
import javax.validation.constraints.*;

/**
 * @author Ana preis
 * criação da entidade real estate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class District {

    @NotNull(message = "O bairro é obrigatório")
    @NotEmpty(message = "O bairro não pode estar vazio.")
    @Size(max = 45, message = "O comprimento do nome do assunto não pode exceder 45 caracteres.")
    private String propDistrict;

    @NotNull(message = "O valor do metro quadrado do bairro é obrigatório")
    @NotEmpty(message = "O valor do metro quadrado no bairro não pode estar vazio")
    private BigDecimal valueDistrictM2;
}
