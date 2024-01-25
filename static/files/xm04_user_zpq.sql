/*
Navicat MySQL Data Transfer

Source Server         : www.ylxteach.net_3366
Source Server Version : 50151
Source Host           : www.ylxteach.net:3366
Source Database       : yjykfsj2023

Target Server Type    : MYSQL
Target Server Version : 50151
File Encoding         : 65001

Date: 2024-01-25 12:37:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for xm04_user_zpq
-- ----------------------------
DROP TABLE IF EXISTS `xm04_user_zpq`;
CREATE TABLE `xm04_user_zpq` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `password` varchar(255) DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
