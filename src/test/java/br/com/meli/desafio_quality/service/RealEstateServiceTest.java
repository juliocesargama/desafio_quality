package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.entity.RoomAreaDTO;
import br.com.meli.desafio_quality.repository.RealEstateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
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
    RealEstate i1 = new RealEstate();
    Room r1 = new Room();
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
        Room room = realEstateService.getRoomByName(i1, roomName);

        Assertions.assertEquals(r1, room);
    }

    /**
     * @author Ana preis
     */
    @Test
    public void getAreaByRoomTest(){
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
        Mockito.when(roomService.getRoomArea(r1)).thenReturn(150.0);
        Mockito.when(roomService.getRoomArea(r2)).thenReturn(900.0);

        BigDecimal price = realEstateService.getRealEstatePrice(i1);

        Assertions.assertEquals(BigDecimal.valueOf(315000.0) , price);
    }

    /**
     * @author Marcelo Leite
     */

    @Test
    public void getAllRealEstateTest() {

        List<RealEstate> realEstateList = new ArrayList<>();
        realEstateList.add(i1);

        Mockito.when(realEstateRepository.findAll()).thenReturn(realEstateList);

        Assertions.assertEquals(realEstateList, realEstateService.getAll());

    }

    /**
     * @author Marcelo Leite
     */

    @Test
    public void getAllRealEstateEmptyTest() {
        //Caso de retornar uma lista vazia
        List<RealEstate> realEstateList = new ArrayList<>();

        Mockito.when(realEstateRepository.findAll()).thenReturn(realEstateList);

        Assertions.assertEquals(realEstateList, realEstateService.getAll());
    }

    /**
     * @author Antonio Hugo Freire
     */

    @Test
    public void shouldBeAbleToFindARealEstateByName() {
        RealEstate mockRealEstate = new RealEstate("Casa", new District("Jardim 1", BigDecimal.valueOf(500.0)), List.of(new Room(
                "sala", 15.0, 10.0
        )));

        Mockito.when(realEstateRepository.findByName(ArgumentMatchers.any())).thenReturn(mockRealEstate);

        RealEstate result = realEstateService.findByName("Casa");

        Assertions.assertEquals(mockRealEstate, result);
    }

    /**
     * @author Antonio Hugo Freire
     */

    @Test
    public void shouldBeAbleToCreateARealEstate() {
        RealEstate mockRealEstate = new RealEstate("Casa", new District("Jardim 1", BigDecimal.valueOf(500.0)), List.of(new Room(
                "sala", 15.0, 10.0
        )));

        Mockito.when(realEstateRepository.save(ArgumentMatchers.any())).thenReturn(mockRealEstate);

        RealEstate result = this.realEstateService.save(mockRealEstate);

        assertThat(result).usingRecursiveComparison().isEqualTo(mockRealEstate);
    }
}
