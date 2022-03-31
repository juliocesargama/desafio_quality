package br.com.meli.desafio_quality.exception;

/**
 * @author Ana Preis
 */
public class MissingRoomException extends RuntimeException {
    public MissingRoomException(String message) {
        super(message);
    }
}
