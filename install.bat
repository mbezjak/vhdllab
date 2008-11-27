@echo off

mvn -Pfast
cd vhdllab-war
mvn jetty:run-exploded -Pdev
