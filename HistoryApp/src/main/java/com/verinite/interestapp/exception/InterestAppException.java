package com.verinite.interestapp.exception;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class InterestAppException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final ErrorCodes historyModuleErrorCode;

    public InterestAppException(ErrorCodes historyModuleErrorCode, String message) {
    	
        super(message);
        this.historyModuleErrorCode = historyModuleErrorCode;
    }

    public InterestAppException(ErrorCodes historyModuleErrorCode, String message, Throwable cause) {
        super(message, cause);
        this.historyModuleErrorCode = historyModuleErrorCode;
    }

    public InterestAppException(ErrorCodes historyModuleErrorCode, Throwable cause) {
        super(cause);
        this.historyModuleErrorCode = historyModuleErrorCode;
    }
    
}
