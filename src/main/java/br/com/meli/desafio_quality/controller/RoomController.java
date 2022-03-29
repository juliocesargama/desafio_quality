package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/room/area")
    public ResponseEntity<Double> returnRoomArea(@RequestParam String nameRoom,
                                                 @RequestParam String nameRealEstate) {
        Room room = roomService.getRoomByName(nameRoom);
        return ResponseEntity.ok(roomService.getRoomArea(room));
    }
}
