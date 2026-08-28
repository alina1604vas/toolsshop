package org.example.tools;

public class SystemConfig {

    /**
     * Base URL of the application under test.
     *
     * Override for CI (or any environment) via the BASE_URL environment variable, or the
     * -DbaseUrl system property, e.g. BASE_URL=http://localhost:4200/ when the app is
     * self-hosted in Docker. Falls back to the public demo site when nothing is set, so
     * local runs behave exactly as before.
     *
     * Always returns a value ending with "/" - callers append paths like "checkout".
     */
    public static String getBaseUrl() {
        String url = System.getProperty("baseUrl");
        if (url == null || url.isBlank()) {
            url = System.getenv("BASE_URL");
        }
        if (url == null || url.isBlank()) {
            url = "https://practicesoftwaretesting.com/";
        }
        return url.endsWith("/") ? url : url + "/";
    }

}
