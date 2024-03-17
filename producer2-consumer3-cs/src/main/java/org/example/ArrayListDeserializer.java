package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.ArrayList;

public class ArrayListDeserializer<T> implements Deserializer<ArrayList<T>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ArrayList<T> deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, new TypeReference<ArrayList<T>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

