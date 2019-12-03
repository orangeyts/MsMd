#!/bin/sh

## java env
export JAVA_HOME=#(JAVA_HOME??)
export JRE_HOME=$JAVA_HOME/jre
## mvn home
export MAVEN_HOME=#(MAVEN_HOME??)
export PATH=$PATH:$MAVEN_HOME/bin

if [ ! -d "#(projectName??)" ];then
    echo clone
    git clone https://#(scmUser??):#(scmPwd??)@#(scmPath??) #(projectName??)
else
    echo 'git pull'
    cd #(projectName??)
    echo 'clean workspace'
    git clean -df
    git reset --hard
    git pull
    sleep 5
    echo 'mvn clean install'
    mvn clean install
fi


## restart tomcat
echo 'shutdown tomcat'
## /home/projectName/software/apache-tomcat-7.0.61/bin/shutdown.sh
sleep 3
echo 'remove old project'
## rm -rf /home/projectName/software/apache-tomcat-7.0.61/webapps/cms-admin
echo 'copy new project'
## cp /home/projectName/software/project/java-admin/jfinal-cms/cms-admin/target/cms-admin.war /home/projectName/software/apache-tomcat-7.0.61/webapps/
echo 'START ...'
## /home/projectName/software/apache-tomcat-7.0.61/bin/startup.sh

get_char

