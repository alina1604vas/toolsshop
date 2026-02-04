package org.example.tools.utils;

import java.nio.file.Path;
import java.util.Properties;

public class Credentials {

    private final Properties props;

    public Credentials() {
        this(new PropertiesFileReader(), Path.of("credentials.properties"));
    }

    public Credentials(PropertiesFileReader reader, Path path) {
        this.props = reader.load(path);
    }

    public String email() {
        return firstNonNull(System.getenv("TEST_USER_EMAIL"), props.getProperty("test.user.email"));
    }

    public String password() {
        return firstNonNull(System.getenv("TEST_USER_PASSWORD"), props.getProperty("test.user.password"));
    }

    private static String firstNonNull(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        throw new IllegalStateException("Missing test user credentials");
    }
}
