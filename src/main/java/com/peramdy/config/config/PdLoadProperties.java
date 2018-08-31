package com.peramdy.config.config;

import com.peramdy.config.execption.PdException;
import com.peramdy.config.execption.PdIOException;
import com.peramdy.config.execption.PdNoSuchElementException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author pd
 */
public class PdLoadProperties {

    private final Properties properties;

    public PdLoadProperties(String... resourcePaths) {
        this.properties = this.loadProperties(resourcePaths);
    }

    public Properties getProperties() {
        return properties;
    }


    /**
     * get system property
     *
     * @param key
     * @return
     */
    private String getValue(String key) {
        if (StringUtils.isNotBlank(key)) {
            String value = System.getProperty(key);
            value = value != null ? value : (this.properties.containsKey(key) ? (String) this.properties.get(key) : "");
            return value;
        } else {
            throw new PdNoSuchElementException("key is not null !");
        }
    }


    /**
     * get properties
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String value = this.getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        } else {
            throw new PdNoSuchElementException("not find key :" + key + " to value !");
        }
    }

    /**
     * get property (defaultValue)
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        String value = this.getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        } else {
            return defaultValue;
        }
    }

    /**
     * get Integer value
     *
     * @param key
     * @return
     */
    public Integer getInteger(String key) {
        String value = this.getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return Integer.parseInt(value);
        } else {
            throw new PdNoSuchElementException("not find key :" + key + " to value !");
        }
    }

    /**
     * get Integer value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Integer getInteger(String key, Integer defaultValue) {
        String value = this.getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return Integer.getInteger(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * get Long value
     *
     * @param key
     * @return
     */
    public Long getLong(String key) {
        String value = this.getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return Long.parseLong(value);
        } else {
            throw new PdNoSuchElementException("not find key :" + key + " to value !");
        }
    }

    /**
     * get Long value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Long getLong(String key, Long defaultValue) {
        String value = this.getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return Long.parseLong(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * get Boolean value
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        String value = this.getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return Boolean.parseBoolean(value);
        } else {
            throw new PdNoSuchElementException("not find key :" + key + " to value !");
        }
    }


    /**
     * get Boolean value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = this.getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return Boolean.parseBoolean(value);
        } else {
            return defaultValue;
        }
    }


    /**
     * load resourcePaths
     *
     * @param resourcePaths
     * @return
     */
    private Properties loadProperties(String... resourcePaths) {
        Properties properties = new Properties();
        String[] paths = resourcePaths;
        Arrays.stream(paths).forEach(path -> {
            InputStreamReader is = null;
            try {
                try {
                    is = new InputStreamReader(this.getClass().getResourceAsStream(path));
                } catch (Exception e) {
                    is = null;
                    throw new PdException("load file error ! ", e);
                }
                if (is != null) {
                    properties.load(is);
                }
            } catch (IOException e) {
                throw new PdIOException("Could not load properties form path:" + path, e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        throw new PdIOException(e);
                    }
                }
            }
        });
        return properties;
    }


}
