# Projeto: Mensageria em Java (V1)

Este repositório contém uma prova de conceito em Java usando **Apache Kafka** para processar pedidos em tempo real.  
Foco da **v1**: atender aos requisitos funcionais RF-1 a RF-4 :contentReference[oaicite:0]{index=0}.

## Serviços

1. **Order-Service**  
   - Endpoint: `POST /orders`  
   - Gera UUID, timestamp e lista de itens  
   - Publica em tópico `orders` (≥ 3 partitions) :contentReference[oaicite:1]{index=1}

2. **Inventory-Service**  
   - Consome de `orders` por `orderId`  
   - Reserva estoque (simulado)  
   - Publica resultado (`SUCCESS`|`FAILURE`) em `inventory-events` :contentReference[oaicite:2]{index=2}

3. **Notification-Service**  
   - Consome de `inventory-events`  
   - Registra no console a notificação (e-mail/SMS simulado) :contentReference[oaicite:3]{index=3}

## Infraestrutura local

- **Docker Compose** com Kafka e ZooKeeper
- Script para criar tópicos em `infra/create-topics.sh`

## Como executar

1. `cd infra && ./create-topics.sh && docker-compose up -d`  
2. Em cada serviço (`order-service`, etc):  
   ```bash
   mvn spring-boot:run
