@echo off
echo ========================================
echo   CENTRE DE FORMATION - BUILD ET RUN
echo ========================================
echo.
echo Etape 1: Compilation de l'application...
echo.

call mvn clean package -P dev -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR: La compilation a echoue!
    pause
    exit /b 1
)

echo.
echo Etape 2: Demarrage de l'application en mode DEVELOPPEMENT...
echo.

java -jar target/centre-formation-1.0.0-CLEAN.jar --spring.profiles.active=dev

pause

