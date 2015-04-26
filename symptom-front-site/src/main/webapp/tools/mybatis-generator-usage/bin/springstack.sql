/*
 Navicat Premium Data Transfer

 Source Server         : M
 Source Server Type    : MySQL
 Source Server Version : 50621
 Source Host           : localhost
 Source Database       : springstack

 Target Server Type    : MySQL
 Target Server Version : 50621
 File Encoding         : utf-8

 Date: 10/30/2014 16:36:28 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `task`
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `task`
-- ----------------------------
BEGIN;
INSERT INTO `task` VALUES ('1', null), ('3', 'task'), ('4', 'task'), ('5', 'task'), ('6', '33'), ('7', '11'), ('8', '11'), ('9', '11'), ('10', 'task'), ('11', 'task'), ('12', '690578'), ('13', null), ('14', 'yyy');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
