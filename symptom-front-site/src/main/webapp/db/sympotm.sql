/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : sympotm

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2015-05-30 15:34:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_syndrome`
-- ----------------------------
DROP TABLE IF EXISTS `t_syndrome`;
CREATE TABLE `t_syndrome` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `symptom_category` varchar(30) DEFAULT NULL,
  `symptom_name` varchar(100) NOT NULL,
  `description` varchar(5000) NOT NULL,
  `syndrome_element_start` varchar(100) NOT NULL,
  `syndrome_element_end` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_syndrome
-- ----------------------------

-- ----------------------------
-- Table structure for `t_syndrome_element`
-- ----------------------------
DROP TABLE IF EXISTS `t_syndrome_element`;
CREATE TABLE `t_syndrome_element` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `syndrome_element_start` varchar(100) NOT NULL,
  `syndrome_element_end` varchar(100) NOT NULL,
  `relate_type` tinyint(2) NOT NULL DEFAULT '1',
  `is_relate` tinyint(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_syndrome_element
-- ----------------------------
