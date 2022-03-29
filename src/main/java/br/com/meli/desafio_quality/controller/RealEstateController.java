package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealEstateController {

    @PostMapping("/realEstate")
    public ResponseEntity<Room> returnBiggestRoom(@RequestBody RealEstate teste) {

        RoomService roomService = new RoomService();

        Room biggestRoom = roomService.getBiggestRoom(teste);

        return ResponseEntity.ok(biggestRoom);
    }
}
