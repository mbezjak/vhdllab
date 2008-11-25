@echo off

mvn -Pfast
cd vhdllab-war
mvn tomcat:exploded
