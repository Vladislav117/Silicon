package ru.vladislav117.silicon.node;

public class SiNodeException extends RuntimeException{
    public SiNodeException() {
        super();
    }

    public SiNodeException(String message) {
        super(message);
    }

    public SiNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SiNodeException(Throwable cause) {
        super(cause);
    }

    protected SiNodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
