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

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class RealEstateController {

    @Autowired
    RealEstateService realEstateService;

    @Autowired
    RoomService roomService;

    /**
     * @author Julio Gama
     */
    @GetMapping("/realestate/all")
    public ResponseEntity<List<RealEstate>> getAllRealEstates(){
        return ResponseEntity.ok(realEstateService.getAll());
    }

    /**
     * @author Julio Gama,     Antonio Hugo Freire
     * criação desse endpoint, refatoração
     */
    @GetMapping("/realestate/{name}")
    public ResponseEntity<RealEstate> getRealEstateByName(@PathVariable String name){
        return ResponseEntity.ok(realEstateService.findByName(name));
    }

    /**
     * @author Ana preis,      Julio Gama
     * criação desse endpoint, refatoração
     */
    @GetMapping("/realestate/{propName}/{roomName}/area")
    public ResponseEntity<Double> returnRoomArea(@PathVariable String roomName,
                                                 @PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);
        Room room = realEstateService.getRoomByName(realEstate, roomName);

        if(room == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(roomService.getRoomArea(room));
    }

    /**
     * @author Felipe Myose
     */
    @GetMapping("/realestate/{propName}/areabyroom")
    public ResponseEntity<List<RoomAreaDTO>> returnAreaByRoom(@PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);
        List<RoomAreaDTO> roomAreas = realEstateService.getAreaByRoom(realEstate);

        return ResponseEntity.ok(roomAreas);
    }

    /**
     * @author Julio Gama, Antonio Hugo Freire
     * **/
    @GetMapping("/realestate/{propName}/totalarea")
    public ResponseEntity<Double> getRealEstateTotalArea(@PathVariable String propName){

        RealEstate realEstate = realEstateService.findByName(propName);

        return ResponseEntity.ok(realEstateService.getRealStateTotalArea(realEstate));

    }

    /**
     * @author Felipe Myose
     */
    @GetMapping("/realestate/{propName}/price")
    public ResponseEntity<BigDecimal> getRealEstatePrice(@PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);

        return ResponseEntity.ok(realEstateService.getRealEstatePrice(realEstate));
    }

    /**
     * @author Ana preis
     * criação desse endpoint
     */
    @GetMapping("/realestate/{propName}/largestroom")
    public ResponseEntity<Room> getLargestRoom(@PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);

        return ResponseEntity.ok(roomService.getBiggestRoom(realEstate));
    }

    /**
     * @author Antonio Hugo Freire
     * **/
    @PostMapping("/realestate")
    public ResponseEntity<RealEstate> createRealEstate(@Valid @RequestBody RealEstate realEstate) {

        RealEstate realEstateCreated = realEstateService.save(realEstate);

        return new ResponseEntity<>(realEstateCreated, HttpStatus.CREATED);

    }
}