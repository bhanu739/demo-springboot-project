package com.springsec.demo.exception;

import java.io.Serial;

public class JwtTokenInvalidException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public JwtTokenInvalidException(String message) {
        super(message);
    }
}
