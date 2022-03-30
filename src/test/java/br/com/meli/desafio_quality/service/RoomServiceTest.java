package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.service.RoomService;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;

public class RoomServiceTest {

    @Mock
    private RoomService roomService;


    @Test
    private void getBiggestRoomDeveEstarOk(){
        District bairroSilva = new District("Bairro silva ", new BigDecimal(1000.00));
        new Room("Sala", 15.0, 10.0);
        new Room("Cozinha", 1.0, 10.0);

//        new RealEstate("Jose Alfredo",  bairroSilva,  ),
//        roomService.getBiggestRoom(sala);
    }
}
