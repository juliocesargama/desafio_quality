package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.exception.MissingRealEstateException;
import br.com.meli.desafio_quality.exception.MissingRoomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Test
    public void getBiggestRoomDeveRetornarMaiorRoom(){
        District bairroSilva = new District("Bairro silva ", new BigDecimal(1000.00));
        Room cozinha = new Room("Cozinha", 20.0, 20.0);
        Room quarto = new Room("Quarto", 10.0, 8.0);
        Room sala = new Room("Sala", 15.0, 10.0);
        List<Room> rooms = Arrays.asList(sala, cozinha, quarto);
        RealEstate realEstate = new RealEstate("Jose Alfredo", bairroSilva, rooms);

        Room biggestRoom = this.roomService.getBiggestRoom(realEstate);

        Assertions.assertEquals(biggestRoom, cozinha);
    }


    /**
     * @author Juliano Souza, Antonio Hugo Freire
     * Este teste espera receber uma exceção quando os cômodo estiverem vazios
     */
    @Test
    public void getBiggestRoomDeveRetornarNullSeNaoTiverRoom(){
        District bairroSilva = new District("Bairro silva ", new BigDecimal(1000.00));
        RealEstate realEstate = new RealEstate("Jose Alfredo", bairroSilva, null);

        Assertions.assertThrows(MissingRoomException.class, () -> this.roomService.getBiggestRoom(realEstate));

   }
}
