#!/bin/bash
if [ "$1" = "y" ];
then JPDA_SUSPEND=y;
else JPDA_SUSPEND=n;
fi
export JPDA_SUSPEND
export JAVAWS_VM_ARGS=-Xdebug\ -Xrunjdwp:transport=dt_socket,address=5555,server=y,suspend=$JPDA_SUSPEND
javaws http://@@app.host@@:@@http.port@@/@@app.name@@/@@jnlp.file@@
