package com.quotemedia.qaassignment.denhalych.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertyReader {

    private static final String PROP_FILE_PATH;

    static {
        URL url = PropertyReader.class.getClassLoader().getResource("app.properties");
        if (url == null) {
            throw new IllegalArgumentException("Properties file not found!");
        } else {
            PROP_FILE_PATH = url.getPath();
        }
    }


    public static String getProperty(String property) {
        Properties appProp = new Properties();
        try {
            appProp.load(new FileInputStream(PROP_FILE_PATH));
            return appProp.getProperty(property);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read property");
        }
    }


}
