package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.entity.RoomAreaDTO;
import br.com.meli.desafio_quality.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RealEstateService {

    @Autowired
    RoomService roomService;

    @Autowired
    RealEstateRepository realEstateRepository;

    public List<RealEstate> getAll() {
        return realEstateRepository.findAll();
    }

    public Double getRealStateTotalArea(RealEstate realEstate){

        return realEstate.getRooms().stream().mapToDouble(room -> roomService.getRoomArea(room)).sum();

    }

    public List<RoomAreaDTO> getAreaByRoom(RealEstate realEstate) {
        return realEstate.getRooms().stream().map(room -> new RoomAreaDTO(room.getRoomName(), roomService.getRoomArea(room)))
                .collect(Collectors.toList());
    }

    public BigDecimal getRealEstatePrice(RealEstate realEstate) {
        return BigDecimal.valueOf(getRealStateTotalArea(realEstate)).multiply(realEstate.getDistrict().getValueDistrictM2());
    }

    public RealEstate save(RealEstate realEstate) {
        return realEstateRepository.save(realEstate);
    }

    public RealEstate findByName(String name) {
        return realEstateRepository.findByName(name);
    }

    public Room getRoomByName(RealEstate realEstate, String roomName) {
        Room roomByName = realEstate.getRooms().stream().filter(room -> room.getRoomName().equals(roomName)).findFirst().orElse(null);

        return roomByName;
    }
}
