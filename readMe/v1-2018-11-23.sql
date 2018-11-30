/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : jfinal_demo

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2018-11-30 17:52:32
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
INSERT INTO `blog` VALUES ('1', 'JFinal Demo Title here2222', 'JFinal Demo Content here121');
INSERT INTO `blog` VALUES ('2', 'test 1', 'test 1');
INSERT INTO `blog` VALUES ('3', 'test 2', 'test 2');
INSERT INTO `blog` VALUES ('4', 'test 3', 'test 3');
INSERT INTO `blog` VALUES ('5', 'test 4', 'test 4');
INSERT INTO `blog` VALUES ('6', '111', '2222');

-- ----------------------------
-- Table structure for tb_account
-- ----------------------------
DROP TABLE IF EXISTS `tb_account`;
CREATE TABLE `tb_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL COMMENT '描述',
  `userName` varchar(50) NOT NULL COMMENT '用户名',
  `pwd` varchar(50) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_account
-- ----------------------------
INSERT INTO `tb_account` VALUES ('1', 'gitee', 'ss', 'ss');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `accountId` int(11) DEFAULT NULL COMMENT '账户Id,用于git 检出鉴权',
  `scmPath` varchar(255) DEFAULT NULL COMMENT '版本控制地址',
  `scriptFilePath` varchar(255) DEFAULT NULL COMMENT '脚本执行路径',
  `script` text COMMENT '执行脚本',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_project
-- ----------------------------
INSERT INTO `tb_project` VALUES ('2', 'wx2333', '2', 'he2', '1', null, null, 'cmd.exe /c cd %projectHome%\r\ncmd.exe /c mvn clean install -DskipTests', '137111112', '2018-11-27 15:15:29', '2018-11-27 15:15:34');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_step
-- ----------------------------
INSERT INTO `tb_step` VALUES ('1', '2', '1', '1', '1', '2', '121221', '2018-11-28 18:22:49', '2018-11-28 18:22:51', '2018-11-28 18:22:54', '2018-11-28 18:22:58');
