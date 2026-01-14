@echo off
echo ========================================
echo   CENTRE DE FORMATION - BUILD ET RUN
echo ========================================
echo.
echo Etape 1: Compilation de l'application...
echo.

call mvn clean package -P prod -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR: La compilation a echoue!
    pause
    exit /b 1
)

echo.
echo Etape 2: Demarrage de l'application en mode PRODUCTION...
echo.
docker compose down
echo Building Docker images...
docker compose build
echo Starting Docker containers...
docker compose up -d


pause

