/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50532
Source Host           : localhost:3306
Source Database       : sympotm

Target Server Type    : MYSQL
Target Server Version : 50532
File Encoding         : 65001

Date: 2015-06-07 20:13:03
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;


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
  `description` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

