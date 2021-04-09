package com.verinite.interestapp.exception;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {

    INTERNAL_SERVER_ERROR("ER000", "Unexpected server error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("ER001", "Bad request", HttpStatus.BAD_REQUEST),
    CONFLICT("ER002", "Conflicting input", HttpStatus.CONFLICT),
    NOT_FOUND("ER003", "Resource not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("ER004", "Unauthorized action", HttpStatus.UNAUTHORIZED),
    DATA_ALREADY_EXISTS("ER005", "Conflicting input", HttpStatus.CONFLICT),
    ENROLLMENT_FAILED_DUE_TO_PRECONDITION("ER006", "Precondition not fulfilled", HttpStatus.PRECONDITION_FAILED),
    DATA_IDENTIFICATION_FAILED("ER007", "Not enough data to identify record", HttpStatus.CONFLICT),
    JSON_SCHEMA_FAILED("ER008", "Json Schema Failed", HttpStatus.UNAUTHORIZED),
    METHOD_ARGUMENT_TYPE_MISMATCH("ER009","Method Argument Type Mismatch",HttpStatus.CONFLICT);

    public static final String DATE_CONDITION_FAILED  ="Validfrom date is greater then validtill date";
    public static final String PAGINATION_DIRECTION_NOT_VALID = "Direction Should be Asc or Desc";
    public static final String ID_NOT_PRESENT ="Requested ID Not Found";
    public static final String PAGE_LIMIT_EXCEED="Page limit exceed";

    private String errorCode;
    private String errorMessage;
    private HttpStatus errorStatus;
    
}
