package br.com.meli.desafio_quality.repository;

import br.com.meli.desafio_quality.entity.RealEstate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class RealEstateRepository implements IRepository<RealEstate> {
    private final List<RealEstate> realEstates = new ArrayList<>();

    @Override
    public RealEstate save(RealEstate realEstate) {
        realEstates.add(realEstate);
        return realEstate;
    }

    @Override
    public boolean delete(String name) {
        return false;
    }

    @Override
    public RealEstate findByName(String name) {
        return realEstates.stream().filter(realEstate -> realEstate.getPropName().equals(name)).findFirst().orElse(new RealEstate());
    }

    public List<RealEstate> findAll() {
        return realEstates;
    }
}
