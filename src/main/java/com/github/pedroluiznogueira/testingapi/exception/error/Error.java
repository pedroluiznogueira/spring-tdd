package com.github.pedroluiznogueira.testingapi.exception.error;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class Error {
    Date timestamp;
    String message;
    String detail;
}

