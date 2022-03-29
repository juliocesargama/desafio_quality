package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.service.RealEstateService;
import br.com.meli.desafio_quality.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    @Autowired
    RealEstateService realEstateService;
    RoomService roomService;

    @GetMapping("realestate/{propName}/{roomName}/area")
    public ResponseEntity<Double> returnRoomArea(@PathVariable String roomName,
                                                 @PathVariable String propName) {

        RealEstate realEstate = realEstateService.getRealEstate(propName);
        Room room = realEstate.getRooms().stream()
                                         .map(r -> roomService.getRoomByName(propName))
                                         .findFirst()
                                         .get();

        return ResponseEntity.ok(roomService.getRoomArea(room));
    }
}
