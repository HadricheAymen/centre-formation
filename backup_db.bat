@echo off
chcp 65001 >nul
echo ================================================
echo          DATABASE BACKUP SCRIPT
echo ================================================
echo.

REM Check if Docker Compose is running
docker compose ps | findstr "formation_db" >nul
if errorlevel 1 (
    echo [ERROR] Database container is not running!
    echo Starting database container...
    docker compose up -d db
    timeout /t 5 /nobreak >nul
)

REM Create backups directory if it doesn't exist
if not exist "backups" mkdir backups

REM Create timestamp for backup file
for /f "tokens=1-3 delims=/ " %%a in ('date /t') do set dd=%%a
for /f "tokens=1-3 delims=/ " %%a in ('date /t') do set mm=%%b
for /f "tokens=1-3 delims=/ " %%a in ('date /t') do set yy=%%c
for /f "tokens=1-2 delims=: " %%a in ('time /t') do set hh=%%a
for /f "tokens=1-2 delims=: " %%a in ('time /t') do set nn=%%b

REM Remove any leading spaces and ensure 2-digit format
set dd=%dd: =%
set mm=%mm: =%
if %dd% LSS 10 set dd=0%dd%
if %mm% LSS 10 set mm=0%mm%

set timestamp=%yy%%mm%%dd%_%hh%%nn%
set BACKUP_FILE=backup_%timestamp%.sql

echo [INFO] Creating backup: %BACKUP_FILE%

REM Execute mysqldump inside the container
echo [INFO] Running mysqldump...
docker compose exec -T db mysqldump -u root -prootpassword --routines --triggers --events centre_formation > backups\%BACKUP_FILE%

if errorlevel 1 (
    echo [ERROR] Backup failed!
    pause
    exit /b 1
)

REM Check if backup file was created successfully
if not exist "backups\%BACKUP_FILE%" (
    echo [ERROR] Backup file was not created!
    pause
    exit /b 1
)

REM Get file size before compression
for %%F in ("backups\%BACKUP_FILE%") do set "size_before=%%~zF"

REM Compress the backup file using PowerShell
echo [INFO] Compressing backup file...
powershell -Command "$ErrorActionPreference='Stop'; try { Compress-Archive -Path 'backups\%BACKUP_FILE%' -DestinationPath 'backups\%BACKUP_FILE%.zip' -Force } catch { Write-Host 'Compression failed: ' $_.Exception.Message; exit 1 }"

if errorlevel 1 (
    echo [WARNING] Compression failed, keeping uncompressed backup
    goto :backup_complete
)

REM Get file size after compression
for %%F in ("backups\%BACKUP_FILE%.zip") do set "size_after=%%~zF"

REM Convert bytes to MB
set /a size_before_mb=(%size_before% + 1048575) / 1048576
set /a size_after_mb=(%size_after% + 1048575) / 1048576

REM Delete uncompressed SQL file if compression succeeded
del "backups\%BACKUP_FILE%"
set FINAL_FILE=%BACKUP_FILE%.zip

:backup_complete
echo [SUCCESS] Backup completed successfully!
echo [INFO] Backup saved to: backups\%FINAL_FILE%
echo [INFO] Backup size: %size_after_mb% MB (was %size_before_mb% MB)
echo [INFO] Backup timestamp: %dd%/%mm%/%yy% %hh%:%nn%

REM List recent backups
echo.
echo [INFO] Recent backups:
dir backups\backup_*.zip /b /o-d 2>nul | head -5

pause