package br.com.meli.desafio_quality.repository;

public interface IRepository<T> {
    void save(T t);
    boolean delete(String name);
    T findByName(String name);
}
