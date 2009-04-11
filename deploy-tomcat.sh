#!/bin/bash
#

if [ -z "$CATALINA_HOME" ]; then
    echo "CATALINA_HOME must be set!!"
    exit 1
fi

CATALINA_WEBAPP=$CATALINA_HOME/webapps
VHDLLAB_WEBAPP_DIR=$CATALINA_WEBAPP/vhdllab
VHDLLAB_WEBAPP_WAR=$CATALINA_WEBAPP/vhdllab.war

if [ "$1" != "--force" ]; then
    echo "This script will delete:"
    echo "directory: $VHDLLAB_WEBAPP_DIR"
    echo "file: $VHDLLAB_WEBAPP_WAR"
    echo "Are you sure you want to continue deploying vhdllab?"
    read ANSWERE
    if [ "$ANSWERE" != "y" ]; then
        exit 0
    fi
fi

$CATALINA_HOME/bin/shutdown.sh 2> /dev/null
rm -rf $VHDLLAB_WEBAPP_DIR $VHDLLAB_WEBAPP_WAR

mvn -Pprod
cp vhdllab.war $CATALINA_WEBAPP
$CATALINA_HOME/bin/startup.sh

tail -f $CATALINA_HOME/logs/catalina.out

