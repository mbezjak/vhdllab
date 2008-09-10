#!/bin/bash

mvn package assembly:assembly antrun:run -Dmaven.test.skip=true
