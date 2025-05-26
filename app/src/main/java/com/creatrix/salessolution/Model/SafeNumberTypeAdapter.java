package com.creatrix.salessolution.Model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class SafeNumberTypeAdapter extends TypeAdapter<Number> {
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
    }

    @Override
    public Number read(JsonReader in) throws IOException {
        String value = in.nextString();
        try {
            if (value.isEmpty()) {
                return 0; // or any default value
            } else {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            return 0; // or any default value
        }
    }
}