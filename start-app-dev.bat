@echo off
echo ========================================
echo   CENTRE DE FORMATION - MODE DEVELOPPEMENT
echo ========================================
echo.
echo Demarrage de l'application avec H2...
echo.

mvn spring-boot:run -Dspring-boot.run.profiles=dev

pause

