package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.Map;

@Service
public class RoomService {

    public Double getRoomArea(Room room) {
        return room.getRoomWidth() * room.getRoomLength();
    }

    public Room getBiggestRoom(RealEstate realEstate) {
        List<Room> rooms = realEstate.getRooms();
        rooms.sort(Comparator.comparing(room -> getRoomArea(room).shortValue()));

        return rooms.get(rooms.size() - 1);
    }
}
