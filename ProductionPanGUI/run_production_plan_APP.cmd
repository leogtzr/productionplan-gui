@echo off

if "%JAVA_HOME%"=="" (
	echo error: JAVA_HOME variable is not defined
	pause > nul
	exit /b 1
)

set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
set "JAR_FILE_NAME=ProductionPanGUI.jar"

:: echo Path: "%JAVA_BIN%"
if not exist "%JAR_FILE_NAME%" (
	echo error: %JAR_FILE_NAME% does not exist
	pause > nul
	exit /b 1
)

"%JAVA_BIN%" -jar "%JAR_FILE_NAME%"

pause

exit /b 0