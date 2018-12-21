/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : jfinal_demo

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2018-12-21 17:37:59
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_build
-- ----------------------------
INSERT INTO `tb_build` VALUES ('1', '2', null, 'desc', null, '2018-11-30 18:10:39', null);
INSERT INTO `tb_build` VALUES ('2', '2', null, 'desc', null, '2018-12-06 17:16:51', null);
INSERT INTO `tb_build` VALUES ('3', '2', null, 'desc', null, '2018-12-06 17:18:38', null);
INSERT INTO `tb_build` VALUES ('4', '2', null, 'desc', null, '2018-12-06 17:19:16', null);
INSERT INTO `tb_build` VALUES ('5', '2', null, 'desc', null, '2018-12-06 17:26:20', null);
INSERT INTO `tb_build` VALUES ('6', '2', null, 'desc', null, '2018-12-06 17:28:58', null);
INSERT INTO `tb_build` VALUES ('7', '2', null, 'desc', null, '2018-12-06 17:30:03', null);
INSERT INTO `tb_build` VALUES ('8', '2', null, 'desc', null, '2018-12-06 17:30:42', null);
INSERT INTO `tb_build` VALUES ('9', '2', null, 'desc', null, '2018-12-06 17:31:06', null);
INSERT INTO `tb_build` VALUES ('10', '2', null, 'desc', null, '2018-12-07 14:20:35', null);
INSERT INTO `tb_build` VALUES ('11', '2', null, 'desc', null, '2018-12-07 14:21:05', null);
INSERT INTO `tb_build` VALUES ('12', '2', null, 'desc', null, '2018-12-07 14:22:28', null);
INSERT INTO `tb_build` VALUES ('13', '2', null, 'desc', null, '2018-12-07 14:23:28', null);
INSERT INTO `tb_build` VALUES ('14', '2', null, 'desc', null, '2018-12-07 14:24:22', null);
INSERT INTO `tb_build` VALUES ('15', '2', null, 'desc', null, '2018-12-07 14:25:20', null);
INSERT INTO `tb_build` VALUES ('16', '2', null, 'desc', null, '2018-12-07 14:28:23', null);
INSERT INTO `tb_build` VALUES ('17', '2', null, 'desc', null, '2018-12-07 14:31:51', null);
INSERT INTO `tb_build` VALUES ('18', '2', null, 'desc', null, '2018-12-07 14:33:08', null);
INSERT INTO `tb_build` VALUES ('19', '2', null, 'desc', null, '2018-12-07 14:33:51', null);
INSERT INTO `tb_build` VALUES ('20', '2', null, 'desc', null, '2018-12-07 14:34:23', null);
INSERT INTO `tb_build` VALUES ('21', '2', null, 'desc', null, '2018-12-07 14:34:50', null);
INSERT INTO `tb_build` VALUES ('22', '2', null, 'desc', null, '2018-12-07 14:39:12', null);
INSERT INTO `tb_build` VALUES ('23', '2', null, 'desc', null, '2018-12-07 14:43:24', null);
INSERT INTO `tb_build` VALUES ('24', '2', null, 'desc', null, '2018-12-07 14:44:06', null);
INSERT INTO `tb_build` VALUES ('25', '2', null, 'desc', null, '2018-12-07 14:44:48', null);
INSERT INTO `tb_build` VALUES ('26', '2', null, 'desc', null, '2018-12-07 14:46:28', null);
INSERT INTO `tb_build` VALUES ('27', '2', null, 'desc', null, '2018-12-07 14:47:00', null);
INSERT INTO `tb_build` VALUES ('28', '2', null, 'desc', null, '2018-12-07 14:47:28', null);
INSERT INTO `tb_build` VALUES ('29', '2', null, 'desc', null, '2018-12-07 14:48:39', null);
INSERT INTO `tb_build` VALUES ('30', '2', null, 'desc', null, '2018-12-07 14:49:13', null);
INSERT INTO `tb_build` VALUES ('31', '2', null, 'desc', null, '2018-12-07 15:12:40', null);
INSERT INTO `tb_build` VALUES ('32', '2', null, 'desc', null, '2018-12-07 15:14:15', null);
INSERT INTO `tb_build` VALUES ('33', '2', null, 'desc', null, '2018-12-07 15:15:51', null);
INSERT INTO `tb_build` VALUES ('34', '2', null, 'desc', null, '2018-12-07 15:19:50', null);
INSERT INTO `tb_build` VALUES ('35', '2', null, 'desc', null, '2018-12-07 15:26:59', null);
INSERT INTO `tb_build` VALUES ('36', '2', null, 'desc', null, '2018-12-07 15:27:02', null);

-- ----------------------------
-- Table structure for tb_env_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_env_config`;
CREATE TABLE `tb_env_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cGroup` varchar(200) NOT NULL COMMENT '分组',
  `cKey` varchar(50) NOT NULL COMMENT 'key',
  `cValue` varchar(50) DEFAULT NULL COMMENT '配置值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_env_config
-- ----------------------------
INSERT INTO `tb_env_config` VALUES ('1', 'common', 'HOME', 'E:\\ciHome');
INSERT INTO `tb_env_config` VALUES ('2', 'common', 'GIT', 'C:\\Program Files\\Git\\bin');

-- ----------------------------
-- Table structure for tb_project
-- ----------------------------
DROP TABLE IF EXISTS `tb_project`;
CREATE TABLE `tb_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `os` varchar(255) NOT NULL DEFAULT '' COMMENT 'linux windows',
  `type` int(11) DEFAULT 1 COMMENT '项目类型 1 web 2 rpc',
  `dutyPerson` varchar(255) DEFAULT NULL COMMENT '责任人',
  `accountId` int(11) DEFAULT NULL COMMENT '账户Id,用于git 检出鉴权',
  `scmPath` varchar(255) DEFAULT NULL COMMENT '版本控制地址',
  `scriptFilePath` varchar(255) DEFAULT NULL COMMENT '脚本执行路径',
  `script` text COMMENT '执行脚本',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_project
-- ----------------------------
INSERT INTO `tb_project` VALUES ('2', 'spring-mvc-chat_bak', 'linux', '2', 'he2', '2', 'gitee.com/githubsync/spring-mvc-chat.git', 'E:\\hs\\software\\jfinal-3.5_demo_for_maven\\MsMd', 'cmd.exe /c run.bat', '137111112', '2018-11-27 15:15:29', '2018-11-27 15:15:34');
INSERT INTO `tb_project` VALUES ('3', 'tomcatApp', 'windows', '2', 'he2', '1', 'gitee.com/githubsync/spring-mvc-chat.git', null, 'tomcat-------------', null, null, null);
INSERT INTO `tb_project` VALUES ('4', 'spring-mvc-chat', 'windows', '2', 'he2', '2', 'gitee.com/githubsync/spring-mvc-chat.git', 'run.bat', 'cd #(home??)\r\nset path=%path%;\"#(gitHome??)\"\r\n\r\nif exist #(projectName??) (\r\n    rem pause;\r\n    cd #(projectName??)\r\n	git clean -df\r\n	git reset --hard\r\n	git pull\r\n	echo \'back parentPath, otherWise not exist will repeat execute!!!\'\r\n    cd ..\r\n)\r\n\r\nif not exist #(projectName??) (\r\n    rem pause;\r\n	echo clone\r\n	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??)\r\n)\r\n\r\npause;', '13711111', null, null);

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

-- ----------------------------
-- Table structure for tb_template
-- ----------------------------
DROP TABLE IF EXISTS `tb_template`;
CREATE TABLE `tb_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `os` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '脚本名称',
  `script` text COMMENT '脚本内容',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_template
-- ----------------------------
INSERT INTO `tb_template` VALUES ('1', 'linux', 'springBoot-edas', 'edas:deploy', '2018-11-28 18:22:54', '2018-11-28 18:22:58');
INSERT INTO `tb_template` VALUES ('2', 'windows', 'tomcat', 'cd #(home??)\r\nset path=%path%;\"#(gitHome??)\"\r\n\r\nif exist #(projectName??) (\r\n    rem pause;\r\n    cd #(projectName??)\r\n	git clean -df\r\n	git reset --hard\r\n	git pull\r\n	echo \'back parentPath, otherWise not exist will repeat execute!!!\'\r\n    cd ..\r\n)\r\n\r\nif not exist #(projectName??) (\r\n    rem pause;\r\n	echo clone\r\n	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??)\r\n)\r\n\r\npause;', '2018-11-28 18:22:54', '2018-11-28 18:22:54');
