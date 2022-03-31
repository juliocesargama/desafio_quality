package br.com.meli.desafio_quality.exception.handler;

import br.com.meli.desafio_quality.entity.ErrorDTO;
import br.com.meli.desafio_quality.exception.MissingRoomException;
import br.com.meli.desafio_quality.exception.RealEstateAlreadyExistsException;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    /**
     * @author Felipe Myose
     * Exception para tratar erros referentes aos dados inseridos incorretamente, não obedencendo a entity validation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDTO>> handleModelsValidations(MethodArgumentNotValidException e) {
        List<ErrorDTO> errors = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> new ErrorDTO("Argumento inválido", objectError.getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * @author Felipe Myose, Antonio Hugo Freire
     * Exception para tratar erros de mal formatação de uma requisição.
     */
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableEx(JsonParseException e) {
        ErrorDTO error = ErrorDTO.builder()
                .name("Requisição mal formatada")
                .description(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * @author Ana preis
     *  Tratamento da exceção de quando o comodo é inexistente.
     */
    @ExceptionHandler(MissingRoomException.class)
    public ResponseEntity<?> handleModelsValidations(MissingRoomException e) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .name("Requisição mal formatada")
                .description(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * @author Ana preis
     *  Tratamento da exceção de quando o imóvel já existe
     */
    @ExceptionHandler(RealEstateAlreadyExistsException.class)
    public ResponseEntity<?> handleModelsValidations(RealEstateAlreadyExistsException e) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .name("Conflito na requisição")
                .description(e.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }


}
