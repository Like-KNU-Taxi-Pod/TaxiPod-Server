#!/bin/bash

ROOT_PATH="/home/ubuntu"
DEPLOY_PATH="/home/ubuntu/deploy"

APP_LOG="$DEPLOY_PATH/application.log"
ERROR_LOG="$DEPLOY_PATH/error.log"
START_LOG="$DEPLOY_PATH/start.log"

NOW=$(date +%c)

echo "[$NOW] Copy $JAR" >> $START_LOG
cp $DEPLOY_PATH/core/core-api/build/libs/*.jar $ROOT_PATH

JAR=$(find $ROOT_PATH -name "*.jar" | head -n 1)

echo "[$NOW] Run $JAR" >> $START_LOG
nohup java -jar $JAR --spring.profiles.active=develop > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] Application PID: $SERVICE_PID" >> $START_LOG
