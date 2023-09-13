package com.project.ecommerce.utils;

import lombok.Getter;

@Getter
public class Result<T> {  // helper class for railway oriented programming
    private final T value;
    private final Exception error;

    private Result(T value, Exception error) {
        this.value = value;
        this.error = error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(Exception error) {
        return new Result<>(null, error);
    }
}

