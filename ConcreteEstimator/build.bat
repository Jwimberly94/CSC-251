@echo off
setlocal

set BASE=%~dp0
set SRC=%BASE%src\main\java
set RES=%BASE%src\main\resources
set OUT=%BASE%out
set JAR=%BASE%ConcreteEstimator.jar

echo === Compiling sources ===
if not exist "%OUT%" mkdir "%OUT%"

:: Collect all .java files
for /r "%SRC%" %%f in (*.java) do echo %%f >> "%BASE%sources.tmp"

javac -encoding UTF-8 -d "%OUT%" @"%BASE%sources.tmp"
if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed.
    del /f /q "%BASE%sources.tmp"
    exit /b 1
)
del /f /q "%BASE%sources.tmp"

:: Copy resources into output
xcopy /e /i /y "%RES%" "%OUT%" >nul

echo.
echo === Building JAR ===
jar cfm "%JAR%" "%BASE%MANIFEST.MF" -C "%OUT%" .
if errorlevel 1 (
    echo ERROR: JAR creation failed.
    exit /b 1
)

echo.
echo === Build successful: %JAR% ===
echo Run with:  java -jar ConcreteEstimator.jar
endlocal
