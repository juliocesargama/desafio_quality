package br.com.meli.desafio_quality.exception;

/**
 * @author Ana Preis
 */
public class RealEstateAlreadyExistsException extends RuntimeException {
    public RealEstateAlreadyExistsException(String message) {
        super(message);
    }
}
