@echo off

mkdir output
jdk-14.0.2\bin\java.exe -jar "ProductionPanGUI.jar" output\ >> debug.log 2>&1

pause

exit /b 0