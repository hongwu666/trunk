#!/bin/sh

SRC_DIR=/data/src/lieguo/$1
DEPLOY_DIR=/data/packing/lieguo
CONFIG_DIR=$DEPLOY_DIR/config

cd $SRC_DIR/

svn up

mvn clean deploy -e -U -Dmaven.test.skip=true -pl game-server,game-utils,game-model,game-dao,game-service

rm $DEPLOY_DIR -rf
mkdir $DEPLOY_DIR
mkdir $CONFIG_DIR

rm $CONFIG_DIR -rf

mkdir $CONFIG_DIR

mkdir $CONFIG_DIR/main-server
mkdir $CONFIG_DIR/main-server/server
mkdir $CONFIG_DIR/main-server/logic
cp $SRC_DIR/main-server/src/main/resources/*.xml $CONFIG_DIR/main-server/ -rf
cp $SRC_DIR/main-server/src/main/resources/*.properties $CONFIG_DIR/main-server/ -rf
cp $SRC_DIR/main-server/src/main/resources/server/*.xml $CONFIG_DIR/main-server/server/ -rf
cp $SRC_DIR/main-server/src/main/resources/logic/*.xml  $CONFIG_DIR/main-server/logic/ -rf
cp $SRC_DIR/main-server/src/main/resources/*.cfg $CONFIG_DIR/main-server/ -rf
rm $SRC_DIR/main-server/src/main/resources/*.xml -rf
rm $SRC_DIR/main-server/src/main/resources/server -rf
rm $SRC_DIR/main-server/src/main/resources/logic -rf
rm $SRC_DIR/main-server/src/main/resources/*.properties -rf
rm $SRC_DIR/main-server/src/main/resources/*.cfg -rf

cd $SRC_DIR/main-server/
mvn clean package shade:shade -U -Dmaven.test.skip=true
cp $SRC_DIR/main-server/target/main-server.jar $DEPLOY_DIR/

#/data/shell/main-server/run.sh

mkdir $CONFIG_DIR/logic-server
mkdir $CONFIG_DIR/logic-server/server
mkdir $CONFIG_DIR/logic-server/logic
cp $SRC_DIR/logic-server/src/main/resources/*.xml $CONFIG_DIR/logic-server/ -rf
cp $SRC_DIR/logic-server/src/main/resources/*.properties $CONFIG_DIR/logic-server/ -rf
cp $SRC_DIR/logic-server/src/main/resources/server/*.xml $CONFIG_DIR/logic-server/server/ -rf
cp $SRC_DIR/logic-server/src/main/resources/logic/*.xml  $CONFIG_DIR/logic-server/logic/ -rf
rm $SRC_DIR/logic-server/src/main/resources/*.xml -rf
rm $SRC_DIR/logic-server/src/main/resources/server -rf
rm $SRC_DIR/logic-server/src/main/resources/logic -rf
rm $SRC_DIR/logic-server/src/main/resources/*.properties -rf

cd $SRC_DIR/logic-server/
mvn clean package shade:shade -U -Dmaven.test.skip=true
cp $SRC_DIR/logic-server/target/logic-server.jar $DEPLOY_DIR/

#/data/shell/logic-server/run.sh

cd $SRC_DIR/game-web/

mkdir $CONFIG_DIR/game-web/
mkdir $CONFIG_DIR/game-web/config
mkdir $CONFIG_DIR/game-web/config/web

cd $SRC_DIR/game-web/
cp $SRC_DIR/game-web/src/main/resources/*.xml $CONFIG_DIR/game-web/ -rf
cp $SRC_DIR/game-web/src/main/resources/*.properties $CONFIG_DIR/game-web/ -rf
cp $SRC_DIR/game-web/src/main/resources/config/*.xml $CONFIG_DIR/game-web/config/ -rf
cp $SRC_DIR/game-web/src/main/resources/config/web/*.xml  $CONFIG_DIR/game-web/config/web/ -rf
rm $SRC_DIR/game-web/src/main/resources/*.xml
rm $SRC_DIR/game-web/src/main/resources/*.properties
rm $SRC_DIR/game-web/src/main/resources/config/*.xml
rm $SRC_DIR/game-web/src/main/resources/config/web/*.xml

mvn clean package shade:shade -U -Dmaven.test.skip=true

cd $SRC_DIR/game-web-tr/

mkdir $CONFIG_DIR/game-web-tr/
mkdir $CONFIG_DIR/game-web-tr/config
mkdir $CONFIG_DIR/game-web-tr/config/web

cd $SRC_DIR/game-web-tr/
cp $SRC_DIR/game-web-tr/src/main/resources/*.xml $CONFIG_DIR/game-web-tr/ -rf
cp $SRC_DIR/game-web-tr/src/main/resources/*.properties $CONFIG_DIR/game-web-tr/ -rf
cp $SRC_DIR/game-web-tr/src/main/resources/config/*.xml $CONFIG_DIR/game-web-tr/config/ -rf
cp $SRC_DIR/game-web-tr/src/main/resources/config/web/*.xml  $CONFIG_DIR/game-web-tr/config/web/ -rf
rm $SRC_DIR/game-web-tr/src/main/resources/*.xml
rm $SRC_DIR/game-web-tr/src/main/resources/*.properties
rm $SRC_DIR/game-web-tr/src/main/resources/config/*.xml
rm $SRC_DIR/game-web-tr/src/main/resources/config/web/*.xml

mvn clean package shade:shade -U -Dmaven.test.skip=true

cd $SRC_DIR/game-api/

mkdir $CONFIG_DIR/game-api/
mkdir $CONFIG_DIR/game-api/config
mkdir $CONFIG_DIR/game-api/config/web

cp $SRC_DIR/game-api/src/main/resources/*.xml $CONFIG_DIR/game-api/ -rf
cp $SRC_DIR/game-api/src/main/resources/*.properties $CONFIG_DIR/game-api/ -rf
cp $SRC_DIR/game-api/src/main/resources/config/*.xml $CONFIG_DIR/game-api/config/ -rf
cp $SRC_DIR/game-api/src/main/resources/config/web/*.xml  $CONFIG_DIR/game-api/config/web/ -rf
rm $SRC_DIR/game-api/src/main/resources/*.xml
rm $SRC_DIR/game-api/src/main/resources/*.properties
rm $SRC_DIR/game-api/src/main/resources/config/*.xml
rm $SRC_DIR/game-api/src/main/resources/config/web/*.xml

mvn clean package shade:shade -U -Dmaven.test.skip=true

cp $SRC_DIR/game-web/target/webapp.war $DEPLOY_DIR/game-web.war
cp $SRC_DIR/game-web-tr/target/webapp.war $DEPLOY_DIR/game-web-tr.war
cp $SRC_DIR/game-api/target/game-api.jar $DEPLOY_DIR/

cd $DEPLOY_DIR

tar -zcvf $1\.tar\.gz ./



/usr/bin/ftp -n 183.232.129.35 <<EOF
user ldsg_upload easou8888
binary
prompt
cd deploy_lg
delete $1.tar.gz
EOF

wput $DEPLOY_DIR/$1.tar.gz ftp://ldsg_upload:easou8888@183.232.129.35/deploy_lg/$1.tar.gz
