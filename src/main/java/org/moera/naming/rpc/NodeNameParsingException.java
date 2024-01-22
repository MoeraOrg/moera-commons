package org.moera.naming.rpc;

public class NodeNameParsingException extends RuntimeException {

    public NodeNameParsingException(String message) {
        super(message);
    }

    public NodeNameParsingException(String message, Throwable cause) {
        super(message, cause);
    }

}
