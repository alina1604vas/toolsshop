package org.example.tools;

public class SystemConfig {

    public static int getSelectedSprint() {
        return Integer.parseInt(System.getProperty("selectedSprint", "0"));
    }

    public static String getBaseUrl() {
        return System.getProperty("baseUrl", "https://practicesoftwaretesting.com/#");
    }

}
