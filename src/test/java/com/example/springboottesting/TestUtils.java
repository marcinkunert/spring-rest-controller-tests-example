package com.example.springboottesting;

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TestUtils {
    public static String resourceAsString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String resourceAsString(String name) {
        InputStream resourceAsStream = TestUtils.class.getClassLoader().getResourceAsStream(name);
        if (resourceAsStream == null) {
            throw new RuntimeException("resource " + name + " not found");
        }
        try (Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
