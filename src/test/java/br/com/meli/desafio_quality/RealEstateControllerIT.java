package br.com.meli.desafio_quality;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.repository.RealEstateRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class RealEstateControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RealEstateRepository realEstateRepository;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * @author Ana preis
     */
    RealEstate i1 = new RealEstate();
    RealEstate i2 = new RealEstate();
    Room r1 = new Room();
    Room r2 = new Room();
    Room r3 = new Room();
    Room r4 = new Room();

    /**
     * @author Ana preis
     */
    @BeforeEach
    public void setUp() {
        r1.setRoomName("TestRoom1");
        r1.setRoomWidth(15.0);
        r1.setRoomLength(10.0);

        r2.setRoomName("TestRoom2");
        r2.setRoomWidth(25.0);
        r2.setRoomLength(30.0);

        r3.setRoomName("TestRoom3");
        r3.setRoomWidth(10.0);
        r3.setRoomLength(10.0);

        r4.setRoomName("TestRoom4");
        r4.setRoomWidth(5.0);
        r4.setRoomLength(5.0);

        List<Room> roomList1 = new ArrayList<>(Arrays.asList(r1,r2));
        List<Room> roomList2 = new ArrayList<>(Arrays.asList(r3,r4));

        District district1 = new District("Trindade1", BigDecimal.valueOf(300));
        District district2 = new District("Trindade2", BigDecimal.valueOf(200));

        i1.setPropName("Imovel1");
        i1.setDistrict(district1);
        i1.setRooms(roomList1);

        i2.setPropName("Imovel2");
        i2.setDistrict(district2);
        i2.setRooms(roomList2);
    }

    /**
     * @author Felipe Myose
     */
    @Test
    public void getAllRealEstatesEmptyTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/realestate/all"))
                .andExpect(status().isOk())
                .andReturn();
        TypeReference<List<RealEstate>> typeReference = new TypeReference<List<RealEstate>>() {};
        List<RealEstate> realEstatesFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        Assertions.assertEquals(0, realEstatesFromResponse.size());
    }

    /**
     * @author Felipe Myose
     */
    @Test
    public void getAllRealEstatesTest() throws Exception {

        realEstateRepository.save(i1);
        realEstateRepository.save(i2);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/realestate/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TypeReference<List<RealEstate>> typeReference = new TypeReference<List<RealEstate>>() {};
        List<RealEstate> realEstatesFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        Assertions.assertEquals(i1, realEstatesFromResponse.get(0));
        Assertions.assertEquals(i2, realEstatesFromResponse.get(1));
    }
}
