package br.com.meli.desafio_quality.repository;

import org.springframework.stereotype.Repository;

/**
 * @author Felipe Myose
 */
@Repository
public interface IRepository<T> {
    T save(T t);
    T delete(T t);
    T findByName(String name);
}
