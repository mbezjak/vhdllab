@echo off
if "%1"=="y" goto setJDPA
JPDA_SUSPEND=n
goto done

:setJDPA
set JPDA_SUSPEND=y

:done
set JPDA_SUSPEND
set JAVAWS_VM_ARGS=-Xdebug\ -Xrunjdwp:transport=dt_socket,address=5555,server=y,suspend=%JPDA_SUSPEND%
javaws http://@@app.host@@:@@http.port@@/@@app.name@@/@@jnlp.file@@
