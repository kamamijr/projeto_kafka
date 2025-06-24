#!/usr/bin/env bash

# setup_inicial.sh - Script para provisionar infraestrutura e criar tópicos Kafka

set -e

echo "=============================================="
echo "Iniciando Zookeeper, Kafka e Kafka UI..."
echo "=============================================="
docker-compose -f infra/docker-compose.yml up -d

echo "=============================================="
echo "Aguardando Kafka iniciar (aguarde ~15 segundos)..."
echo "=============================================="
sleep 15

echo "=============================================="
echo "Criando tópicos Kafka: orders e inventory-events"
echo "=============================================="

docker-compose -f infra/docker-compose.yml exec kafka kafka-topics \
  --create --if-not-exists \
  --topic orders \
  --partitions 3 \
  --replication-factor 1 \
  --bootstrap-server kafka:29092

docker-compose -f infra/docker-compose.yml exec kafka kafka-topics \
  --create --if-not-exists \
  --topic inventory-events \
  --partitions 3 \
  --replication-factor 1 \
  --bootstrap-server kafka:29092

echo "=============================================="
echo "Tópicos criados com sucesso!"
echo "Kafka UI disponível em: http://localhost:8080"
echo "=============================================="
