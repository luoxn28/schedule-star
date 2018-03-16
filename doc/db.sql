/*
Navicat MySQL Data Transfer

Source Server         : 192.168.6.150
Source Server Version : 50637
Source Host           : 192.168.6.150:3306
Source Database       : schedule_star

Target Server Type    : MYSQL
Target Server Version : 50637
File Encoding         : 65001

Date: 2018-01-31 22:02:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for executor
-- ----------------------------
DROP TABLE IF EXISTS `executor`;
CREATE TABLE `executor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `executor_id` varchar(64) NOT NULL COMMENT '业务ID',
  `ip` varchar(64) NOT NULL COMMENT 'IP地址',
  `port` int(11) NOT NULL COMMENT '端口号',
  `name` varchar(255) DEFAULT '' COMMENT '执行器名字',
  `token` varchar(64) NOT NULL COMMENT 'token 用于调度中心与执行器之间的校验',
  `status` varchar(16) NOT NULL COMMENT '执行器状态',
  `keep_alive_time` int(11) NOT NULL COMMENT '保活时间',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `executor_id` (`executor_id`) USING BTREE,
  UNIQUE KEY `ip:port` (`ip`,`port`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` varchar(32) NOT NULL,
  `group_id` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `type` varchar(16) NOT NULL,
  `cron` varchar(128) DEFAULT NULL,
  `params` varchar(256) DEFAULT NULL,
  `router_strategy` varchar(36) DEFAULT NULL,
  `fail_strategy` varchar(36) DEFAULT NULL,
  `email_phone` varchar(36) DEFAULT NULL,
  `script_loc` varchar(256) DEFAULT NULL,
  `class_name` varchar(128) DEFAULT NULL,
  `param_creator` varchar(36) DEFAULT NULL COMMENT '生成器 标签',
  `param_raw_value` varchar(512) DEFAULT NULL COMMENT '动态参数',
  `child_jobs` text COMMENT '子任务列表，已","分隔',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `status` varchar(16) NOT NULL,
  PRIMARY KEY (`id`,`job_id`),
  UNIQUE KEY `job_id` (`job_id`) USING BTREE,
  KEY `job_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL,
  `group_id` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `status` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_id` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for link_group_executor
-- ----------------------------
DROP TABLE IF EXISTS `link_group_executor`;
CREATE TABLE `link_group_executor` (
  `group_id` varchar(64) NOT NULL,
  `executor_id` varchar(64) NOT NULL,
  PRIMARY KEY (`group_id`),
  KEY `group_id` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `SCHEDULE_JOB_QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `SCHEDULE_JOB_QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_CALENDARS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_CRON_TRIGGERS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `SCHEDULE_JOB_QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `SCHEDULE_JOB_QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_JOB_DETAILS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_LOCKS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_SCHEDULER_STATE`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `SCHEDULE_JOB_QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `SCHEDULE_JOB_QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `SCHEDULE_JOB_QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `SCHEDULE_JOB_QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for SCHEDULE_JOB_QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `SCHEDULE_JOB_QRTZ_TRIGGERS`;
CREATE TABLE `SCHEDULE_JOB_QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `SCHEDULE_JOB_QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `SCHEDULE_JOB_QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
