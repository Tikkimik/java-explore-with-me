package ru.practicum.ewm.exceptions;

public class ConflictException extends RuntimeException {

    private final String parameter;

    public ConflictException(String parameter) {
        super(parameter);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
