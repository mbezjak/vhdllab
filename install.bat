@echo off
mvn package assembly:assembly antrun:run -Dmaven.test.skip=true
