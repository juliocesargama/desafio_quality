package br.com.meli.desafio_quality.repository;

import org.springframework.stereotype.Repository;

/**
 * @author Felipe Myose
 */
@Repository
public interface IRepository<T> {
    T save(T t);
    boolean delete(String name);
    T findByName(String name);
}
