@echo off

call mvn -Pfast
chdir vhdllab-server
call mvn jetty:run-exploded -Pdev
exit
