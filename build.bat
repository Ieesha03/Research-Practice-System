@echo off
REM Set classpath to include all JARs in the lib directory and the bin directory
set CLASSPATH=lib\*;bin

REM Compile all Java files in src, output to bin directory
javac -cp "%CLASSPATH%" -d bin src\controller\*.java src\model\*.java src\view\*.java src\utils\*.java src\main\MainApp.java

REM Run the main application
java -cp "%CLASSPATH%" main.MainApp
pause
