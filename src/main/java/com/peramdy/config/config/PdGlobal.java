package com.peramdy.config.config;

import com.peramdy.config.utils.PdZkApi;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author peramdy on 2018/8/15.
 */
public class PdGlobal {

    private static ConcurrentHashMap cache = new ConcurrentHashMap();
    private static PdLoadProperties properties = new PdLoadProperties(new String[]{"cfg.properties", "config.properties", "application.properties"});


    public static String getConfig(String key) {
        String value = (String) cache.get(key);
        if (StringUtils.isBlank(value)) {
            value = properties.getProperty(key);
            cache.put(key, value);
        }
        return value;
    }

    public static String getConfig(String key, String defaultValue) {
        String value = (String) cache.get(key);
        if (StringUtils.isBlank(value)) {
            value = properties.getProperty(key, defaultValue);
            cache.put(key, value);
        }
        return value;
    }

    public static String getConfigCfg(String key, String defaultValue) {
        String cacheKey = key.replace(":", ".");
        String value = getConfig(cacheKey);
        if (StringUtils.isBlank(value)) {
            value = getConfigCfg(key);
            if (StringUtils.isBlank(value)) {
                value = defaultValue;
                cache.put(key, defaultValue);
            }
        }

        return value;
    }

    private static String getConfigCfg(String key) {
        String[] flag = new String[2];
        if (key.contains(":")) {
            flag = key.split(":");
        } else {
            flag[0] = "cfg";
            flag[1] = key;
        }
        String value = "";
        Map<String, String> cfgMap = PdZkApi.getPrivateCfgData(flag[0]);
        if (cfgMap != null && cfgMap.size() > 0) {
            value = cfgMap.get(flag[1]);
            if (StringUtils.isNotBlank(value)) {
                cache.put(key.replace(":", "."), value);
            }
        }
        return value;
    }


    public static String getConfigUnionCfg(String key, String defaultVal) {
        String cacheKey = key.replace(":", ".");
        String value = getConfig(cacheKey);
        if (StringUtils.isBlank(value)) {
            value = getConfigCfg(key);
            if (StringUtils.isBlank(value)) {
                String[] flag = key.split(":");
                Map<String, String> cfgMap = PdZkApi.getPublicCfgData(flag[0]);
                if (cfgMap != null && cfgMap.size() > 0) {
                    value = (String) cfgMap.get(flag[1]);
                    if (StringUtils.isBlank(value)) {
                        value = defaultVal;
                    }
                    cache.put(cacheKey, value);
                } else {
                    value = defaultVal;
                    cache.put(cacheKey, defaultVal);
                }
            }
        }

        return value;
    }

    public static String getConfigGlobalCfg(String key, String defaultVal) {
        String cacheKey = key.replace(":", ".");
        String value = getConfig(cacheKey);
        if (StringUtils.isBlank(value)) {
            String[] flag = key.split(":");
            Map<String, String> cfgMap = PdZkApi.getPublicCfgData(flag[0]);
            if (cfgMap != null && cfgMap.size() > 0) {
                value = (String) cfgMap.get(flag[1]);
                if (StringUtils.isBlank(value)) {
                    value = defaultVal;
                }
                cache.put(cacheKey, value);
            } else {
                value = defaultVal;
                cache.put(cacheKey, defaultVal);
            }
        }

        return value;
    }


    public static Boolean isTestMode() {
        String dm = getConfig("TestMode");
        return Boolean.valueOf("true".equals(dm) || "1".equals(dm));
    }

    public static Map<String, String> getAll() {
        return cache;
    }

    public static void addProperties(Map<String, String> data) {
        if (data != null) {
            cache.putAll(data);
        }
    }


}
