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

# Run the application
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -jar target/Strategy-Demo-1.0-SNAPSHOT-fat.jar

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Failed to start the application!"
    echo "Make sure you have built the project first with: mvn clean package"
    echo
    exit 1
fi

echo
echo "Application closed."