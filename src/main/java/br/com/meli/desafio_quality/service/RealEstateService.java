package br.com.meli.desafio_quality.service;

import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.entity.Room;
import br.com.meli.desafio_quality.entity.RoomAreaDTO;
import br.com.meli.desafio_quality.exception.MissingRoomException;
import br.com.meli.desafio_quality.exception.RealEstateAlreadyExistsException;
import br.com.meli.desafio_quality.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Julio Gama
 * estrutura base da classe
 */
@Service
public class RealEstateService {

    @Autowired
    RoomService roomService;

    @Autowired
    RealEstateRepository realEstateRepository;

    /**
     * @author Marcelo Leite
     * Metodo que retorna todos os imóveis.
     */
    public List<RealEstate> getAll() {

        return realEstateRepository.findAll();

    }

    /**
     * @author Julio Gama
     * Regra de negócio que verifica se há cômodos na propriedade, em caso positivo deve realizar o cálculo de cada área dos cômodos e somá-los
     */
    public Double getRealStateTotalArea(RealEstate realEstate){

            if(realEstate.getRooms().size() == 0){
                throw new MissingRoomException("Comodos nao foram encontrados.");
            }
                return realEstate.getRooms().stream().mapToDouble(room -> roomService.getRoomArea(room)).sum();
    }

    /**
     * @author Felipe Myose
     * retorna uma lista de RoomAreaDTO, contendo o nome do cômodo e sua respectiva área.
     */
    public List<RoomAreaDTO> getAreaByRoom(RealEstate realEstate) {

        if(realEstate.getRooms().size() == 0){
            throw new MissingRoomException("Comodos nao foram encontrados.");
        }

        return realEstate.getRooms().stream().map(room -> new RoomAreaDTO(room.getRoomName(), roomService.getRoomArea(room)))
                .collect(Collectors.toList());
    }

    /**
     * @author Marcelo Leite
     * metodo que faz o calculo do preço total do imovel, total da area * o valor do bairro.
     */
    public BigDecimal getRealEstatePrice(RealEstate realEstate) {
        return BigDecimal.valueOf(getRealStateTotalArea(realEstate)).multiply(realEstate.getDistrict().getValueDistrictM2());
    }

    /**
     * @author Antonio Hugo , Ana Preis
     *  Salva o imóvel apenas se ele ainda não existir, caso contrário joga a exceção.
     */
    public RealEstate save(RealEstate realEstate) {
        if(isRealEstateInRepo(realEstate)) {
            throw new RealEstateAlreadyExistsException("Imóvel já existe");
        }
        return realEstateRepository.save(realEstate);
    }

    /**
     * @author Ana preis
     *  Verifica se o imóvel já existe na lista de repositório pelo nome dele.
     */
    public boolean isRealEstateInRepo(RealEstate realEstate) {
        return realEstateRepository.findAll().stream().anyMatch(a -> a.getPropName().equals(realEstate.getPropName()));
    }


    /**
     * @author Antonio Hugo
     * Este método busca um imóvel pelo nome
     */
    public RealEstate findByName(String name) {
        return realEstateRepository.findByName(name);
    }

    /**
     * @author Felipe Myose
     * Retorna a entidade de Room dado roomName de um imóvel
     */
    public Room getRoomByName(RealEstate realEstate, String roomName) {
        return realEstate.getRooms().stream().filter(room -> room.getRoomName().equals(roomName)).findFirst()
                .orElseThrow(() -> new MissingRoomException("Comodo nao encontrado"));
    }
}
