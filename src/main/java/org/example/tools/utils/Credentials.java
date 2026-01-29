package org.example.tools.utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Credentials {
    private static final Properties PROPS = new Properties();

    static {
        Path path = Path.of("credentials.properties");

        if (Files.exists(path)) {
            try (InputStream is = Files.newInputStream(path)) {
                PROPS.load(is);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load credentials.properties", e);
            }
        }
    }

    private Credentials() {}

    public static String email() {
        return firstNonNull(
                System.getenv("TEST_USER_EMAIL"),
                PROPS.getProperty("test.user.email")
        );
    }

    public static String password() {
        return firstNonNull(
                System.getenv("TEST_USER_PASSWORD"),
                PROPS.getProperty("test.user.password")
        );
    }

    private static String firstNonNull(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        throw new IllegalStateException("Missing test user credentials");
    }
}
