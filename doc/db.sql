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
