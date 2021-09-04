package gr.uth.exceptions;

import gr.uth.dto.ValidationError;

public class ExceptionBuilder {

    private ExceptionBuilder() {}

    public static ValidationException fromMessage(I18NMessage i18NMessage) {
        return new ValidationException(new ValidationError(i18NMessage.getMessage(), i18NMessage.getDescription()),
                i18NMessage.getResponseStatus());
    }
}
