package com.peramdy.config;

import java.util.Map;

/**
 * @author peramdy on 2018/8/16.
 */
public interface PdConfig {

    Map<String, String> getConfig(String path);

}
