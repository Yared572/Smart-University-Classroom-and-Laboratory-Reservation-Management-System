@echo off
setlocal
cd /d "%~dp0"
mvn -s .mvn\settings.xml -pl reservation-infrastructure -am compile
if errorlevel 1 exit /b %errorlevel%
java -cp "reservation-infrastructure\target\classes;.m2\repository\com\mysql\mysql-connector-j\9.2.0\mysql-connector-j-9.2.0.jar" com.dbu.reservation.infrastructure.database.DatabaseConnectionCheck
exit /b %errorlevel%



