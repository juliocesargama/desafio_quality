package br.com.meli.desafio_quality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstate {
  
  @NotBlank(message = "O campo propName não pode ser vazio")
  @Pattern(regexp = "[A-Z][a-záàâãéèêíïóôõöúçñ]+", message = "O campo propName deve começar com uma letra maiúscula.")
  @Size(min = 3, max = 30, message = "O campo propName deve ter entre 3 e 30 caracteres")
  private String propName;
}
