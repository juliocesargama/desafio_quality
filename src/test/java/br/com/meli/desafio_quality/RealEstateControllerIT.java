package br.com.meli.desafio_quality;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.repository.RealEstateRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
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
     * @author Felipe Myose
     */
    @Test
    public void getAllRealEstatesTest() throws Exception {
        Room r1 = new Room("TestRoom1", 15.0, 10.0);
        Room r2 = new Room("TestRoom2", 30.0, 30.0);
        Room r3 = new Room("TestRoom3", 10.0, 10.0);
        Room r4 = new Room("TestRoom4", 5.0, 5.0);
        List<Room> roomList1 = new ArrayList<>(Arrays.asList(r1,r2));
        List<Room> roomList2 = new ArrayList<>(Arrays.asList(r3,r4));
        District district1 = new District("Trindade1", BigDecimal.valueOf(300));
        District district2 = new District("Trindade2", BigDecimal.valueOf(200));
        RealEstate i1 = new RealEstate("Imovel1", district1, roomList1);
        RealEstate i2 = new RealEstate("Imovel2", district2, roomList2);

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
