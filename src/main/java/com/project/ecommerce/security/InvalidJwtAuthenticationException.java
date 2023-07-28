package com.project.ecommerce.security;

public class InvalidJwtAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidJwtAuthenticationException(String expiredOrInvalidJwtToken) {
        super(expiredOrInvalidJwtToken);
    }
}
