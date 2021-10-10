package gr.uth.exceptions;

import javax.ws.rs.core.Response;

public enum I18NMessage {

    ATTACHMENT_NOT_FOUND("genesis.attachment-not-found", "Attachment not found", Response.Status.BAD_REQUEST),
    INVALID_PHOTO_TYPE("genesis.invalid-photo-type", "Invalid photo type", Response.Status.BAD_REQUEST);

    private final String message;
    private final String description;
    private final Response.Status responseStatus;

    I18NMessage(String message, String description, Response.Status responseStatus) {
        this.message = message;
        this.description = description;
        this.responseStatus = responseStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public Response.Status getResponseStatus() {
        return responseStatus;
    }
}
