SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `tb_account`;
CREATE TABLE `tb_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(200) NOT NULL COMMENT '类型-linux git 等',
  `userName` varchar(50) NOT NULL COMMENT '用户名',
  `pwd` varchar(50) DEFAULT NULL COMMENT '密码',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='系统一些配置的账户 git, ssh 等';

insert into `tb_account`(`id`,`type`,`userName`,`pwd`,`ip`) values
(1,'gitee','root','sssss',null),
(2,'2','githubsync','githubsync1',null),
(3,'测试服务器','root','p4h','110.77.2.1');
DROP TABLE IF EXISTS  `tb_build`;
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

DROP TABLE IF EXISTS  `tb_env_config`;
CREATE TABLE `tb_env_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cGroup` varchar(200) NOT NULL COMMENT '分组',
  `cKey` varchar(50) NOT NULL COMMENT 'key',
  `cValue` varchar(50) DEFAULT NULL COMMENT '配置值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into `tb_env_config`(`id`,`cGroup`,`cKey`,`cValue`) values
(1,'common','HOME','d:\\cihome'),
(2,'common','GIT','C:\\Program Files\\Git\\bin');
DROP TABLE IF EXISTS  `tb_project`;
CREATE TABLE `tb_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `os` varchar(255) NOT NULL DEFAULT '' COMMENT 'linux windows',
  `type` int(11) DEFAULT '1' COMMENT '项目类型 1 web 2 rpc',
  `dutyPerson` varchar(255) DEFAULT NULL COMMENT '责任人',
  `sshAccountId` int(11) DEFAULT '0' COMMENT 'ssh 账户id',
  `accountId` int(11) DEFAULT NULL COMMENT '账户Id,用于git 检出鉴权',
  `scmPath` varchar(255) DEFAULT NULL COMMENT '版本控制地址',
  `scriptFilePath` varchar(255) DEFAULT NULL COMMENT '脚本执行路径',
  `sshScript` varchar(5000) DEFAULT NULL COMMENT 'ssh 脚本',
  `script` text COMMENT '执行脚本',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

insert into `tb_project`(`id`,`title`,`os`,`type`,`dutyPerson`,`sshAccountId`,`accountId`,`scmPath`,`scriptFilePath`,`sshScript`,`script`,`phone`,`createTime`,`updateTime`) values
(2,'spring-mvc-chat_bak','linux',2,'he2',0,2,'gitee.com/githubsync/spring-mvc-chat.git','E:\\hs\\software\\jfinal-3.5_demo_for_maven\\MsMd',null,'cmd.exe /c run.bat','137111112','2018-11-27 15:15:29','2018-11-27 15:15:34'),
(3,'tomcatApp','windows',2,'he2',0,1,'gitee.com/githubsync/spring-mvc-chat.git',null,null,'tomcat-------------',null,null,null),
(4,'spring-mvc-chat','linux',1,'he2',0,2,'gitee.com/githubsync/spring-mvc-chat.git','run.bat',null,'#!/bin/sh

## mvn home
export MAVEN_HOME=/mnt/cihome/apache-maven-3.5.4
export PATH=$PATH:$MAVEN_HOME/bin

cd #(home??)
if [ -d #(projectName??) ]
then
    rem pause;
    cd #(projectName??)
	git clean -df
	git reset --hard
	git pull
	echo \'back parentPath, otherWise not exist will repeat execute!!!\'
    cd ..
fi

if [ ! -d #(projectName??) ]
then 
	echo clone
	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??)
fi
cd #(projectName??)
mvn clean install -DskipTests','13711111',null,'2019-01-07 14:19:07'),
(5,'nuo','linux',1,'he2',0,1,'gitee.com/didano/nuo.git','run.bat',null,'#!/bin/sh

## mvn home
export MAVEN_HOME=/mnt/cihome/apache-maven-3.5.4
export PATH=$PATH:$MAVEN_HOME/bin

cd #(home??)
if [ -d #(projectName??) ]
then
    rem pause;
    cd #(projectName??)
	git clean -df
	git reset --hard
	git pull
	echo \'back parentPath, otherWise not exist will repeat execute!!!\'
    cd ..
fi

if [ ! -d #(projectName??) ]
then 
	echo clone
	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??)
fi
cd #(projectName??)
mvn clean install -DskipTests','13711111',null,'2019-02-18 14:30:06');
DROP TABLE IF EXISTS  `tb_step`;
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

insert into `tb_step`(`id`,`projectId`,`buildId`,`previousStepId`,`nextStepId`,`order`,`outPut`,`startTime`,`endTime`,`createTime`,`updateTime`) values
(1,2,1,1,1,2,'121221','2018-11-28 18:22:49','2018-11-28 18:22:51','2018-11-28 18:22:54','2018-11-28 18:22:58');
DROP TABLE IF EXISTS  `tb_template`;
CREATE TABLE `tb_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `os` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '脚本名称',
  `script` text COMMENT '脚本内容',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into `tb_template`(`id`,`os`,`name`,`script`,`createTime`,`updateTime`) values
(1,'linux','springBoot-edas','edas:deploy','2018-11-28 18:22:54','2018-11-28 18:22:58'),
(2,'windows','tomcat','cd #(home??)
set path=%path%;"#(gitHome??)"

if exist #(projectName??) (
    rem pause;
    cd #(projectName??)
	git clean -df
	git reset --hard
	git pull
	echo \'back parentPath, otherWise not exist will repeat execute!!!\'
    cd ..
)

if not exist #(projectName??) (
    rem pause;
	echo clone
	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??)
)

pause;','2018-11-28 18:22:54','2018-11-28 18:22:54');
SET FOREIGN_KEY_CHECKS = 1;

