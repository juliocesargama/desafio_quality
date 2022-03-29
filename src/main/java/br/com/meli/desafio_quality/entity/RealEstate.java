package br.com.meli.desafio_quality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstate {

  private String propName;
  private District district;
  private List<Room> rooms;
  
}
