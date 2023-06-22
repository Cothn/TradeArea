package com.example.tradearea.service.exceptions;

public enum OperationType {
    READE_PAGE,
    READE_ONE,
    CREATE,
    UPDATE,
    DELETE;

    @Override
    public String toString() {
        return super.toString().replace('_', ' ');
    }
}
