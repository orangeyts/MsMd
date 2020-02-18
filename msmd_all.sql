SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `blog`;
CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  `category` varchar(32) DEFAULT NULL COMMENT '����',
  `startArticle` tinyint(2) DEFAULT '0' COMMENT '�Ƿ����',
  `authorAge` tinyint(10) DEFAULT NULL COMMENT '��������',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS  `t`;
CREATE TABLE `t` (
  `id` int(11) NOT NULL,
  `c` int(11) DEFAULT NULL,
  `d` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `c` (`c`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

insert into `t`(`id`,`c`,`d`) values
(0,0,0),
(5,5,5),
(10,10,10),
(15,15,15),
(20,20,20),
(25,25,25);
DROP TABLE IF EXISTS  `tb_account`;
CREATE TABLE `tb_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(200) NOT NULL COMMENT '����-linux git ��',
  `regionId` varchar(32) DEFAULT NULL COMMENT 'aliyun-����',
  `userName` varchar(50) NOT NULL COMMENT '�û���',
  `pwd` varchar(50) DEFAULT NULL COMMENT '����',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='ϵͳһЩ���õ��˻� git, ssh ��';

insert into `tb_account`(`id`,`type`,`regionId`,`userName`,`pwd`,`ip`) values
(1,'gitee',null,'1','2',null),
(2,'2',null,'1','2',null),
(3,'242������',null,'1','2','120.1.1.1'),
(4,'aksk','cn-shenzhen','1','2',null),
(5,'ssh-UserService','Huawei-shenZhen','root','2}R67V5}-1','192.168.0.107');
DROP TABLE IF EXISTS  `tb_env_config`;
CREATE TABLE `tb_env_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cGroup` varchar(200) NOT NULL COMMENT '����',
  `cKey` varchar(50) NOT NULL COMMENT 'key',
  `cValue` varchar(50) DEFAULT NULL COMMENT '����ֵ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

insert into `tb_env_config`(`id`,`cGroup`,`cKey`,`cValue`) values
(1,'common','HOME','/Users/sheng/Downloads/cihome'),
(2,'common','GIT','/usr/bin/git'),
(3,'common','serverIp','12.1.1.1'),
(4,'common','cookieDomain','log.company.com');
DROP TABLE IF EXISTS  `tb_project`;
CREATE TABLE `tb_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `os` varchar(255) NOT NULL DEFAULT '' COMMENT 'linux windows',
  `subProjectJson` longtext COMMENT '��ģ����Ŀ ����Ŀ',
  `type` int(11) DEFAULT '1' COMMENT '��Ŀ���� 1 web 2 rpc',
  `dutyPerson` varchar(255) DEFAULT NULL COMMENT '������',
  `sshAccountId` int(11) DEFAULT '0' COMMENT 'ssh �˻�id',
  `edasAppId` varchar(100) DEFAULT NULL COMMENT 'appId',
  `edasPackageVersion` varchar(32) DEFAULT NULL COMMENT '�ع��汾��',
  `eadsAccountId` int(11) DEFAULT NULL COMMENT 'edas�˻�',
  `accountId` int(11) DEFAULT NULL COMMENT '�˻�Id,����git �����Ȩ',
  `scmPath` varchar(255) DEFAULT NULL COMMENT '�汾���Ƶ�ַ',
  `scriptFilePath` varchar(255) DEFAULT NULL COMMENT '�ű�ִ��·��',
  `sshScript` varchar(5000) DEFAULT NULL COMMENT 'ssh �ű�',
  `script` text COMMENT 'ִ�нű�',
  `phone` varchar(20) DEFAULT NULL COMMENT '�ֻ���',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

insert into `tb_project`(`id`,`title`,`os`,`subProjectJson`,`type`,`dutyPerson`,`sshAccountId`,`edasAppId`,`edasPackageVersion`,`eadsAccountId`,`accountId`,`scmPath`,`scriptFilePath`,`sshScript`,`script`,`phone`,`createTime`,`updateTime`) values
(2,'spring-mvc-chat_bak','linux',null,2,'he2',0,null,null,null,2,'gitee.com/githubsync/spring-mvc-chat.git','E:\\hs\\software\\jfinal-3.5_demo_for_maven\\MsMd',null,'cmd.exe /c run.bat','137111112','2018-11-27 15:15:29','2018-11-27 15:15:34'),
(3,'tomcatApp','windows',null,1,'he2',0,null,null,null,2,'gitee.com/githubsync/spring-mvc-chat.git',null,null,'cd #(home??)
set path=%path%;"#(gitHome??)"

if exist #(projectName??) (
    echo \'update project\'
    cd #(projectName??)
	git clean -df
	git reset --hard
	git pull
	echo \'back parentPath, otherWise not exist will repeat execute!!!\'
    cd ..
)

if not exist #(projectName??) (
    echo \'init project\'
	echo clone
	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??) #(projectName??)
)
cd #(projectName??)
mvn clean install -DskiptTests',null,null,'2019-12-03 18:19:15'),
(4,'spring-mvc-chat','linux',null,1,'he2',0,null,null,null,2,'gitee.com/githubsync/spring-mvc-chat.git','run.bat',null,'#!/bin/sh

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
(5,'nuo','linux',null,1,'he2',0,null,null,null,1,'gitee.com/company/nuo.git','run.bat',null,'#!/bin/sh

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
mvn clean install -DskipTests','13711111',null,'2019-02-18 14:30:06'),
(6,'goodTeacher-wx','linux',null,1,'he2',3,'b67f0d03-e522-4782-b27a-bca0db9e777b','2019-12-12 10:42:07',4,0,'gitee.com/githubsync/spring-mvc-chat.git','run.bat','#!/bin/sh

for((i=1;i<=10;i++));
do 
echo $(expr $i \\* 3 + 1);
done
','#!/bin/sh

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
mvn clean install -DskipTests','13711111',null,'2019-12-20 10:52:50'),
(7,'sshclientTest','linux',null,1,'he2',3,'b67f0d03-e522-4782-b27a-bca0db9e777b','2019-12-12 10:42:07',0,0,'gitee.com/githubsync/spring-mvc-chat.git','run.bat','#!/bin/sh

for((i=1;i<=10;i++));
do 
echo $(expr $i \\* 3 + 1);
done
','#!/bin/sh

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
mvn clean install -DskipTests','13711111',null,'2019-12-20 10:56:55'),
(8,'nuoV4','linux','[{"projectPath":"v4-common-parent/v4-nuo-service-user/target","projectName":"v4-nuo-service-user","zip":true,"zipFolderOrFile":"lib v4-nuo-service-user.jar","sshId":"5","sshScript":"#!/bin/sh\\n\\nsource /data/#(subProjectName)/runApp.sh\\n"},{"projectPath":"v4-common-parent/v4-wechat/target","projectName":"v4-wechat","zip":false,"sshId":"5","sshScript":"#!/bin/sh\\n\\nsource /data/#(subProjectName)/runApp.sh\\n"},{"projectPath":"v4-common-parent/v4-school/target","projectName":"v4-school","zip":false,"sshId":"5","sshScript":"#!/bin/sh\\n\\nsource /data/#(subProjectName)/runApp.sh\\n"}]',1,'he2',5,null,null,0,1,'gitee.com/company/mother_infant_center.git','run.sh',null,'#!/bin/sh

## mvn home
export MAVEN_HOME=/root/soft/apache-maven-3.3.9
export PATH=$PATH:$MAVEN_HOME/bin

cd #(home??)
if [ -d #(projectName??) ]
then
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
	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??) #(projectName??) 
fi
cd #(projectName??)
mvn clean install -DskipTests -Ptest','13711111',null,'2020-02-12 17:33:18'),
(10,'v4_school','linux','[{"projectPath":"v4-common-parent/v4-school/target","projectName":"v4-school","zip":false,"sshId":"5","sshScript":"#!/bin/sh\\n\\nsource /data/#(subProjectName)/runApp.sh\\n"}]',1,'he2',5,null,null,0,1,'gitee.com/company/mother_infant_center.git','run.sh',null,'#!/bin/sh

## mvn home
export MAVEN_HOME=/root/soft/apache-maven-3.3.9
export PATH=$PATH:$MAVEN_HOME/bin

cd #(home??)
if [ -d #(projectName??) ]
then
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
	git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??) #(projectName??) 
fi
cd #(projectName??)
mvn clean install -DskipTests -Ptest','13711111',null,'2020-02-13 12:00:00');
DROP TABLE IF EXISTS  `tb_step`;
CREATE TABLE `tb_step` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL COMMENT '��Ŀid',
  `buildId` int(11) DEFAULT NULL COMMENT '����Id',
  `previousStepId` int(11) DEFAULT NULL COMMENT '��һ������id',
  `nextStepId` int(11) DEFAULT NULL COMMENT '��һ������id,û��Ϊnull',
  `order` int(11) NOT NULL COMMENT '��������',
  `outPut` varchar(255) DEFAULT NULL COMMENT 'ִ�����',
  `startTime` datetime DEFAULT NULL COMMENT '������',
  `endTime` datetime DEFAULT NULL COMMENT '�ֻ���',
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
  `name` varchar(255) DEFAULT NULL COMMENT '�ű�����',
  `script` text COMMENT '�ű�����',
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

