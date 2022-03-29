package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.RealEstate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealEstateService {

    @Autowired
    RoomService roomService;

    public Double getRealStateTotalArea(RealEstate realEstate){

        return realEstate.getRooms().stream().mapToDouble(room -> roomService.getRoomArea(room)).sum();

    }
}
