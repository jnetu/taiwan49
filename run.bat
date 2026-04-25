@echo off
setlocal enabledelayedexpansion
:: run.bat — compila e roda o Taiwan49 no Windows

set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%src
set LIB_DIR=%PROJECT_DIR%lib
set RES_DIR=%PROJECT_DIR%res
set OUT_DIR=%PROJECT_DIR%out\classes
set MAIN_CLASS=main.Launcher

:: ── Compilação ──────────────────────────────────────────────────────────────
echo [1/3] Compilando...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

:: Lista todos os .java
dir /s /b "%SRC_DIR%\*.java" > sources.txt

:: Monta classpath com todos os JARs de lib\
set CP=
for %%f in ("%LIB_DIR%\*.jar") do (
    if defined CP (
        set CP=!CP!;%%f
    ) else (
        set CP=%%f
    )
)

javac -d "%OUT_DIR%" -cp "%CP%" @sources.txt
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha na compilacao.
    del sources.txt
    pause
    exit /b %ERRORLEVEL%
)
del sources.txt
echo       OK

:: ── Recursos ────────────────────────────────────────────────────────────────
echo [2/3] Copiando recursos...
xcopy /s /y /q "%RES_DIR%\*" "%OUT_DIR%\" >nul
echo       OK

:: ── Execução ────────────────────────────────────────────────────────────────
echo [3/3] Iniciando o jogo...
java -cp "%OUT_DIR%;%CP%" %MAIN_CLASS%
