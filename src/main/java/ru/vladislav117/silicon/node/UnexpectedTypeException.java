package ru.vladislav117.silicon.node;

import java.util.StringJoiner;

public class UnexpectedTypeException extends SiNodeException {
    public UnexpectedTypeException(SiType actual, SiType expected) {
        super(buildMessage(actual, expected));
    }

    public UnexpectedTypeException(SiType actual, SiType expected, SiType... expectedOr) {
        super(buildMessage(actual, insertFirst(expected, expectedOr)));
    }

    private static SiType[] insertFirst(SiType first, SiType[] array) {
        SiType[] types = new SiType[array.length + 1];
        types[0] = first;
        System.arraycopy(array, 0, types, 1, array.length);
        return types;
    }

    private static String buildMessage(SiType actual, SiType... expected) {
        StringJoiner joiner = new StringJoiner("' or '", "'", "'");
        for (SiType siType : expected) {
            joiner.add(siType.name());
        }
        String string = joiner.toString();
        return "Expected type(s) %s, actual type %s".formatted(string, actual.name());
    }
}
