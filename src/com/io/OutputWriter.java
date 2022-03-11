package com.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.internal.InternalData;

import java.io.File;
import java.io.IOException;

public final class OutputWriter {
    private static OutputWriter instance;
    private String destination;
    private InternalData dataToWrite;

    /**
     * @return instance
     */
    public static OutputWriter getInstance() {
        if (instance == null) {
            instance = new OutputWriter();
        }

        return instance;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public void setDataToWrite(final InternalData dataToWrite) {
        this.dataToWrite = dataToWrite;
    }

    /**
     * writes to the output file
     *
     * @throws IOException
     */
    public void write() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File outputFile = new File(destination);

        objectMapper.writeValue(outputFile, dataToWrite);
    }
}
