package com.peramdy.config.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * @author pd
 */
public enum PdEnv {
    LOCAL(1, "local"),
    DEV(2, "dev"),
    TEST(3, "test"),
    PRO(4, "pro");

    private int id;
    private String env;

    PdEnv(int id, String env) {
        this.id = id;
        this.env = env;
    }

    public int getId() {
        return id;
    }

    public String getEnv() {
        return env;
    }

    public static boolean isContainEnv(String env) {
        if (StringUtils.isNotBlank(env)) {
            for (PdEnv pdEnv : PdEnv.values()) {
                if (pdEnv.getEnv().equals(env.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}
