package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.RealEstate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RealEstateService {

    @Autowired
    RoomService roomService;

    List<RealEstate> realEstates = new ArrayList<RealEstate>();


    public List<RealEstate> getAll() {
        return realEstates;
    }

    public Double getRealStateTotalArea(RealEstate realEstate){

        return realEstate.getRooms().stream().mapToDouble(room -> roomService.getRoomArea(room)).sum();

    }

    public RealEstate getRealEstate(String propName) {

        return realEstates.stream()
                .filter(realEstate -> realEstate.getPropName().equals(propName))
                .findFirst()
                .get();
    }

}
