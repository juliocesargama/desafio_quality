package br.com.meli.desafio_quality.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    private String roomName;
    private Double roomWidth;
    private Double roomLength;


}
