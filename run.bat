@echo off
echo =============================================
echo    Strategy Pattern Demo - E-Commerce App
echo =============================================
echo.
echo Starting the application...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH!
    echo Please install Java 21 or later and try again.
    echo.
    pause
    exit /b 1
)

REM Run the application
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -jar target/Strategy-Demo-1.0-SNAPSHOT-fat.jar

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to start the application!
    echo Make sure you have built the project first with: mvn clean package
    echo.
    pause
    exit /b 1
)

echo.
echo Application closed.
pause