package ru.practicum.ewm.exceptions;

public class UpdateException extends RuntimeException {

    private final String parameter;

    public UpdateException(String parameter) {
        super(parameter);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
