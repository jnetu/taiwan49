#!/usr/bin/env bash
# run.sh — compila e roda o Taiwan49 no macOS / Linux
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
LIB_DIR="$PROJECT_DIR/lib"
RES_DIR="$PROJECT_DIR/res"
OUT_DIR="$PROJECT_DIR/out/classes"
MAIN_CLASS="main.Game"

# ── Compilação ─────────────────────────────────────────────────────────────
echo "[1/3] Compilando..."
mkdir -p "$OUT_DIR"

SOURCES=$(find "$SRC_DIR" -name "*.java")
CP=$(find "$LIB_DIR" -name "*.jar" | tr '\n' ':')

javac -d "$OUT_DIR" -cp "$CP" $SOURCES
echo "      OK"

# ── Recursos ───────────────────────────────────────────────────────────────
echo "[2/3] Copiando recursos..."
cp -r "$RES_DIR"/. "$OUT_DIR"/
echo "      OK"

# ── Execução ───────────────────────────────────────────────────────────────
echo "[3/3] Iniciando o jogo..."

OS="$(uname -s)"

if [ "$OS" = "Darwin" ]; then
    # macOS: GLFW exige -XstartOnFirstThread passado diretamente ao java,
    # não via re-launch. Usamos 'exec' para substituir o processo shell
    # pelo java, mantendo o working directory intacto.
    exec java \
        -XstartOnFirstThread \
        -cp "$OUT_DIR:$CP" \
        "$MAIN_CLASS"
else
    exec java \
        -cp "$OUT_DIR:$CP" \
        "$MAIN_CLASS"
fi
