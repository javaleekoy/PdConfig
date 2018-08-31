package com.peramdy.config;

import com.google.common.collect.Maps;
import com.peramdy.config.config.PdGlobal;
import com.peramdy.config.execption.PdException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Map;

import static com.peramdy.config.constant.PdZkElement.LOCAL_CONFIG_PATH;

/**
 * @author peramdy on 2018/8/31.
 */
public class PdZkConfig implements PdConfig {

    @Override
    public Map<String, String> getConfig(String path) {
        CuratorFramework client = PdZkFactory.get();
        if (!exists(client, path)) {
            throw new PdException("Path : " + path + " does not exists.");
        }
        try {
            List<String> list = client.getChildren().forPath(path);
            if (list != null && list.size() > 0) {
                Map<String, String> map = Maps.newHashMap();
                String bashPath = PdGlobal.getConfig(LOCAL_CONFIG_PATH);
                list.stream().forEach(c -> {
                    try {
                        String data = new String(client.getData().forPath(bashPath + "/" + c));
                        map.put(c, data);
                    } catch (Exception e) {
                        throw new PdException(e);
                    }
                });
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean exists(CuratorFramework client, String path) {
        Stat stat;
        try {
            stat = client.checkExists().forPath(path);
        } catch (Exception e) {
            throw new PdException(e);
        }
        return stat != null;
    }
}
