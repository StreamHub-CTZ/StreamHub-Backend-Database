#!/bin/bash
# StreamHub Backend - Bash Setup Script (for Linux/Mac users)

echo ""
echo "===================================="
echo "StreamHub Backend Setup"
echo "===================================="
echo ""

# Check Java
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed!"
    echo "Install from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html"
    exit 1
fi
echo "✓ Java installed: $(java -version 2>&1 | head -n 1)"

# Check Maven
echo "Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed!"
    echo "Install from: https://maven.apache.org/"
    exit 1
fi
echo "✓ Maven installed: $(mvn -version | head -n 1)"

# Check MySQL
echo "Checking MySQL installation..."
if ! command -v mysql &> /dev/null; then
    echo "WARNING: MySQL CLI not found. Install MySQL or ensure it's in PATH"
    echo "But if MySQL service is running, you can continue"
fi

# Build project
echo ""
echo "Building project with Maven..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "===================================="
    echo "✓ Setup Verification Complete!"
    echo "===================================="
    echo ""
    echo "Next Steps:"
    echo "1. Create MySQL database:"
    echo "   mysql -u root -p streamhub_db < src/main/resources/schema.sql"
    echo ""
    echo "2. Edit database credentials:"
    echo "   src/main/resources/application.properties"
    echo ""
    echo "3. Run the application:"
    echo "   mvn spring-boot:run"
    echo ""
    echo "4. API runs at: http://localhost:8080/api/content"
    echo ""
else
    echo "ERROR: Build failed!"
    exit 1
fi
