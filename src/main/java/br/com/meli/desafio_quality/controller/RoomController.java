package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    @GetMapping("/room/area/{name}")
    public ResponseEntity<Double> returnRoomArea(@PathVariable String name) {
        RoomService roomService = new RoomService();
        Room room = roomService.getRoomByName(name);
        return ResponseEntity.ok(roomService.getRoomArea(room));
    }
}
