package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<RealEstate> getRealEstateByName(@PathVariable String propName){
        return ResponseEntity.ok(realEstateService.getRealEstate(propName));
    }

    @GetMapping("/realestate/{propName}/totalarea")
    public ResponseEntity<Double> getRealEstateTotalArea(@PathVariable String propName){

        RealEstate realEstate = realEstateService.getRealEstate(propName);

        return ResponseEntity.ok(realEstateService.getRealStateTotalArea(realEstate));
    }
}
