package com.verinite.interestapp.exception;


public class ApiError {
    private String code;
    private String message;
    private String details;
   

    public static ApiError.ApiErrorBuilder builder() {
        return new ApiError.ApiErrorBuilder();
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.details;
    }



    public ApiError(String code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public ApiError(String code2, String message2, String version2, String details2, ApiError innerError2,
            String debugId2, String reference2) {
    }

    public static class ApiErrorBuilder {
        private String code;
        private String message;
        private String details;

        ApiErrorBuilder() {
        }

        public ApiError.ApiErrorBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ApiError.ApiErrorBuilder message(String message) {
            this.message = message;
            return this;
        }


        public ApiError.ApiErrorBuilder details(String details) {
            this.details = details;
            return this;
        }

        public ApiError build() {
            return new ApiError(this.code, this.message, this.details);
        }

    }
}