#!/bin/bash
#

cat pom.xml | xpath-query '//project/properties/application.version/text()'
