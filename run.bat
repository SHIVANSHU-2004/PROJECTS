@echo off
setlocal
set ROOT=%~dp0
set SRC=%ROOT%src\main\java
set OUT=%ROOT%out

if not exist "%OUT%" mkdir "%OUT%"

dir /s /b "%SRC%\*.java" > "%ROOT%sources.txt"
javac -d "%OUT%" @"%ROOT%sources.txt"
if errorlevel 1 (
  echo Compilation failed.
  exit /b 1
)

java -cp "%OUT%" com.symptomchecker.Main
