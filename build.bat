@echo off
echo =============================================
echo       Building Strategy Pattern Demo
echo =============================================
echo.
echo This will compile and package the application...
echo.

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH!
    echo Please install Maven and try again.
    echo Download from: https://maven.apache.org/download.cgi
    echo.
    pause
    exit /b 1
)

echo Running: mvn clean package
echo.
mvn clean package

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Build failed!
    echo Please check the error messages above.
    echo.
    pause
    exit /b 1
)

echo.
echo =============================================
echo           Build Successful!
echo =============================================
echo.
echo The executable JAR has been created at:
echo target\Strategy-Demo-1.0-SNAPSHOT-fat.jar
echo.
echo You can now run the application using:
echo - Double-click run.bat (Windows)
echo - Or run: java -jar target\Strategy-Demo-1.0-SNAPSHOT-fat.jar
echo.
pause