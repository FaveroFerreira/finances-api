package com.deliver.finances.exception;

public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3992894501225831700L;

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException() {
        this("Registro não encontrado!");
    }

}
