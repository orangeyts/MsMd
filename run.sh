#!/bin/sh

## java env
export JAVA_HOME=/home/project/software/jdk1.8.0_11
export JRE_HOME=$JAVA_HOME/jre
## mvn home
export MAVEN_HOME=/home/project/software/apache-maven-3.0.5
export PATH=$PATH:$MAVEN_HOME/bin

echo 'git pull'
cd /home/project/software/project/java-admin
git clean -df
git reset --hard
git pull


sleep 5
echo 'mvn clean install'
cd /home/project/software/project/java-admin/jfinal-cms
mvn clean install -Ptest


## restart tomcat
echo 'shutdown tomcat'
/home/project/software/apache-tomcat-7.0.61/bin/shutdown.sh
sleep 3
echo 'remove old project'
rm -rf /home/project/software/apache-tomcat-7.0.61/webapps/cms-admin
rm -rf /home/project/software/apache-tomcat-7.0.61/webapps/cms-api
echo 'copy new project'
cp /home/project/software/project/java-admin/jfinal-cms/cms-admin/target/cms-admin.war /home/project/software/apache-tomcat-7.0.61/webapps/
cp /home/project/software/project/java-admin/jfinal-cms/cms-api/target/cms-api.war /home/project/software/apache-tomcat-7.0.61/webapps/
echo 'START ...'
/home/project/software/apache-tomcat-7.0.61/bin/startup.sh

