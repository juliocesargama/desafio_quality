package br.com.meli.desafio_quality.exception;

/**
 * @author Antonio Hugo Freire
 */
public class MissingRealEstateException extends RuntimeException {
    public MissingRealEstateException(String message) {
        super(message);
    }
}
