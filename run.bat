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

REM Check if the fat JAR exists
if not exist "target\Strategy-Demo-1.0-SNAPSHOT-fat.jar" (
    echo ERROR: JAR file not found!
    echo Please build the project first with: mvn clean package
    echo.
    pause
    exit /b 1
)

REM Run the application (fat JAR includes all JavaFX dependencies)
java -jar target/Strategy-Demo-1.0-SNAPSHOT-fat.jar

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to start the application!
    echo If you see JavaFX module errors, try:
    echo java --module-path "C:\Program Files\Java\javafx-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar target/Strategy-Demo-1.0-SNAPSHOT-fat.jar
    echo.
    pause
    exit /b 1
)

echo.
echo Application closed.
pause