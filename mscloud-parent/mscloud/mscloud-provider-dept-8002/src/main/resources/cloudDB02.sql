/*
 Navicat Premium Data Transfer

 Source Server         : 123.57.18.177
 Source Server Type    : MySQL
 Source Server Version : 50515
 Source Host           : 123.57.18.177:38141
 Source Schema         : cloudDB02

 Target Server Type    : MySQL
 Target Server Version : 50515
 File Encoding         : 65001

 Date: 15/12/2018 21:53:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
  `deptno` bigint(20) NOT NULL AUTO_INCREMENT,
  `dname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `db_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`deptno`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES (1, '开发部', 'clouddb02');
INSERT INTO `dept` VALUES (2, '财务部', 'cloudDB02');
INSERT INTO `dept` VALUES (3, '销售部', 'cloudDB02');

SET FOREIGN_KEY_CHECKS = 1;
