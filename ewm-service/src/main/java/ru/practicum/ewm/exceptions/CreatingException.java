package ru.practicum.ewm.exceptions;

public class CreatingException extends RuntimeException {

    private final String parameter;

    public CreatingException(String parameter) {
        super(parameter);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
