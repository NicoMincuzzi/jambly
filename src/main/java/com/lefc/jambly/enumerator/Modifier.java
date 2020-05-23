package com.lefc.jambly.enumerator;

enum Modifier {
    PRIVATE("private"),
    PROTECTED("protected"),
    PUBLIC("public");

    private final String modifier;

    Modifier(String modifier) {
        this.modifier = modifier;
    }

    public static String get(Modifier modifier) {
        return modifier.modifier;
    }
}
