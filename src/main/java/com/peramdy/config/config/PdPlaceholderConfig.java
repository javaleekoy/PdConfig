package com.peramdy.config.config;

import com.peramdy.config.PdConfig;
import com.peramdy.config.PdZkConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;
import java.util.Properties;

import static com.peramdy.config.constant.PdZkElement.*;

/**
 * @author peramdy on 2018/8/16.
 */
public class PdPlaceholderConfig extends PropertyPlaceholderConfigurer {

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);

    }


    private void fillCustomProperties(Properties properties) {
        Map<String, String> data = this.getData(properties);
        if (data != null) {
            this.fillProperties(properties, data);
        }
    }

    private void fillProperties(Properties props, Map<String, String> data) {
        if (data != null) {
            data.keySet().stream().forEach(key -> {
                String value = data.get(key);
                props.put(key, value);
            });
            PdGlobal.addProperties(data);
        }

    }

    private Map<String, String> getData(Properties properties) {
        String appPath = (String) properties.get(APPLICATION);
        PdConfig pdConfig = new PdZkConfig();
        return pdConfig.getConfig(appPath);
    }

}
