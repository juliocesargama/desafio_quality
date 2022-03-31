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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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


        Assertions.assertEquals(expectedMessage,  error.getDescription());
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera criar um Imóvel e recber retorno 201 com o payload do imóvel criado.
     */
    @Test
    public void shouldCreateRealEstate() throws Exception {

        String  realEstate = objectMapper.writeValueAsString(i1);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.propName").value("Imovel1"));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 409 com o payload error com messagem que o imóvel já existe.
     */

    @Test
    public void shouldNotCreateRealEstateThatAlreadyExists() throws Exception {
        realEstateRepository.save(i1);

        String  realEstate = objectMapper.writeValueAsString(i1);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.description").value("Imóvel já existe"));
    }
    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com messagem O nome da propriedade não pode ficar vazio.
     */
    @Test
    public void shouldNotBeAbleToCreateAnUnnamedPropName() throws Exception{
        RealEstate mockRealEstate = new RealEstate(null,
                new District("Jardim 1", BigDecimal.valueOf(500.0)), new ArrayList<>());

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("O nome da propriedade não pode ficar vazio."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com messagem O nome da propriedade deve começar com letra maiúscula.
     */
    @Test
    public void shouldNotBeAbleToCreateRealEstateFirstLetterOfTheLowercaseName() throws Exception{
        RealEstate mockRealEstate = new RealEstate("letraMinúscula",
                new District("Jardim 1", BigDecimal.valueOf(500.0)), new ArrayList<>());

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("O nome da propriedade deve começar com letra maiúscula."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com a messagem O comprimento do nome da propriedade não pode exceder 30 caracteres.
     */
    @Test
    public void shouldNotBeAbleToCreateRealEstateWhoseNameExceeds30Characters() throws Exception{
        RealEstate mockRealEstate = new RealEstate("NomeMuitoCompridoParaEstaPropeidade",
                new District("Jardim 1", BigDecimal.valueOf(500.0)), new ArrayList<>());

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("O comprimento do nome da propriedade não pode exceder 30 caracteres."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com a messagem O nome do cômodo deve começar com uma letra maiúscula.
     */
    @Test
    public void shouldNotBeAbleToCreateRealEstateRoomNameFirstLetterOfTheLowercase() throws Exception{
        RealEstate mockRealEstate = new RealEstate("Imoval1",
                new District("Jardim 1", BigDecimal.valueOf(500.0)),  List.of(new Room(
                "sala", 15.0, 10.0)));

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("O nome do cômodo deve começar com uma letra maiúscula."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com a messagem O nome do cômodo não pode excder 30 caracteres.
     */

    @Test
    public void shouldNotBeAbleToCreateRealEstateRoomNameExceeds30Characters() throws Exception{
        RealEstate mockRealEstate = new RealEstate("Imoval1",
                new District("Jardim 1", BigDecimal.valueOf(500.0)),  List.of(new Room(
                "NomeMuitoCompridoParaEstaPropeidade", 15.0, 10.0)));

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("O nome do cômodo não pode excder 30 caracteres."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com a messagem O nome do cômodo não pode estar vazio.
     */
    @Test
    public void shouldNotBeAbleToCreateRealEstateWithRoomNameEmpty() throws Exception{
        RealEstate mockRealEstate = new RealEstate("Imoval1",
                new District("Jardim 1", BigDecimal.valueOf(500.0)),  List.of(new Room(
                null, 15.0, 10.0)));

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("O nome do cômodo não pode estar vazio."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com a messagem A largura do cômodo não pode estar vazia.
     */
    @Test
    public void shouldNotBeAbleToCreateRealEstateWithRoomWidthEmpty() throws Exception{
        RealEstate mockRealEstate = new RealEstate("Imoval1",
                new District("Jardim 1", BigDecimal.valueOf(500.0)),  List.of(new Room(
                "Casa", null, 10.0)));

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("A largura do cômodo não pode estar vazia."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com a messagem A largura máxima permitida por cômodo é de 25 metros.
     */
    @Test
    public void shouldNotBeAbleToCreateRealEstateWithRoomMaximumWidthIs25Meters() throws Exception{
        RealEstate mockRealEstate = new RealEstate("Imoval1",
                new District("Jardim 1", BigDecimal.valueOf(500.0)),  List.of(new Room(
                "Casa", 30.0, 10.0)));

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("A largura máxima permitida por cômodo é de 25 metros."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com a messagem O comprimento máximo permitido por cômodo é de 33 metros.
     */
    @Test
    public void shouldNotBeAbleToCreateRealEstateWithRoomMaximumLengthIs33Meters() throws Exception{
        RealEstate mockRealEstate = new RealEstate("Imoval1",
                new District("Jardim 1", BigDecimal.valueOf(500.0)),  List.of(new Room(
                "Casa", 25.0, 40.0)));

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("O comprimento máximo permitido por cômodo é de 33 metros."));
    }

    /**
     * @author Antonio Hugo Freire
     * Este teste espera receber retorno 400 com o payload error com a messagem O comprimento do cômodo não pode estar vazio.
     */
    @Test
    public void shouldNotBeAbleToCreateRealEstateWithRoomLengthEmpty() throws Exception{
        RealEstate mockRealEstate = new RealEstate("Imoval1",
                new District("Jardim 1", BigDecimal.valueOf(500.0)),  List.of(new Room(
                "Casa", 25.0, null)));

        String  realEstate = objectMapper.writeValueAsString(mockRealEstate);

        mockMvc.perform(post("/realestate")
                .contentType("application/json")
                .content(realEstate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].description").value("O comprimento do cômodo não pode estar vazio."));
    }
}
