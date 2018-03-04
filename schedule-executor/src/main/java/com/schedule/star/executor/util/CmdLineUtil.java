package com.schedule.star.executor.util;

import org.apache.commons.cli.*;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author xiangnan
 * @date 2018/3/4 19:14
 */
public class CmdLineUtil {

    public static final String h = "h";
    public static final String p = "p";
    public static final String r = "r";
    public static final String c = "c";
    public static final String help = "help";

    /**
     * 命令行提示
     */
    public static Options buildCommandlineOptions(final Options options) {
        Option opt = new Option(help, "help", false, "Print help");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option(h, "ip", true, "Localhost Ip, eg: 192.168.1.110");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option(p, "port", true, "Port, eg: 9999");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option(r, "register", true,
                "Register url list, eg: http://1.1.1.1:8080/api/register,http://2.2.2.2:8080/api/register");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option(c, "configFile", true, "Executor config properties file");
        opt.setRequired(false);
        options.addOption(opt);

        return options;
    }

    private static String appName;
    private static Options options;

    public static CommandLine parseCmdLine(String appName, String[] args, Options options, CommandLineParser parser) {
        CmdLineUtil.appName = appName;
        CmdLineUtil.options = options;

        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption("help")) {
                hf.printHelp(appName, options, true);
                System.exit(0);
                return null;
            }
        } catch (ParseException e) {
            hf.printHelp(appName, options, true);
        }

        return commandLine;
    }

    public static void printHelpInfo() {
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);

        hf.printHelp(appName, options, true);
    }

    public static void properties2Object(final Properties p, final Object object) {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            String mn = method.getName();
            if (mn.startsWith("set")) {
                try {
                    String tmp = mn.substring(4);
                    String first = mn.substring(3, 4);

                    String key = first.toLowerCase() + tmp;
                    String property = p.getProperty(key);
                    if (property != null) {
                        Class<?>[] pt = method.getParameterTypes();
                        if (pt != null && pt.length > 0) {
                            String cn = pt[0].getSimpleName();
                            Object arg = null;
                            if (cn.equals("int") || cn.equals("Integer")) {
                                arg = Integer.parseInt(property);
                            } else if (cn.equals("long") || cn.equals("Long")) {
                                arg = Long.parseLong(property);
                            } else if (cn.equals("double") || cn.equals("Double")) {
                                arg = Double.parseDouble(property);
                            } else if (cn.equals("boolean") || cn.equals("Boolean")) {
                                arg = Boolean.parseBoolean(property);
                            } else if (cn.equals("float") || cn.equals("Float")) {
                                arg = Float.parseFloat(property);
                            } else if (cn.equals("String")) {
                                arg = property;
                            } else {
                                continue;
                            }
                            method.invoke(object, arg);
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
        }
    }

}
