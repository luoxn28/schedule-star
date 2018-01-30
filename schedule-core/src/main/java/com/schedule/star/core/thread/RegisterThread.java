package com.schedule.star.core.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author xiangnan
 * @date 2018/1/30 22:39
 */
@Component
public class RegisterThread {
    private static final Logger logger = LogManager.getLogger();

    private boolean running = true;

    public void start() {

    }

    public void stop() {
        this.running = false;
    }
}
