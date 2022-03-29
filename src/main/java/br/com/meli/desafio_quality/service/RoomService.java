package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.Room;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    List<Room> roomList = new ArrayList<>();

    public Double getRoomArea(Room room) {
        return room.getRoomWidth() * room.getRoomLength();
    }

    public Room getRoomByName(String name) {
        return roomList.stream()
                .filter(a -> a.getRoomName().equals(name))
                .findFirst()
                .get();
    }


}
