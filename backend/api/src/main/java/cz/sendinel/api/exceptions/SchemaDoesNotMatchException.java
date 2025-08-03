package cz.sendinel.api.exceptions;

public class SchemaDoesNotMatchException extends RuntimeException {
    public SchemaDoesNotMatchException(String message) {
        super(message);
    }

    public SchemaDoesNotMatchException(Throwable cause) {
        super(cause);
    }
}
