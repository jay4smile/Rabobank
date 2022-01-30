package com.rabobank.transactionservice.exception;

import com.rabobank.transactionservice.domain.Status;
import com.rabobank.transactionservice.domain.ValidationStatus;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ValidationStatus apiError = new ValidationStatus();


        apiError.setResult(Status.BAD_REQUEST);
        return new ResponseEntity(apiError, headers, status);
    }


//    @ExceptionHandler(value = InvalidDataException.class)
//    public ResponseEntity<Object> handleException(
//            Exception exception) {
//
//        ValidationStatus apiError = new ValidationStatus();
//
//
//        apiError.setMessage(exception.getMessage());
//        apiError.setStatus(Status.FAIL);
//        return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
//    }
}
