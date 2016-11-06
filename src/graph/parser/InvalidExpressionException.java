package graph.parser;

/**
 * This exception is thrown when an expression is syntactically incorrect.
 */
final class InvalidExpressionException extends RuntimeException {

    /**
     * Constructs an {@code InvalidExpressionException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    InvalidExpressionException(String message) {
        super(message);
    }

}
