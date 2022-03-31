package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

/**
 * @author Ana preis, Antonio Hugo
 */
@Service
public class RoomService {

    /**
     * @author Ana preis
     *  Retorna a área total do comodo em m2.
     */
    public Double getRoomArea(Room room) {
        return room.getRoomWidth() * room.getRoomLength();
    }

    /**
     * @author Antonio Hugo
     * Este método retorna o maior comodo do imóvel
     */
    public Room getBiggestRoom(RealEstate realEstate) {
        List<Room> rooms = realEstate.getRooms();
        if(rooms == null || rooms.size() == 0){
            return null;
        }

        rooms.sort(Comparator.comparing(room -> getRoomArea(room).shortValue()));

        return rooms.get(rooms.size() - 1);
    }
}
