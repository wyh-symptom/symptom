/*
Navicat MySQL Data Transfer

Source Server         : Mysql-local
Source Server Version : 50532
Source Host           : localhost:3306
Source Database       : sympotm

Target Server Type    : MYSQL
Target Server Version : 50532
File Encoding         : 65001

Date: 2015-04-23 20:33:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_syndrome
-- ----------------------------
DROP TABLE IF EXISTS `t_syndrome`;
CREATE TABLE `t_syndrome` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `symptom_name` varchar(100) NOT NULL,
  `description` varchar(5000) NOT NULL,
  `syndrome_element_start` varchar(100) NOT NULL,
  `syndrome_element_end` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_syndrome_element
-- ----------------------------
DROP TABLE IF EXISTS `t_syndrome_element`;
CREATE TABLE `t_syndrome_element` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `syndrome_element_start` varchar(100) NOT NULL,
  `syndrome_element_end` varchar(100) NOT NULL,
  `is_relate` tinyint(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
