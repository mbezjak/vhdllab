@echo off

call mvn -Pfast
chdir vhdllab-war
call mvn jetty:run-exploded -Pdev
exit
