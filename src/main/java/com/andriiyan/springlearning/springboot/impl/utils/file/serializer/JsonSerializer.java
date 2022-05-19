package com.andriiyan.springlearning.springboot.impl.utils.file.serializer;

import com.andriiyan.springlearning.springboot.impl.utils.file.Serializer;
import com.google.gson.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Serializes/deserializes plain java objects by transforming them into the string.
 */
@Component
public class JsonSerializer implements Serializer {

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    @Override
    public <T> boolean serialize(@NonNull Collection<T> models, @NonNull OutputStream outputStream) {
        try {
            outputStream.write(gson.toJson(models).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public <T> Collection<T> deserialize(@NonNull InputStream inputStream, @NonNull Class<T> type) {
        final String json;
        try {
            json = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        JsonArray jsonElements = JsonParser.parseString(json).getAsJsonArray();
        List<T> models = new ArrayList<>();
        for (JsonElement element : jsonElements) {
            models.add(gson.fromJson(element, type));
        }
        return models;
    }

}
