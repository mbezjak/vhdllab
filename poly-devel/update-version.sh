#!/bin/bash
#

declare -r version="$1"
declare -r oldxml='<application.version>.\+</application.version>'
declare -r newxml="<application.version>$version</application.version>"

sed -i "s|$oldxml|$newxml|" pom.xml
