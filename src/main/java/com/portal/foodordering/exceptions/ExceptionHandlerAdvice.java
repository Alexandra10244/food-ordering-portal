package com.portal.foodordering.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;


@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(UserNotFoundExceptionException.class)
    public ResponseEntity<String> userNotFoundException(UserNotFoundExceptionException userNotFoundExceptionException) {
        return new ResponseEntity<>(objectToString(Map.of("message", userNotFoundExceptionException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<String> restaurantNotFoundException(RestaurantNotFoundException restaurantNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", restaurantNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> itemNotFoundException(ItemNotFoundException itemNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", itemNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> orderNotFound(OrderNotFoundException orderNotFoundException) {
        return new ResponseEntity<>(objectToString(Map.of("message", orderNotFoundException.getMessage())), NOT_FOUND);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> insufficientStock(InsufficientStockException insufficientStock) {
        return new ResponseEntity<>(objectToString(Map.of("message", insufficientStock.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(ItemCantBeRemovedException.class)
    public ResponseEntity<String> itemCantBeRemoved(ItemCantBeRemovedException itemCantBeRemovedException) {
        return new ResponseEntity<>(objectToString(Map.of("message", itemCantBeRemovedException.getMessage())), METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    public ResponseEntity<String> productNotAvailable(ProductNotAvailableException productNotAvailableException) {
        return new ResponseEntity<>(objectToString(Map.of("message", productNotAvailableException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(PaymentStatusExceptionException.class)
    public ResponseEntity<String> paymentStatusException(PaymentStatusExceptionException paymentStatusExceptionException) {
        return new ResponseEntity<>(objectToString(Map.of("message", paymentStatusExceptionException.getMessage())), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            String message = violation.getMessage();
            errors.put(fieldName, message);
        }
        return new ResponseEntity<>(objectToString(errors), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String defaultMessage = Objects.requireNonNull(error.getDefaultMessage());
            errors.put(error.getField(), defaultMessage);
        });
        return new ResponseEntity<>(objectToString(errors), HttpStatus.BAD_REQUEST);
    }


    private String objectToString(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            log.error("Error processing response to string");
            return "Internal error";
        }
    }
}
