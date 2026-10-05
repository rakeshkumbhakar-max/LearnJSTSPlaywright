package com.qa.salesforce.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.salesforce.exceptions.FrameworkException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static JsonNode readTree(String classpathResource) {
        try (InputStream inputStream = openStream(classpathResource)) {
            return MAPPER.readTree(inputStream);
        } catch (IOException e) {
            throw new FrameworkException("Failed to read JSON resource: " + classpathResource, e);
        }
    }

    public static <T> T read(String classpathResource, Class<T> type) {
        try (InputStream inputStream = openStream(classpathResource)) {
            return MAPPER.readValue(inputStream, type);
        } catch (IOException e) {
            throw new FrameworkException("Failed to deserialize JSON resource: " + classpathResource, e);
        }
    }

    public static Object[][] toDataProvider(String classpathResource, String... fields) {
        JsonNode root = readTree(classpathResource);
        if (!root.isArray()) {
            throw new FrameworkException("JSON data provider source must be an array: " + classpathResource);
        }
        List<Object[]> rows = new ArrayList<>();
        for (JsonNode node : root) {
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                JsonNode value = node.get(fields[i]);
                row[i] = (value == null || value.isNull()) ? null : value.asText();
            }
            rows.add(row);
        }
        return rows.toArray(new Object[0][]);
    }

    private static InputStream openStream(String classpathResource) {
        InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(classpathResource);
        if (inputStream == null) {
            throw new FrameworkException("JSON resource not found on classpath: " + classpathResource);
        }
        return inputStream;
    }
}
