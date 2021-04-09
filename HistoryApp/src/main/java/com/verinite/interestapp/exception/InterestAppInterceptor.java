package com.verinite.interestapp.exception;

import java.io.IOException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

import javax.persistence.EntityNotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j

public class InterestAppInterceptor {

	private StackTraceElement findCause(Exception e) {
		StackTraceElement[] stackTrace = e.getStackTrace();

		for (int i = 0; i < stackTrace.length; i++) {
			String[] packageArray = stackTrace[i].getClassName().split("\\.");
			String subPackage = packageArray[0] + "." + packageArray[1];
			if (subPackage.equalsIgnoreCase("com.verinite")) {
				return stackTrace[i];
			}

		}
		return null;
	}

	private ResponseEntity<ApiError> getErrorResponse(ErrorCodes errorCode, Exception e) {

		ApiError errorResponse;
		String errorMessage = errorCode.getErrorMessage();
		StackTraceElement stackTrace = findCause(e);
		if (stackTrace != null) {

			log.error(errorCode.getErrorMessage() + e.getMessage() + stackTrace.getClassName()
					+ stackTrace.getMethodName() + errorCode.getErrorCode() + stackTrace.getLineNumber());

			if (e instanceof InterestAppException) {
				errorMessage = e.getMessage();
			}

			errorResponse = ApiError.builder().code(errorCode.getErrorCode()).message(errorMessage)
								.details(stackTrace.toString()).build();
		} else {
			log.error(errorCode.getErrorMessage() + e.getMessage() + e.getClass().getName() + errorCode.getErrorCode());

			if (e instanceof InterestAppException) {
				errorMessage = e.getMessage();
			}
			errorResponse = ApiError.builder().code(errorCode.getErrorCode()).message(errorMessage)
								.details(e.fillInStackTrace().getMessage()).build();
		}

		return new ResponseEntity<>(errorResponse, errorCode.getErrorStatus());
	}

	@ExceptionHandler(value = { InterestAppException.class })
	public ResponseEntity<ApiError> handleException(InterestAppException e) {
		return getErrorResponse(e.getHistoryModuleErrorCode(), e);
	}

	@ExceptionHandler(value = { IllegalArgumentException.class })
	public ResponseEntity<ApiError> handleException(IllegalArgumentException e) {
		return getErrorResponse(ErrorCodes.CONFLICT, e);
	}

	@ExceptionHandler(value = { IOException.class })
	public ResponseEntity<ApiError> handleException(IOException e) {
		return getErrorResponse(ErrorCodes.CONFLICT, e);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<ApiError> handleException(Exception e) {
		return getErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(value = { ParseException.class })
	public ResponseEntity<ApiError> handleException(ParseException e) {
		return getErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(value = { DateTimeException.class })
	public ResponseEntity<ApiError> handleException(DateTimeException e) {
		return getErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(value = { DateTimeParseException.class })
	public ResponseEntity<ApiError> handleException(DateTimeParseException e) {
		return getErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(value = { JsonMappingException.class })
	public ResponseEntity<ApiError> handleException(JsonMappingException e) {
		return getErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(value = { JsonProcessingException.class })
	public ResponseEntity<ApiError> handleException(JsonProcessingException e) {
		return getErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(value = { InvalidFormatException.class })
	public ResponseEntity<ApiError> handleException(InvalidFormatException e) {
		return getErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(value = { EntityNotFoundException.class })
	public ResponseEntity<ApiError> handleException(EntityNotFoundException e) {
		return getErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR, e);
	}

	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
	public ResponseEntity<ApiError> handleException(MethodArgumentTypeMismatchException e) {
		return getErrorResponse(ErrorCodes.METHOD_ARGUMENT_TYPE_MISMATCH, e);
	}

	@ExceptionHandler(value = { HttpMessageNotReadableException.class })
	public ResponseEntity<ApiError>
	handleException(HttpMessageNotReadableException e) {
	return getErrorResponse(ErrorCodes.METHOD_ARGUMENT_TYPE_MISMATCH, e);
	}

}
