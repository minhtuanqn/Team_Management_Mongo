package com.nli.probation.controller;

import com.nli.probation.customexception.SQLCustomException;
import com.nli.probation.customexception.TimeCustomException;
import com.nli.probation.model.APIErrorModel;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanExpressionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class for handle exception from controller
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

  Map<String, String> invalidMap;

  @ExceptionHandler(BeanExpressionException.class)
  @ResponseBody
  public String handleConflict() {
    return "Exception handled outside the Controller";
  }

  private void initMap() {
    if (invalidMap == null) {
      invalidMap = new HashMap<>();
    } else {
      invalidMap.clear();
    }
  }

  /**
   * handle mismatch type
   *
   * @param ex
   * @param headers
   * @param status
   * @param request
   * @return response entity
   */
  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    initMap();
    invalidMap.put("Type mismatch", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(), status.name(), invalidMap);
    return handleExceptionInternal(ex, apiErrorModel, headers, status, request);
  }

  /**
   * handle method argument not valid
   *
   * @param ex
   * @param headers
   * @param status
   * @param request
   * @return response entity
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    initMap();
    invalidMap.put(ex.getFieldError().getField(), ex.getFieldError().getDefaultMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(), status.name(), invalidMap);
    return super.handleExceptionInternal(ex, apiErrorModel, headers, status, request);
  }

  /**
   * handle method missing path variable
   *
   * @param ex
   * @param headers
   * @param status
   * @param request
   * @return response entity
   */
  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    initMap();
    invalidMap.put("Missing path variable", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(), status.name(), invalidMap);
    return super.handleExceptionInternal(ex, apiErrorModel, headers, status, request);
  }

  /**
   * handle method missing request param
   *
   * @param ex
   * @param headers
   * @param status
   * @param request
   * @return response entity
   */
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    initMap();
    invalidMap.put("Missing request param", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(), status.name(), invalidMap);
    return super.handleExceptionInternal(ex, apiErrorModel, headers, status, request);
  }

  /**
   * handle unsupported media type
   *
   * @param ex
   * @param headers
   * @param status
   * @param request
   * @return response entity
   */
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    initMap();
    invalidMap.put("Unsupported media type", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(), status.name(), invalidMap);
    return super.handleExceptionInternal(ex, apiErrorModel, headers, status, request);
  }

  /**
   * Handle exception for parameter
   *
   * @param ex
   * @return response entity
   */
  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstrainExceptions(
      ConstraintViolationException ex) {
    initMap();
    Iterator<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations().iterator();
    while (constraintViolations.hasNext()) {
      ConstraintViolation<?> constraintViolation = constraintViolations.next();
      invalidMap.put(constraintViolation.getPropertyPath().toString(),
          constraintViolation.getMessage());
    }
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.name(), invalidMap);
    return new ResponseEntity<>(apiErrorModel, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle exception for constrain violation of SQL
   *
   * @param ex
   * @return response entity
   */
  @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
  public ResponseEntity<Object> handleSQLExceptionExceptions(
      SQLIntegrityConstraintViolationException ex) {
    initMap();
    invalidMap.put("Constrain error", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.name(), invalidMap);
    return new ResponseEntity<>(apiErrorModel, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle exception of no such element
   *
   * @param ex
   * @return response entity
   */
  @ExceptionHandler({NoSuchElementException.class})
  public ResponseEntity<Object> handleSNoSuchElementExceptions(
      NoSuchElementException ex) {
    initMap();
    invalidMap.put("No such element error", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.name(), invalidMap);
    return new ResponseEntity<>(apiErrorModel, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle illegal exception
   *
   * @param ex
   * @return response entity
   */
  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<Object> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    initMap();
    invalidMap.put("Illegal argument exception", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.name(), invalidMap);
    return new ResponseEntity<>(apiErrorModel, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle exist unique identity exception
   *
   * @param ex
   * @return response entity
   */
  @ExceptionHandler({SQLCustomException.class})
  public ResponseEntity<Object> handleSQLCustomExceptionException(
      SQLCustomException ex) {
    initMap();
    invalidMap.put("Data exception", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.name(), invalidMap);
    return new ResponseEntity<>(apiErrorModel, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle time exception
   *
   * @param ex
   * @return response entity
   */
  @ExceptionHandler({TimeCustomException.class})
  public ResponseEntity<Object> handleRangeTimeException(
      TimeCustomException ex) {
    initMap();
    invalidMap.put("Time custom exception", ex.getMessage());
    APIErrorModel apiErrorModel = new APIErrorModel(LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.name(), invalidMap);
    return new ResponseEntity<>(apiErrorModel, HttpStatus.BAD_REQUEST);
  }

}
