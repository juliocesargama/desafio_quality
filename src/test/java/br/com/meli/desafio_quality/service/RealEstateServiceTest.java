package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.entity.RoomAreaDTO;
import br.com.meli.desafio_quality.exception.MissingRealEstateException;
import br.com.meli.desafio_quality.exception.MissingRoomException;
import br.com.meli.desafio_quality.exception.RealEstateAlreadyExistsException;
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
     * Objetos utilizados em cada teste unitário.
     */
    RealEstate i1 = new RealEstate();
    Room r1 = new Room();
    Room r2 = new Room();
    
    /**
     * @author Ana preis
     *  Setando os objetos antes de cada teste unitário.
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
     * Teste unitário para verificar método realEstateService.getRoomByName
     */
    @Test
    public void testGetRoomByNameExistingRoom() {
        String roomName = "TestRoom";
        Room room = realEstateService.getRoomByName(i1, roomName);

        Assertions.assertEquals(r1, room);
    }

    /**
     * @author Ana preis
     *  Testa se a área dos comodos estão retornando corretas.
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
     *  Testa se, quando a área dos comodos não estão corretas, retorna a exceção.
     */
    @Test
    public void shouldNotGetAreaByRoomTest(){
        String message = "Área do comodo não encontrada";
        Mockito.when(roomService.getRoomArea(r1)).thenThrow(new MissingRoomException("Área do comodo não encontrada"));

        RuntimeException exception =  Assertions.assertThrows(MissingRoomException.class, () -> roomService.getRoomArea(r1));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * @author Ana preis
     *  Testa se o método getRealEstatePrice retorna o valor correto.
     */
    @Test
    public void getRealEstatePriceTest(){
        Mockito.when(roomService.getRoomArea(r1)).thenReturn(150.0);
        Mockito.when(roomService.getRoomArea(r2)).thenReturn(900.0);

        BigDecimal price = realEstateService.getRealEstatePrice(i1);

        Assertions.assertEquals(BigDecimal.valueOf(315000.0) , price);
    }

    /**
     * @author Ana preis
     *  Testa se, quando o método getRealEstatePrice não retorna o valor correto, retorna a exceção.
     */
    @Test
    public void shouldNotGetRealEstatePriceTest(){
        String message = "Imóvel não encontrado";
        Mockito.when(realEstateService.getRealEstatePrice(i1)).thenThrow(new MissingRealEstateException("Imóvel não encontrado"));
        RuntimeException exception =  Assertions.assertThrows(MissingRealEstateException.class, () -> realEstateService.getRealEstatePrice(i1));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * @author Marcelo Leite
     * teste unitario do metodo que retorna todos os imoveis caso de sucesso.
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
     * teste unitario do metodo que retonar todos os imoveis, caso de não ter imovel cadastrado, retorna vazio.
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

        RealEstate result = realEstateService.save(mockRealEstate);

        assertThat(result).usingRecursiveComparison().isEqualTo(mockRealEstate);
    }

    /**
     * @author Antonio Hugo Freire
     */

    @Test
    public void shouldNotBeAbleToFindRealEstateIfDoesntExist() {
        String message = "Imóvel não encontrado";
        Mockito.when(realEstateRepository.findByName("Not_exists")).thenThrow( new MissingRealEstateException("Imóvel não encontrado"));

        RuntimeException exception =  Assertions.assertThrows(MissingRealEstateException.class, () -> realEstateService.findByName("Not_exists"));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * @author Julio Gama
     */
    @Test
    public void shouldReturnRealStateTotalArea(){

        Mockito.when(roomService.getRoomArea(r1)).thenReturn(150.0);
        Mockito.when(roomService.getRoomArea(r2)).thenReturn(900.0);

        Assertions.assertEquals(1050.0, realEstateService.getRealStateTotalArea(i1));

    }
    /**
     * @author Julio Gama
     */
    @Test
    public void shouldNotReturnRealStateTotalArea(){

        RealEstate realEstate = new RealEstate();
        realEstate.setRooms(new ArrayList<>());

        Assertions.assertThrows(MissingRoomException.class, () -> realEstateService.getRealStateTotalArea(realEstate));
    }

    /**
     * @author Marcelo Leite
     * teste unitario se existe o distrito.
     */
    @Test
    public void shouldDistrictTest() {
        List<RealEstate> realEstateList = new ArrayList<>();
        realEstateList.add(i1);

        Mockito.when(realEstateRepository.findAll()).thenReturn(realEstateList);

        Assertions.assertEquals(realEstateList.get(0).getDistrict(), realEstateService.getAll().get(0).getDistrict());
    }

    /**
     * @author Antonio Hugo Freire
     */
    @Test
    public void shouldNotBeAbleToGetAreaByRoom() {
        RealEstate mockRealEstate = new RealEstate("Casa", new District("Jardim 1", BigDecimal.valueOf(500.0)), new ArrayList<>());
        String message = "Cômodos não foram encontrados.";

        RuntimeException exception = Assertions.assertThrows(MissingRoomException.class, () -> realEstateService.getAreaByRoom(mockRealEstate));
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * @author Antonio Hugo Freire
     */

    @Test
    public void shouldNotBeAbleAlreadyExistsRealEstate() {

        List<RealEstate> mockRealEstateList = List.of(i1);

        Mockito.when(realEstateRepository.findAll()).thenReturn(mockRealEstateList);

        Assertions.assertThrows(RealEstateAlreadyExistsException.class, () -> realEstateService.save(i1));
    }

}
