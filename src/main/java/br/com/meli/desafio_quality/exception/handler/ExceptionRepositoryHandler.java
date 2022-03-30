package br.com.meli.desafio_quality.exception.handler;

import br.com.meli.desafio_quality.entity.ErrorDTO;
import br.com.meli.desafio_quality.exception.MissingRealEstateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionRepositoryHandler {

    /**
     * @author Antonio Hugo Freire
     * **/
    @ExceptionHandler(MissingRealEstateException.class)
    public ResponseEntity<?> handleModelsValidations(MissingRealEstateException e) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .name("Requisição mal formatada")
                .description(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
