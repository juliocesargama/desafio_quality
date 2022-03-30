package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.entity.RoomAreaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class RealEstateServiceTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RealEstateService realEstateService;

    /**
     * @author Ana preis
     */
    @Mock
    RealEstate i1 = new RealEstate();
    @InjectMocks
    Room r1 = new Room();
    @InjectMocks
    Room r2 = new Room();

    /**
     * @author Ana preis
     */
    @BeforeEach
    public void setUp() {
        r1.setRoomName("TestRoom");
        r1.setRoomLength(15.0);
        r1.setRoomWidth(10.0);

        r2.setRoomName("TestRoom2");
        r2.setRoomLength(30.0);
        r2.setRoomWidth(30.0);

        List<Room> roomList = new ArrayList<>();
        roomList.add(r1);
        roomList.add(r2);

        i1.setPropName("Imovel");
        i1.setDistrict (new District("Trindade", BigDecimal.valueOf(300)));
        i1.setRooms(roomList);
    }

    /**
     * @author Felipe Myose
     */
    @Test
    public void testGetRoomByNameExistingRoom() {
        String roomName = "TestRoom";
        Room r1 = new Room("TestRoom", 10.0, 15.0);
        Room r2 = new Room("TestRoom2", 30.0, 30.0);
        List<Room> roomList = new ArrayList<>(Arrays.asList(r1,r2));
        District d1 = new District("DistrictName", BigDecimal.valueOf(1000));
        RealEstate i1 = new RealEstate("PropName", d1, roomList);

        Room room = realEstateService.getRoomByName(i1, roomName);

        Assertions.assertEquals(r1, room);
    }

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
     * @author Ana preis
     */
    @Test
    public void getRealEstatePriceTest(){

        Room r1 = new Room("TestRoom", 10.0, 15.0);
        Room r2 = new Room("TestRoom2", 30.0, 30.0);
        List<Room> roomList = new ArrayList<>();
        roomList.add(r1);
        roomList.add(r2);
        RealEstate i1 = new RealEstate("Imovel", new District("Trindade", BigDecimal.valueOf(300)), roomList);

        Mockito.when(roomService.getRoomArea(r1)).thenReturn(150.0);
        Mockito.when(roomService.getRoomArea(r2)).thenReturn(900.0);

        BigDecimal price = realEstateService.getRealEstatePrice(i1);

        Assertions.assertEquals(BigDecimal.valueOf(315000.0) , price);
    }
}
