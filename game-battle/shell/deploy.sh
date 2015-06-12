#!/bin/sh

SRC_DIR=/data/src/lieguo/$1
DEPLOY_DIR=/data/deploy_dir
CONFIG_DIR=/data/deploy_dir/config

cd $SRC_DIR/

svn up


rm $DEPLOY_DIR -rf
mkdir $DEPLOY_DIR

rm $CONFIG_DIR -rf
mkdir $CONFIG_DIR

mkdir $CONFIG_DIR/game-battle
cp $SRC_DIR/game-battle/src/main/resources/*.xml $CONFIG_DIR/game-battle/ -rf
cp $SRC_DIR/game-battle/src/main/resources/*.properties $CONFIG_DIR/game-battle/ -rf
rm $SRC_DIR/game-battle/src/main/resources/*.xml -rf
rm $SRC_DIR/game-battle/src/main/resources/*.properties -rf

cd $SRC_DIR/game-battle/
mvn clean package shade:shade -U -Dmaven.test.skip=true
cp $SRC_DIR/game-battle/target/game-battle.jar $DEPLOY_DIR/

cd $DEPLOY_DIR

tar -zcvf game-battle_$1\.tar\.gz ./


/usr/bin/ftp -n 183.232.129.35 <<EOF
user ldsg_upload easou8888
binary
prompt
cd deploy_lg
delete game-battle_$1.tar.gz
EOF

wput $DEPLOY_DIR/game-battle_$1.tar.gz ftp://ldsg_upload:easou8888@183.232.129.35/deploy_lg/game-battle_$1.tar.gz
