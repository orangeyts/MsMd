#!/bin/sh

## java env
export JAVA_HOME=/root/soft/jdk1.8.0_11
export JRE_HOME=$JAVA_HOME/jre

## service name

SERVICE_DIR=/data/v4-wechat
SERVICE_NAME=v4-wechat
JAR_NAME=$SERVICE_NAME\.jar
PID=$SERVICE_NAME\.pid
timeStamp=`date "+%Y%m%d_%H_%M_%S"`
cd $SERVICE_DIR

case "$1" in

    start)
        nohup $JRE_HOME/bin/java -Xms512m -Xmx2048m -jar $JAR_NAME >/dev/null 2>&1 &
        echo $! > $SERVICE_DIR/$PID
        echo "=== start $SERVICE_NAME"
        ;;

    stop)
        kill `cat $SERVICE_DIR/$PID`
        rm -rf $SERVICE_DIR/$PID
        echo "=== stop $SERVICE_NAME"

        sleep 5
        P_ID=`ps -ef | grep -w "$SERVICE_NAME" | grep -v "grep" | awk '{print $2}'`
        if [ "$P_ID" == "" ]; then
            echo "=== $SERVICE_NAME process not exists or stop success"
        else
            echo "=== $SERVICE_NAME process pid is:$P_ID"
            echo "=== begin kill $SERVICE_NAME process, pid is:$P_ID"
            kill -9 $P_ID
        fi
        ;;

    restart)
        $0 stop
        sleep 2
        $0 start
        echo "=== restart $SERVICE_NAME"
        ;;
		
	cpNewVersion)
        ## bakUp current version
		echo "=== bakUp jar $timeStamp"
		cp -f $JAR_NAME $JAR_NAME\.$timeStamp
		echo "=== copy new version"
		## cp newVersion to deploy dir
		cp -f project_versions/$JAR_NAME $SERVICE_DIR
        ;;

    *)
        ## restart
        $0 stop
        sleep 2
		$0 cpNewVersion
        $0 start
        ;;
esac
exit 0

