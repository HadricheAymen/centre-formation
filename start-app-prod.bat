@echo off
echo ========================================
echo   CENTRE DE FORMATION - MODE PRODUCTION
echo ========================================
echo.
echo Demarrage de l'application avec MySQL...
echo.

mvn spring-boot:run -Dspring-boot.run.profiles=prod

pause

