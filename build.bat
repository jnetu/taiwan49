@echo off
setlocal

:: --- CONFIGURAÇÕES ---
set APP_NAME=Taiwan49
set APP_VERSION=0.0.1
set MAIN_CLASS=main.Game
set INPUT_DIR=dist\input_jars
set OUTPUT_DIR=dist\build


:: METADADOS
set APP_VENDOR=jnetu
set APP_DESC=Taiwan 1949 Action Game
set APP_COPYRIGHT=Copyright (C) 2025 jnetu


:: tipo de build: 'app-image' (pasta para testes) ou 'exe' (instalador final)
set TYPE=app-image

echo ==========================================
echo    BUILD: %APP_NAME% (%TYPE%)
echo ==========================================

:: LIMPEZA
echo [1/5] Limpando...
if exist out rmdir /s /q out
if exist dist rmdir /s /q dist
mkdir out\classes
mkdir %INPUT_DIR%

:: COMPILAÇÃO
echo [2/5] Compilando...
dir /s /b src\*.java > sources.txt
javac -d out\classes -cp "lib\*" @sources.txt
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha na compilacao.
    del sources.txt
    pause
    exit /b %ERRORLEVEL%
)
del sources.txt

:: RECURSOS
echo [3/5] Copiando recursos...
xcopy /s /y /q res\* out\classes\

:: 4. JAR
echo [4/5] Criando JAR...
jar --create --file %INPUT_DIR%\app.jar --main-class %MAIN_CLASS% -C out\classes .
copy /y lib\*.jar %INPUT_DIR% >nul

:: JPACKAGE
echo [5/5] Rodando jpackage...

:: JPACKAGE -- configs 
jpackage ^
  --name %APP_NAME% ^
  --app-version %APP_VERSION% ^
  --input %INPUT_DIR% ^
  --main-jar app.jar ^
  --main-class %MAIN_CLASS% ^
  --type %TYPE% ^
  --dest %OUTPUT_DIR% ^
  --icon res\window\icon.ico ^
  --description "%APP_DESC%" ^
  --vendor "%APP_VENDOR%" ^
  --copyright "%APP_COPYRIGHT%" ^
  --java-options "-Djava.library.path=$APPDIR -Xmx512m"

if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha no jpackage.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ==========================================
echo    SUCESSO!
echo    Abra a pasta: %OUTPUT_DIR%\%APP_NAME%
echo    E execute o arquivo: %APP_NAME%.exe
echo ==========================================
pause