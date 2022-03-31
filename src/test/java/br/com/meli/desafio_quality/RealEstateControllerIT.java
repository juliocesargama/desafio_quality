package br.com.meli.desafio_quality;

import br.com.meli.desafio_quality.entity.District;
import br.com.meli.desafio_quality.entity.ErrorDTO;
import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.repository.RealEstateRepository;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
     *  Objetos utilizados em cada teste de integração.
     */
    RealEstate i1 = new RealEstate();
    RealEstate i2 = new RealEstate();
    Room r1 = new Room();
    Room r2 = new Room();
    Room r3 = new Room();
    Room r4 = new Room();

    /**
     * @author Ana preis
     *  Setando os objetos utilizados antes de cada teste de integração
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
     * Limpa a base de dados (lista) após a execução de cada teste
     */
    @AfterEach
    public void resetData() {
        List<RealEstate> temp = realEstateRepository.findAll();
        int size = temp.size();
        for (int i = 0; i < size; i++) {
            realEstateRepository.delete(temp.get(0));
        }
    }

    /**
     * @author Felipe Myose
     * Criação da estrutura base para os Testes de integração
     * Teste para verificar /realestate/all
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
     * Teste de integração para verificar se o o endpoint /realestate/all retorna todos os imóveis do repositório.
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

    /**
     * @author Ana Preis
     *  Testa se endpoint getLargestRoom() retorna o r2 (maior comodo).
     */
    @Test
    public void getLargestRoom() throws Exception {

        realEstateRepository.save(i1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/realestate/{propName}/largestroom", "Imovel1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TypeReference<Room> typeReference = new TypeReference<>() {};
        Room roomFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        Assertions.assertEquals(r2, roomFromResponse);
    }

    /**
     * @author Ana Preis
     *  Testa se, ao passar um imóvel inexistente para o endpoint getLargestRoom, retorna a exceção de Imóvel
     *  não encontrado.
     */
    @Test
    public void getLargestRoomWithException() throws Exception {

        realEstateRepository.save(i1);

        MvcResult result = mockMvc.perform(get("/realestate/{propName}/largestroom"
                , "Imovel_Inexitente"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        TypeReference<ErrorDTO> typeReference = new TypeReference<>() {};

        ErrorDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference) ;

        Assertions.assertEquals("Imovel nao encontrado", response.getDescription());
    }
  
    /**
     * @author Marcelo Leite
     * Teste de integração, testenado o endpoint que retorna o preço total do imovel, recebendo como @PathVariable o nome do imovel.
     */
    @Test
    public void getRealEstatePriceTest() throws Exception {
        realEstateRepository.save(i1);
        realEstateRepository.save(i2);

        MvcResult imovel1 = mockMvc.perform(MockMvcRequestBuilders.get("/realestate/Imovel1/price"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult imovel2 = mockMvc.perform(MockMvcRequestBuilders.get("/realestate/Imovel2/price"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TypeReference<BigDecimal> typeReference = new TypeReference<BigDecimal>() {};
        BigDecimal totalPrice = objectMapper.readValue(imovel1.getResponse().getContentAsString(), typeReference);
        BigDecimal totalPrice2 = objectMapper.readValue(imovel2.getResponse().getContentAsString(), typeReference);

        double expectdPrice = 270000.0;
        double expectdPrice2 = 25000.0;

        Assertions.assertEquals(BigDecimal.valueOf(expectdPrice), totalPrice);
        Assertions.assertEquals(BigDecimal.valueOf(expectdPrice2), totalPrice2);
    }

    /**
     * @author Julio Gama
     * Teste de integração para verificar se passando os parâmetros corretos, é realizado o cálculo  da área do cômodo de forma esperada.
     */
    @Test
    public void getRoomAreaTest() throws Exception{

        realEstateRepository.save(i1);

        MvcResult result = mockMvc.perform(get("/realestate/{propName}/{roomName}/area","Imovel1","TestRoom1"))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<Double> typeReference = new TypeReference<Double>() {};
        Double roomAreaFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        Assertions.assertEquals(150.0,roomAreaFromResponse.doubleValue());

    }

    /**
     * @author Julio Gama
     * Teste de Integraçao para verificar se é lançada uma excessão caso seja passado um imóvel inválido.
     */
    @Test
    public void getRoomAreaPropNameInvalid() throws Exception{

        realEstateRepository.save(i1);
        MvcResult result = mockMvc.perform(get("/realestate/{propName}/{roomName}/area","Imovel","TestRoom"))
                .andExpect(status().is4xxClientError())
                .andReturn();

            TypeReference<ErrorDTO> typeReference = new TypeReference<>() {};

            ErrorDTO response = objectMapper.readValue(result.getResponse().getContentAsString(),typeReference);
            Assertions.assertEquals("Imovel nao encontrado", response.getDescription());
    }

    /**
     * @author Julio Gama
     * Teste de Integraçao para verificar se é lançada uma excessão caso seja passado um cômodo inválido.
     */
    @Test
    public void getRoomAreaRoomNameInvalid() throws Exception{

        realEstateRepository.save(i1);
        MvcResult result = mockMvc.perform(get("/realestate/{propName}/{roomName}/area","Imovel1","Quarto"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        TypeReference<ErrorDTO> typeReference = new TypeReference<>() {};

        ErrorDTO response = objectMapper.readValue(result.getResponse().getContentAsString(),typeReference);
        Assertions.assertEquals("Comodo nao encontrado", response.getDescription());
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera um statusCode 200 com o resutado total da area do Imóvel.git
     */
    @Test
    public void shouldCalcTotalAreaOfRealEstate() throws Exception{
        realEstateRepository.save(i1);
        String expected = "900.0";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/realestate/Imovel1/totalarea"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assertions.assertEquals(expected, result.getResponse().getContentAsString());
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera um status de error 400
     */
    @Test
    public void shouldNotBeAbleCalcTotalAreaOfRealEstate() throws Exception{

        String expectedMessage = "Imovel nao encontrado";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/realestate/not_exists/totalarea"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        TypeReference<ErrorDTO> typeReference = new TypeReference<ErrorDTO>() {};
        ErrorDTO error = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        Assertions.assertEquals(expectedMessage,  error.getDescription());
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera um status de error 400
     */
    @Test
    public void shouldBeAbleFailException() throws Exception{

        RealEstate mockRealEstate = new RealEstate("Casa",
                new District("Jardim 1", BigDecimal.valueOf(500.0)), new ArrayList<>());

        realEstateRepository.save(mockRealEstate);

        String expectedMessage = "Comodos nao foram encontrados.";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/realestate/Casa/totalarea"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        TypeReference<ErrorDTO> typeReference = new TypeReference<ErrorDTO>() {};
        ErrorDTO error = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        System.out.println(error.getDescription());
        Assertions.assertEquals(expectedMessage,  error.getDescription());
    }


    /**
     * @author Julio Gama
     * Teste de Integraçao para verificar se o imóvel retorna corretamente.
     */
    @Test
    public void shouldGetRealEstateByName() throws Exception {

        realEstateRepository.save(i1);

        MvcResult result = mockMvc.perform(get("/realestate/{name}/","Imovel1"))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<RealEstate> typeReference = new TypeReference<RealEstate>() {};
        RealEstate realStateFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        Assertions.assertEquals(i1,realStateFromResponse);

    }

    /**
     * @author Julio Gama
     * Teste de Integraçao para verificar se é lançada uma excessão caso seja passado imóvel inválido.
     */
    @Test
    public void shouldGetRealEstateByNameThrowsExpection() throws Exception {

        realEstateRepository.save(i1);

        MvcResult result = mockMvc.perform(get("/realestate/{name}/","Imovel"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        TypeReference<ErrorDTO> typeReference = new TypeReference<>() {};
        ErrorDTO error = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        Assertions.assertEquals("Imovel nao encontrado",error.getDescription());

    }

}
