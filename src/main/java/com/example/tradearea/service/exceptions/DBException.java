package com.example.tradearea.service.exceptions;



public class DBException extends RuntimeException {

    private final OperationType operationType;
    private final String requestParams;

    public DBException(OperationType operationType, String requestParams, Exception e) {
        super("Exception for "  + operationType
                + " operation, with params:(" + requestParams + ")", e);
        this.operationType = operationType;
        this.requestParams = requestParams;
    }

    public DBException(OperationType operationType, String requestParams) {
        super("Exception for "  + operationType
                + " operation, with params:(" + requestParams + ")");
        this.operationType = operationType;
        this.requestParams = requestParams;
    }

    public OperationType getOperationType() {
        return operationType;
    }
    public String getRequestParams() {
        return requestParams;
    }


}