package br.com.meli.desafio_quality.service.tests;

import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.service.RoomService;
import org.junit.Test;
import org.mockito.Mock;

public class RoomServiceTest {

    @Mock
    private RoomService roomService;


    @Test
    private void getBiggestRoomDeveEstarOk(){
        new Room("Sala", 20.0, 25.0);
//        roomService.getBiggestRoom();
    }
}
