package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.RealEstate;

import br.com.meli.desafio_quality.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class RealEstateController {

    @Autowired
    RealEstateService realEstateService;

    @GetMapping("/realestate/all")
    public ResponseEntity<List<RealEstate>> getAllRealEstates(){
        return ResponseEntity.ok(realEstateService.getAll());
    }

    @GetMapping("/realestate/{name}")
    public ResponseEntity<RealEstate> getRealEstateByName(@PathVariable String name){
        return ResponseEntity.ok(realEstateService.findByName(name));
    }

    @GetMapping("/realestate/{name}/totalarea")
    public ResponseEntity<Double> getRealEstateTotalArea(@PathVariable String name){

        RealEstate realEstate = realEstateService.findByName(name);

        return ResponseEntity.ok(realEstateService.getRealStateTotalArea(realEstate));

    }

    @PostMapping("/realestate")
    public ResponseEntity<RealEstate> createRealEstate(@RequestBody RealEstate realEstate) {

        RealEstate realEstateCreated = realEstateService.save(realEstate);

        return new ResponseEntity<>(realEstateCreated, HttpStatus.CREATED);

    }
}
