/*
Navicat MySQL Data Transfer

Source Server         : 43.139.101.228_3306
Source Server Version : 80035
Source Host           : 43.139.101.228:3306
Source Database       : projects_yjykfsj2023

Target Server Type    : MYSQL
Target Server Version : 80035
File Encoding         : 65001

Date: 2024-01-18 18:59:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL,
  `file_size` bigint NOT NULL,
  `upload_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `course_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
