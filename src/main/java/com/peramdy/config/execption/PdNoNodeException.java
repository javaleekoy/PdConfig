package com.peramdy.config.execption;

import org.apache.zookeeper.KeeperException;

/**
 * @author peramdy on 2018/8/31.
 */
public class PdNoNodeException extends KeeperException.NodeExistsException {

    public PdNoNodeException() {
        super();
    }

    public PdNoNodeException(String path) {
        super(path);
    }
}
