package com.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internal.InputFormat;

import java.io.File;
import java.io.IOException;

public final class InputReader {
    private static InputReader instance;
    private String source;

    private InputReader() {
    }

    /**
     * @return instance
     */
    public static InputReader getInstance() {
        if (instance == null) {
            instance = new InputReader();
        }
        return instance;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    /**
     * @return input representation
     * @throws IOException
     */
    public InputFormat read() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File inputFile = new File(source);
        return objectMapper.readValue(inputFile, InputFormat.class);
    }

}
