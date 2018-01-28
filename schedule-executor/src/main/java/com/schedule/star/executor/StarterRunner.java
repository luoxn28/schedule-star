package com.schedule.star.executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiangnan
 * @date 2018/1/28 15:17
 */
@Configuration
public class StarterRunner implements CommandLineRunner {

    private final Logger logger = LogManager.getLogger();

    @Override
    public void run(String... strings) throws Exception {
        logger.info("schedule-star start success");
    }

}
