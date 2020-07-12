@echo off
:: author: Leo Gutiérrez <leogutierrezramirez@autozone.com>

if "%JAVA_HOME%"=="" (
	echo error: JAVA_HOME variable is not defined
	pause > nul
	exit /b 1
)

set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
set "JAR_FILE_NAME=ProductionPanGUI.jar"

:: echo Path: "%JAVA_BIN%"
rem if not exist "%JAR_FILE_NAME%" (
rem 	echo error: %JAR_FILE_NAME% does not exist
rem 	pause > nul
rem 	exit /b 1
rem )
rem "%JAVA_BIN%" -jar "%JAR_FILE_NAME%"

rem copy "dist\%JAR_FILE_NAME%" run\
copy run\* dist\
mkdir dist\templates
copy html-templates\* dist\templates
cd dist\
"%JAVA_BIN%" -jar "%JAR_FILE_NAME%"
cd ..

exit /b 0