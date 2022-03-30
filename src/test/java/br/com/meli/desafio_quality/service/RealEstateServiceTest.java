package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.entity.RoomAreaDTO;
import br.com.meli.desafio_quality.repository.RealEstateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RealEstateServiceTest {

    @Mock
    private RoomService roomService;

    @Mock
    private RealEstateRepository realEstateRepository;

    @InjectMocks
    private RealEstateService realEstateService;

    /**
     * @author Ana preis
     */
    @Test
    public void getAreaByRoomTest(){

        Room r1 = new Room("TestRoom", 10.0, 15.0);
        Room r2 = new Room("TestRoom2", 30.0, 30.0);
        List<Room> roomList = new ArrayList<>();
        roomList.add(r1);
        roomList.add(r2);
        RealEstate i1 = new RealEstate("Imovel", new District("Trindade", BigDecimal.valueOf(300)), roomList);

        Mockito.when(roomService.getRoomArea(r1)).thenReturn(150.0);
        Mockito.when(roomService.getRoomArea(r2)).thenReturn(900.0);

        List<RoomAreaDTO> areaList = realEstateService.getAreaByRoom(i1);
        RoomAreaDTO dto1 = new RoomAreaDTO("TestRoom", 150.0);
        RoomAreaDTO dto2 = new RoomAreaDTO("TestRoom2", 900.0);

        Assertions.assertEquals(dto1.getRoomArea(), areaList.get(0).getRoomArea());
        Assertions.assertEquals(dto2.getRoomArea(), areaList.get(1).getRoomArea());

    }

    /**
     * @author Marcelo Leite
     */

    @Test
    public void getAllRealEstateTest() {
        Room r1 = new Room("TestRoom", 10.0, 15.0);
        Room r2 = new Room("TestRoom2", 30.0, 30.0);
        List<Room> roomList = new ArrayList<>();
        roomList.add(r1);
        roomList.add(r2);

        List<RealEstate> realEstateList = new ArrayList<>();
        RealEstate realEstate = new RealEstate("Casa", new District("Melicidade", BigDecimal.valueOf(500)), roomList);
        realEstateList.add(realEstate);

        Mockito.when(realEstateRepository.findAll()).thenReturn(realEstateList);

        Assertions.assertEquals(realEstateList, realEstateService.getAll());

    }

    @Test
    public void getAllRealEstateEmptyTest() {
        //Caso de retornar uma lista vazia
        List<RealEstate> realEstateList = new ArrayList<>();

        Mockito.when(realEstateRepository.findAll()).thenReturn(realEstateList);

        Assertions.assertEquals(realEstateList.isEmpty(), realEstateService.getAll());
    }
}
