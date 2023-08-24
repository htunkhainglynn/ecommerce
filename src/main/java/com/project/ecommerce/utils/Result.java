package com.project.ecommerce.utils;

public class Result<T> {
    private T value;
    private Exception error;

    private Result(T value, Exception error) {
        this.value = value;
        this.error = error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public T getValue() {
        return value;
    }

    public Exception getError() {
        return error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(Exception error) {
        return new Result<>(null, error);
    }
}

