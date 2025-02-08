package gr.uth.exceptions;

import gr.uth.dto.ValidationError;

import jakarta.ws.rs.core.Response;

public class ValidationException extends RuntimeException {

    private ValidationError validationError;
    private Response.Status responseStatus;

    public ValidationException(ValidationError validationError, Response.Status responseStatus) {
        super(validationError.getMessage());
        this.validationError = validationError;
        this.responseStatus = responseStatus;
    }

    public ValidationError getValidationError() {
        return validationError;
    }

    public void setValidationError(ValidationError validationError) {
        this.validationError = validationError;
    }

    public Response.Status getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Response.Status responseStatus) {
        this.responseStatus = responseStatus;
    }
}
