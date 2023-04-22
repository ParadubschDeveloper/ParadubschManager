package de.craftery.craftinglib;

/*
 * This is an error wrapper mainly used in unit testing.
 *
 * It is supposed to be used as a return type for functions that can fail.
 * Some environments require, that a function can fail in different ways.
 */
public class ErrorOr<T> {
    private final boolean isError;
    private final String errorMessage;
    private final T value;

    public ErrorOr(T object) {
        this.isError = false;
        this.errorMessage = null;
        this.value = object;
    }

    public ErrorOr(String errorMessage) {
        this.isError = true;
        this.errorMessage = errorMessage;
        this.value = null;
    }

    private ErrorOr(boolean isError, String errorMessage, T value) {
        this.isError = isError;
        this.errorMessage = errorMessage;
        this.value = value;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getValue() {
        return value;
    }

    public static <T> ErrorOr<T> release(T object) {
        return new ErrorOr<>(false, null, object);
    }
}
