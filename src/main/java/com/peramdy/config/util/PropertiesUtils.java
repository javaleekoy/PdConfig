package com.peramdy.config.util;

import com.peramdy.config.execption.PdException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

/**
 * @author pd
 */
public class PropertiesUtils {

    public static Properties getProperties(String fileName) {
        Properties properties = null;
        try {
            PropertiesUtils propertiesUtils = new PropertiesUtils();
            Class cla = propertiesUtils.getClass();
            ClassLoader cl = cla.getClassLoader();
            URL url = cl.getResource(fileName);
            URI uri = url.toURI();
            Path path = Paths.get(uri);
            File file = path.toFile();
            FileReader fr = new FileReader(file.getPath());
            properties = new Properties();
            properties.load(fr);
            Set<String> keys = properties.stringPropertyNames();
            if (keys == null || keys.size() == 0) {
                throw new PdException("[{" + fileName + "}] is empty", new IllegalArgumentException());
            }
            fr.close();
        } catch (IOException e) {
            throw new PdException(fileName + " load error", e);
        } catch (URISyntaxException e) {
            throw new PdException(fileName + " uri error", e);
        }
        return properties;
    }


    public Properties getAllProperties(String fileName) {
        Properties properties = null;
        try {
            Class cla = this.getClass();
            ClassLoader cl = cla.getClassLoader();
            URL url = cl.getResource(fileName);
            URI uri = url.toURI();
            Path path = Paths.get(uri);
            File file = path.toFile();
            FileReader fr = new FileReader(file.getPath());
            properties = new Properties();
            properties.load(fr);
            Set<String> keys = properties.stringPropertyNames();
            if (keys == null || keys.size() == 0) {
                throw new PdException("[{" + fileName + "}] is empty", new IllegalArgumentException());
            }
            fr.close();
        } catch (IOException e) {
            throw new PdException(fileName + " load error", e);
        } catch (URISyntaxException e) {
            throw new PdException(fileName + " uri error", e);
        }
        return properties;
    }

    public static void main(String[] args) {
        PropertiesUtils propertiesUtils = new PropertiesUtils();
        propertiesUtils.getAllProperties("config.properties");
    }

}
