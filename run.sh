#!/bin/bash

echo "============================================="
echo "   Strategy Pattern Demo - E-Commerce App"
echo "============================================="
echo

echo "Starting the application..."
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH!"
    echo "Please install Java 21 or later and try again."
    echo
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "WARNING: Java version $JAVA_VERSION detected. Java 17+ recommended."
    echo
fi

# Check if the fat JAR exists
if [ ! -f "target/Strategy-Demo-1.0-SNAPSHOT-fat.jar" ]; then
    echo "ERROR: JAR file not found!"
    echo "Please build the project first with: mvn clean package"
    echo
    exit 1
fi

# Run the application (fat JAR includes all JavaFX dependencies)
java -jar target/Strategy-Demo-1.0-SNAPSHOT-fat.jar

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Failed to start the application!"
    echo "If you see JavaFX module errors, you may need to:"
    echo "1. Install JavaFX separately"
    echo "2. Use: java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/Strategy-Demo-1.0-SNAPSHOT-fat.jar"
    echo
    exit 1
fi

echo
echo "Application closed."