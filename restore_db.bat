@echo off
chcp 65001 >nul
echo ================================================
echo        DATABASE RESTORE SCRIPT
echo ================================================
echo.

REM Create backups directory if it doesn't exist
if not exist "backups" (
    echo [ERROR] Backups directory not found!
    mkdir backups
    echo [INFO] Created backups directory
)

REM List available backups
echo Available backups in backups folder:
echo ------------------------------------
dir backups\*.zip /b 2>nul
if errorlevel 1 (
    echo No backup files found!
    pause
    exit /b 1
)

echo.
set /p BACKUP_FILE="Enter backup filename (with .zip): "

REM Check if file exists
if not exist "backups\%BACKUP_FILE%" (
    echo [ERROR] Backup file not found: backups\%BACKUP_FILE%
    echo Available files:
    dir backups\*.zip /b
    pause
    exit /b 1
)

REM Check if database container is running
docker compose ps | findstr "formation_db" >nul
if errorlevel 1 (
    echo [ERROR] Database container is not running!
    echo Starting database container...
    docker compose up -d db
    timeout /t 10 /nobreak >nul
    
    REM Verify container started
    docker compose ps | findstr "formation_db" >nul
    if errorlevel 1 (
        echo [ERROR] Failed to start database container!
        pause
        exit /b 1
    )
)

REM Create temporary directory for extraction
set TEMP_DIR=backups\temp_restore_%RANDOM%
if exist "%TEMP_DIR%" rmdir /s /q "%TEMP_DIR%"
mkdir "%TEMP_DIR%"

echo [INFO] Extracting backup file...
powershell -Command "$ErrorActionPreference='Stop'; try { Expand-Archive -Path 'backups\%BACKUP_FILE%' -DestinationPath '%TEMP_DIR%' -Force } catch { Write-Host 'Extraction failed: ' $_.Exception.Message; exit 1 }"

if errorlevel 1 (
    echo [ERROR] Failed to extract backup file!
    rmdir /s /q "%TEMP_DIR%" 2>nul
    pause
    exit /b 1
)

REM Find SQL file in extracted directory
set SQL_FILE=
for /f "delims=" %%i in ('dir /b "%TEMP_DIR%\*.sql" 2^>nul') do set "SQL_FILE=%%i"

if "%SQL_FILE%"=="" (
    echo [ERROR] No SQL file found in backup archive!
    rmdir /s /q "%TEMP_DIR%"
    pause
    exit /b 1
)

echo [INFO] Found SQL file: %SQL_FILE%

REM Check if application is running
echo [INFO] Checking application status...
docker compose ps | findstr "formation_app" >nul
if errorlevel 1 (
    set APP_RUNNING=0
    echo [INFO] Application is not running
) else (
    set APP_RUNNING=1
    echo [INFO] Application is running. Will pause it temporarily...
    docker compose pause app
)

REM Create safety backup before restore
echo [INFO] Creating safety backup before restore...
set SAFETY_BACKUP=safety_backup_before_restore_%time:~0,2%%time:~3,2%.sql
docker compose exec -T db mysqldump -u root -prootpassword centre_formation > "%TEMP_DIR%\%SAFETY_BACKUP%" 2>nul

echo [INFO] Starting restore process...
echo Step 1: Dropping and recreating database...
docker compose exec -T db mysql -u root -prootpassword -e "DROP DATABASE IF EXISTS centre_formation; CREATE DATABASE centre_formation;"

echo Step 2: Restoring data from backup...
docker compose exec -T db mysql -u root -prootpassword centre_formation < "%TEMP_DIR%\%SQL_FILE%"

if errorlevel 1 (
    echo [ERROR] Restore failed! Attempting to restore from safety backup...
    
    REM Try to restore from safety backup
    if exist "%TEMP_DIR%\%SAFETY_BACKUP%" (
        echo [INFO] Restoring from safety backup...
        docker compose exec -T db mysql -u root -prootpassword -e "DROP DATABASE IF EXISTS centre_formation; CREATE DATABASE centre_formation;"
        docker compose exec -T db mysql -u root -prootpassword centre_formation < "%TEMP_DIR%\%SAFETY_BACKUP%"
        
        if errorlevel 1 (
            echo [CRITICAL] Failed to restore from safety backup!
        ) else (
            echo [INFO] Successfully restored from safety backup
        )
    )
    
    if "%APP_RUNNING%"=="1" (
        docker compose unpause app
    )
    
    rmdir /s /q "%TEMP_DIR%"
    pause
    exit /b 1
)

echo [SUCCESS] Database restored successfully!

REM Resume application if it was paused
if "%APP_RUNNING%"=="1" (
    echo [INFO] Resuming application...
    docker compose unpause app
    timeout /t 3 /nobreak >nul
    echo [INFO] Application is running
)

REM Verify the restore
echo [INFO] Verifying restore...
docker compose exec -T db mysql -u root -prootpassword centre_formation -e "SHOW TABLES;" | findstr /i /v "Tables_in" | findstr /i /v "Database"

REM Clean up temporary directory
rmdir /s /q "%TEMP_DIR%"

echo.
echo ================================================
echo          RESTORE COMPLETED SUCCESSFULLY
echo ================================================
echo [INFO] Backup used: %BACKUP_FILE%
echo [INFO] Restore time: %date% %time:~0,8%

pause