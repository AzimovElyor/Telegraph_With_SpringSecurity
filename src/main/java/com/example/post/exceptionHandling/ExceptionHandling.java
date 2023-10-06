package com.example.post.exceptionHandling;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.post.dto.ExceptionDto;


import java.util.ArrayList;
import java.util.List;
@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ExceptionDto> runTimeException(RuntimeException exception){
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionDto,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> validatorException(ConstraintViolationException exception){
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionDto,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ExceptionDto>> handleBindException(BindException ex) {
        ArrayList<ExceptionDto> exceptionDtos = new ArrayList<>();
        StringBuilder errorMessage = new     StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage());
            exceptionDtos.add(new ExceptionDto(errorMessage.toString(),HttpStatus.BAD_REQUEST.value()));
            errorMessage = new StringBuilder();
        }
        return new ResponseEntity<>(exceptionDtos,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDto> handlerUserNotFoundException(UsernameNotFoundException exception){
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionDto,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handlerException(Exception exception){
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionDto,HttpStatus.BAD_REQUEST);
    }
   /* @ExceptionHandler
    public ResponseEntity<ExceptionDto> loginExceptionHandler(LoginException ex){
                ExceptionDto exceptionDto = new ExceptionDto(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
                return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
        }*/
}
