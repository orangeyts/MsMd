/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : jfinal_demo

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2018-11-23 17:53:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES ('1', 'JFinal Demo Title here', 'JFinal Demo Content here');
INSERT INTO `blog` VALUES ('2', 'test 1', 'test 1');
INSERT INTO `blog` VALUES ('3', 'test 2', 'test 2');
INSERT INTO `blog` VALUES ('4', 'test 3', 'test 3');
INSERT INTO `blog` VALUES ('5', 'test 4', 'test 4');
INSERT INTO `blog` VALUES ('6', '111', '2222');

-- ----------------------------
-- Table structure for tb_build
-- ----------------------------
DROP TABLE IF EXISTS `tb_build`;
CREATE TABLE `tb_build` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL COMMENT '项目类型 1 web 2 rpc',
  `triggerPerson` varchar(255) DEFAULT NULL COMMENT '触发人',
  `triggerDesc` varchar(200) NOT NULL COMMENT '构建描述',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_build
-- ----------------------------

-- ----------------------------
-- Table structure for tb_project
-- ----------------------------
DROP TABLE IF EXISTS `tb_project`;
CREATE TABLE `tb_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `type` int(11) NOT NULL COMMENT '项目类型 1 web 2 rpc',
  `dutyPerson` varchar(255) DEFAULT NULL COMMENT '责任人',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_project
-- ----------------------------

-- ----------------------------
-- Table structure for tb_step
-- ----------------------------
DROP TABLE IF EXISTS `tb_step`;
CREATE TABLE `tb_step` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL COMMENT '项目id',
  `buildId` int(11) DEFAULT NULL COMMENT '构建Id',
  `previousStepId` int(11) DEFAULT NULL COMMENT '上一个步骤id',
  `nextStepId` int(11) DEFAULT NULL COMMENT '下一个步骤id,没有为null',
  `order` int(11) NOT NULL COMMENT '步骤排序',
  `outPut` varchar(255) DEFAULT NULL COMMENT '执行输出',
  `startTime` datetime DEFAULT NULL COMMENT '责任人',
  `endTime` datetime DEFAULT NULL COMMENT '手机号',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_step
-- ----------------------------
