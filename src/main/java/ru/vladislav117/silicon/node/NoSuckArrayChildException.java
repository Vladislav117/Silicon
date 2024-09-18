package ru.vladislav117.silicon.node;

public class NoSuckArrayChildException extends SiNodeException {
    public final int index;
    public NoSuckArrayChildException(int index, SiNode context) {
        super("Unknown index %s".formatted(index));
        this.index=index;
    }
/*
    public NoSuckObjectChildException(int index) {
        super("Unknown index %s".formatted(index));
    }*/
}
