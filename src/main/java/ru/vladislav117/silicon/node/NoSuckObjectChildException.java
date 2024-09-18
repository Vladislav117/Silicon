package ru.vladislav117.silicon.node;

public class NoSuckObjectChildException extends SiNodeException {
    public final String key;
    public NoSuckObjectChildException(String key) {
        super("Unknown key %s".formatted(key));
        this.key=key;
    }
/*
    public NoSuckObjectChildException(int index) {
        super("Unknown index %s".formatted(index));
    }*/
}
