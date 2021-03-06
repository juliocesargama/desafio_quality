package br.com.meli.desafio_quality.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Felipe Myose
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomAreaDTO {
    private String roomName;
    private Double roomArea;
}
