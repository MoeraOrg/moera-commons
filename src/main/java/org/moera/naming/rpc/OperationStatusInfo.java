package org.moera.naming.rpc;

import java.util.UUID;

public class OperationStatusInfo {

    private UUID operationId;
    private String name;
    private int generation;
    private OperationStatus status;
    private Long added;
    private Long completed;
    private String errorCode;
    private String errorMessage;

    public UUID getOperationId() {
        return operationId;
    }

    public void setOperationId(UUID operationId) {
        this.operationId = operationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public Long getAdded() {
        return added;
    }

    public void setAdded(Long added) {
        this.added = added;
    }

    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
