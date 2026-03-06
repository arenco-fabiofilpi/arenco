#!/bin/bash

# Carrega variáveis do .env da raiz do projeto
if [ -f ".env" ]; then
  set -a
  source .env
  set +a
  echo "✔ Variáveis do .env carregadas"
else
  echo "⚠ Arquivo .env não encontrado na raiz"
fi

JAR_FILE=$(ls -t target/*.jar 2>/dev/null | head -1)

if [ -z "$JAR_FILE" ]; then
  echo "❌ Nenhum JAR encontrado em target/. Rode 'mvn package' primeiro."
  exit 1
fi

if [ "$1" == "debug" ]; then
  echo "🐛 Iniciando em modo REMOTE DEBUG (porta 5005)..."
  exec java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar "$JAR_FILE"
elif [ "$1" == "seed" ]; then
  echo "🌱 Iniciando com profile seed..."
  exec java -Dspring.profiles.active=seed -jar "$JAR_FILE"
else
  echo "🚀 Iniciando em background (nohup)..."
  nohup java -jar "$JAR_FILE" > app.log 2>&1 &
  echo "PID: $!"
  echo "Log: tail -f app.log"
fi
