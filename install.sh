#!/bin/bash

mvn install assembly:assembly antrun:run -Dmaven.test.skip=true
