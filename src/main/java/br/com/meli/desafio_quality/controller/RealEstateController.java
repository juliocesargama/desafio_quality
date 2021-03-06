package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.RealEstate;

import br.com.meli.desafio_quality.entity.ResponseTotalAreaDTO;

import br.com.meli.desafio_quality.entity.RealEstatePriceDTO;

import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.entity.RoomAreaDTO;
import br.com.meli.desafio_quality.exception.MissingRoomException;
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
     * Endpoint para retornar todas as propriedades cadastradas.
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
     * Refatoração para realizar o cálculo da área do cômodo sem chamada do Service de Cômodo.
     */
    @GetMapping("/realestate/{propName}/{roomName}/area")
    public ResponseEntity<RoomAreaDTO> returnRoomArea(@PathVariable String roomName,
                                                 @PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);
        Room room = realEstateService.getRoomByName(realEstate, roomName);
        RoomAreaDTO result = new RoomAreaDTO(room.getRoomName(), roomService.getRoomArea(room));

        return ResponseEntity.ok(result);
    }

    /**
     * @author Felipe Myose
     * retorna a lista de comodos com sua respectiva area de um imovel.
     */
    @GetMapping("/realestate/{propName}/areabyroom")
    public ResponseEntity<List<RoomAreaDTO>> returnAreaByRoom(@PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);
        List<RoomAreaDTO> roomAreas = realEstateService.getAreaByRoom(realEstate);

        return ResponseEntity.ok(roomAreas);
    }

    /**
     * @author Julio Gama, Antonio Hugo Freire
     * Este método retorna a total do imóvel.
     * **/
    @GetMapping("/realestate/{propName}/totalarea")
    public ResponseEntity<?> getRealEstateTotalArea(@PathVariable String propName){

        RealEstate realEstate = realEstateService.findByName(propName);
        Double totalArea = realEstateService.getRealStateTotalArea(realEstate);
        return ResponseEntity.ok(new ResponseTotalAreaDTO(totalArea));

    }

    /**
     * @author Felipe Myose
     * retorna RealEstatePriceDTO, contendo o nome do imóvel e o seu preço.
     */
    @GetMapping("/realestate/{propName}/price")
    public ResponseEntity<RealEstatePriceDTO> getRealEstatePrice(@PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);

        RealEstatePriceDTO realEstatePriceDTO = new RealEstatePriceDTO(propName, realEstateService.getRealEstatePrice(realEstate));

        return ResponseEntity.ok(realEstatePriceDTO);
    }

    /**
     * @author Ana preis
     * criação desse endpoint
     *  Busca o comodo com maior área em m2 artavés do nome do imóvel.
     */
    @GetMapping("/realestate/{propName}/largestroom")
    public ResponseEntity<Room> getLargestRoom(@PathVariable String propName) {

        RealEstate realEstate = realEstateService.findByName(propName);

        return ResponseEntity.ok(roomService.getBiggestRoom(realEstate));
    }

    /**
     * @author Antonio Hugo Freire
     * Este método cria um novo imóvel, em caso de sucesso retonar o código 201 e o imóvel criado.
     * **/
    @PostMapping("/realestate")
    public ResponseEntity<RealEstate> createRealEstate(@Valid @RequestBody RealEstate realEstate) {

        RealEstate realEstateCreated = realEstateService.save(realEstate);

        return new ResponseEntity<>(realEstateCreated, HttpStatus.CREATED);

    }
}