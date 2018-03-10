package com.schedule.star.executor;

import cn.hutool.core.convert.Convert;
import cn.hutool.setting.dialect.Props;
import com.schedule.star.core.executor.ExecutorStarter;
import com.schedule.star.core.executor.ExecutorConfig;
import com.schedule.star.executor.util.CmdLineUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

/**
 * @author xiangnan
 * @date 2018/1/28 15:15
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.schedule.star.core"})
public class ExecutorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExecutorApplication.class, args);
    }

    private final Logger logger = LogManager.getLogger();

    @Resource
    private ExecutorStarter starter;

    @Override
    public void run(String[] args) throws Exception {

        try {
            Options options = CmdLineUtil.buildCommandlineOptions(new Options());
            CommandLine commandLine = CmdLineUtil.parseCmdLine("java -jar schedule-executor.jar", args, options, new PosixParser());
            if (null == commandLine) {
                System.exit(-1);
                return;
            }

            final ExecutorConfig config = new ExecutorConfig();
            if (commandLine.hasOption(CmdLineUtil.c)) {
                String file = commandLine.getOptionValue(CmdLineUtil.c);
                if (file != null) {
                    CmdLineUtil.properties2Object(new Props(file), config);
                    config.setConfigPath(file);

                    logger.info("load config properties file OK, {}", file);
                }
            }

            if (commandLine.hasOption(CmdLineUtil.h)) {
                config.setIp(commandLine.getOptionValue(CmdLineUtil.h));
            }
            if (commandLine.hasOption(CmdLineUtil.p)) {
                config.setPort(Convert.toInt(commandLine.getOptionValue(CmdLineUtil.p)));
            }
            if (commandLine.hasOption(CmdLineUtil.r)) {
                config.setRegisterUrls(commandLine.getOptionValue(CmdLineUtil.r));
            }

            if (!config.valid()) {
                logger.info("executor config invalid");
                CmdLineUtil.printHelpInfo();
                System.exit(-1);
                return;
            }

            starter.run(config);
            logger.info("schedule-star start success");
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            logger.info("schedule-star shutdown, bye bye");
        }

    }

}
