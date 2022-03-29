package br.com.meli.desafio_quality.controller;

import br.com.meli.desafio_quality.entity.ErrorDTO;
import br.com.meli.desafio_quality.entity.RealEstate;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorDTO>> handleModelsValidations(MethodArgumentNotValidException e) {
        List<ErrorDTO> errors = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> new ErrorDTO("Argumento inválido", objectError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableEx(JsonParseException e) {
        ErrorDTO error = new ErrorDTO("Requisição mal formatada", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
