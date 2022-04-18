package com.github.pedroluiznogueira.testingapi.support;

import java.util.Random;

public class StringSupport {

    public static String generate() {
        Random random = new Random();
        return random.ints(0, 100)
                .limit(3)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
