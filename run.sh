#!/bin/bash

PROJECT_DIR="$HOME/IdeaProjects/ResQ360"
SRC_DIR="$PROJECT_DIR/src"
OUT_DIR="$PROJECT_DIR/class"
LIB_PATH="$PROJECT_DIR/lib"
ENV_FILE="$SRC_DIR/resources/config/.env"
MAIN_CLASS="Main"

# Charger les variables d'environnement
[ -f "$ENV_FILE" ] && echo "Chargement des variables d'environnement..." && export $(grep -v '^#' "$ENV_FILE" | xargs)

# Compilation
echo "Compilation du projet..."
mkdir -p "$OUT_DIR"
find "$SRC_DIR" -name "*.java" > sources.txt

if ! javac --module-path "$LIB_PATH" \
           --add-modules javafx.controls,javafx.fxml,javafx.web \
           -d "$OUT_DIR" @sources.txt; then
    echo "Erreur de compilation"
    rm sources.txt
    exit 1
fi

rm sources.txt

# Lancement
echo "Lancement de l'application..."
cd "$OUT_DIR" && \
java --module-path "$LIB_PATH" \
     --add-modules javafx.controls,javafx.fxml,javafx.web \
     "$MAIN_CLASS"
