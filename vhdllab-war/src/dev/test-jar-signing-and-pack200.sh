#!/bin/bash
#

SEGMENT_LIMIT=-1

cd ../../target
echo "current dir=`pwd`"
rm vhdllab-client-with-deps.jar.pack.gz
cp ../../vhdllab-client/target/vhdllab-client-with-deps.jar .

pack200 --repack --segment-limit=$SEGMENT_LIMIT vhdllab-client-with-deps.jar
jarsigner -keystore ../src/dev/devel-keystore -keypass vhdllab -storepass vhdllab vhdllab-client-with-deps.jar vhdllab
jarsigner -verify vhdllab-client-with-deps.jar
pack200 --segment-limit=$SEGMENT_LIMIT vhdllab-client-with-deps.jar.pack.gz vhdllab-client-with-deps.jar
unpack200 vhdllab-client-with-deps.jar.pack.gz vhdllab-client-with-deps.jar
jarsigner -verify vhdllab-client-with-deps.jar

