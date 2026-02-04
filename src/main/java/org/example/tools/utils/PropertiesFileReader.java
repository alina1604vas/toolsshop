package org.example.tools.utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesFileReader {

    public Properties load(Path path) {
        Properties props = new Properties();
        if (Files.exists(path)) {
            try (InputStream is = Files.newInputStream(path)) {
                props.load(is);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load " + path, e);
            }
        }
        return props;
    }
}
