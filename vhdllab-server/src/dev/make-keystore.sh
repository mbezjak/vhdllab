#!/bin/bash
#
# Creates a very weak keystore valid for 10 years. This is good enough for
# development but in production different keytool should be used!
keytool -genkey -alias vhdllab -keypass vhdllab -storepass vhdllab -keystore devel-keystore -dname "CN=vhdllab OU=Java team O=FER L=Zagreb C=HR" -validity 3650

if [ $? -eq 0 ]; then # keytool finished successfully
    echo "devel-keystore created."
fi
