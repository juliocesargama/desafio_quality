package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.RealEstate;

import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.entity.RoomAreaDTO;
import br.com.meli.desafio_quality.service.RealEstateService;
import br.com.meli.desafio_quality.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;

@RestController
public class RealEstateController {

    @Autowired
    RealEstateService realEstateService;

    @Autowired
    RoomService roomService;

    @GetMapping("/realestate/all")
    public ResponseEntity<List<RealEstate>> getAllRealEstates(){
        return ResponseEntity.ok(realEstateService.getAll());
    }

    @GetMapping("/realestate/{name}")
    public ResponseEntity<RealEstate> getRealEstateByName(@PathVariable String name){
        return ResponseEntity.ok(realEstateService.findByName(name));
    }

    @GetMapping("/realestate/{propName}/{roomName}/area")
    public ResponseEntity<Double> returnRoomArea(@PathVariable String roomName,
                                                 @PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);
        Room room = realEstateService.getRoomByName(realEstate, roomName);

        return ResponseEntity.ok(roomService.getRoomArea(room));
    }

    @GetMapping("/realestate/{propName}/areabyroom")
    public ResponseEntity<List<RoomAreaDTO>> returAreaByRoom(@PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);
        List<RoomAreaDTO> roomAreas = realEstateService.getAreaByRoom(realEstate);

        return ResponseEntity.ok(roomAreas);
    }

    @GetMapping("/realestate/{propName}/totalarea")
    public ResponseEntity<Double> getRealEstateTotalArea(@PathVariable String propName){

        RealEstate realEstate = realEstateService.findByName(propName);

        return ResponseEntity.ok(realEstateService.getRealStateTotalArea(realEstate));

    }

    @GetMapping("/realestate/{propName}/price")
    public ResponseEntity<BigDecimal> getRealEstatePrice(@PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);

        return ResponseEntity.ok(realEstateService.getRealEstatePrice(realEstate));
    }

    @PostMapping("/realestate")
    public ResponseEntity<RealEstate> returnBiggestRoom(@RequestBody RealEstate realEstate) {

        RealEstate realEstateCreated = realEstateService.save(realEstate);

        return new ResponseEntity<>(realEstateCreated, HttpStatus.CREATED);

    }
}
