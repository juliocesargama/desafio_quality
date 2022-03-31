package br.com.meli.desafio_quality.repository;

import br.com.meli.desafio_quality.entity.RealEstate;
import br.com.meli.desafio_quality.exception.MissingRealEstateException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Hugo Freire
 */
@Repository
public class RealEstateRepository implements IRepository<RealEstate> {
    private final List<RealEstate> realEstates = new ArrayList<>();

    /**
     * @author Antonio Hugo Freire
     */
    @Override
    public RealEstate save(RealEstate realEstate) {
        realEstates.add(realEstate);
        return realEstate;
    }

    /**
     * @author Antonio Hugo Freire
     */
    @Override
    public boolean delete(String name) {
        return false;
    }

    /**
     * @author Antonio Hugo Freire
     */
    @Override
    public RealEstate findByName(String name) {
        return realEstates.stream().filter(realEstate -> realEstate.getPropName().equals(name)).findFirst().orElseThrow(() ->new MissingRealEstateException("Imóvel não encontrado"));
    }

    public List<RealEstate> findAll() {
        return realEstates;
    }
}
