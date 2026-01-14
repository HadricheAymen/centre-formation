@echo off
echo ========================================
echo   REINITIALISATION DE LA BASE DE DONNEES
echo ========================================
echo.
echo ATTENTION: Cette operation va supprimer toutes les donnees!
echo.
pause

echo.
echo Connexion a MySQL et suppression de la base de donnees...
echo.

mysql -u root -h localhost -P 3306 -e "DROP DATABASE IF EXISTS formationdb; CREATE DATABASE formationdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERREUR: Impossible de reinitialiser la base de donnees!
    echo Verifiez que MySQL est demarre et que les identifiants sont corrects.
    pause
    exit /b 1
)

echo.
echo Base de donnees reinitialisee avec succes!
echo.
echo Demarrage de l'application...
echo.

mvn spring-boot:run -Dspring-boot.run.profiles=prod

pause

