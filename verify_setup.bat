@echo off
REM ============================================
REM StreamHub Backend - Setup Verification Script
REM Windows Batch File
REM ============================================

echo.
echo ====================================
echo StreamHub Backend Setup Verification
echo ====================================
echo.

REM Check Java Installation
echo [1/5] Checking Java Installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed!
    echo Please install Java 17+ from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
    pause
    exit /b 1
) else (
    echo [OK] Java is installed
    java -version
)

echo.

REM Check Maven Installation
echo [2/5] Checking Maven Installation...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed!
    echo Please install Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
) else (
    echo [OK] Maven is installed
    mvn -version
)

echo.

REM Check MySQL Installation
echo [3/5] Checking MySQL Installation...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [WARNING] MySQL command line tool not found in PATH
    echo Please install MySQL from: https://dev.mysql.com/downloads/mysql/
    echo You can still proceed if MySQL service is running
) else (
    echo [OK] MySQL is installed
    mysql --version
)

echo.

REM Check if pom.xml exists
echo [4/5] Checking project files...
if exist "pom.xml" (
    echo [OK] pom.xml found
) else (
    echo [ERROR] pom.xml not found! Wrong directory?
    pause
    exit /b 1
)

if exist "src\main\resources\application.properties" (
    echo [OK] application.properties found
) else (
    echo [ERROR] application.properties not found!
    pause
    exit /b 1
)

if exist "src\main\java\com\streamhub\entity\Content.java" (
    echo [OK] Content.java found
) else (
    echo [ERROR] Content.java not found!
    pause
    exit /b 1
)

echo.

REM Build project
echo [5/5] Building project...
echo.
mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Maven build failed!
    echo Please check the error messages above
    pause
    exit /b 1
) else (
    echo.
    echo [OK] Build successful!
)

echo.
echo ====================================
echo ✓ Setup Verification Complete!
echo ====================================
echo.
echo Next Steps:
echo 1. Create MySQL database:
echo    mysql -u root -p streamhub_db ^< src\main\resources\schema.sql
echo.
echo 2. Update database password in:
echo    src\main\resources\application.properties
echo.
echo 3. Run the application:
echo    mvn spring-boot:run
echo.
echo 4. API will be available at:
echo    http://localhost:8080/api/content
echo.
echo 5. Test with cURL:
echo    curl http://localhost:8080/api/content
echo.
echo For more info, read: QUICKSTART.md
echo.
pause
