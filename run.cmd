@echo off
setlocal
cd /d "%~dp0"
mvn -s .mvn\settings.xml -pl reservation-presentation javafx:run
exit /b %errorlevel%
