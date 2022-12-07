package ru.practicum.ewm.exceptions;

public class IncorrectParameterException extends RuntimeException {

    private final String parameter;

    public IncorrectParameterException(String parameter) {
        super(parameter);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}