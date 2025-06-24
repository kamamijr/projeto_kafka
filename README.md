# transforme essa explicacao em um md simplicado levando o que ja esta na explicacao de base:

"""

## 📌 README.md – Projeto Mensageria em Java (V1)

### 🖥️ Disciplina: Software Concorrente e Distribuído

### 📚 Curso: Bacharelado em Engenharia de Software

---

## ✅ Visão Geral

Este projeto é uma prova de conceito de microserviços distribuídos, em que cada serviço é desacoplado e comunica-se de forma assíncrona através de **Apache Kafka**. O fluxo completo é:

1. **Order-Service**
   - Recebe requisições HTTP `POST /orders`
   - Cria um objeto `Order` com ID, timestamp e lista de itens
   - Publica esse objeto em JSON no tópico Kafka `orders`
2. **Inventory-Service**
   - Lê mensagens JSON de `orders`
   - Converte cada JSON em um DTO local `OrderDTO` (para não depender de classes de outro serviço)
   - Simula reserva de estoque (80% de chance de sucesso)
   - Cria um objeto `InventoryEvent` e publica em JSON no tópico `inventory-events`
3. **Notification-Service**
   - Lê mensagens JSON de `inventory-events`
   - Converte cada JSON em um DTO local `InventoryEvent`
   - Loga uma mensagem simulando envio de e-mail/SMS

Cada serviço roda em **porta distinta** (8383, 8081, 8082) para evitar conflitos e facilitar testes paralelos.

---

## ✅ Requisitos

- **Java 11+** e **Apache Maven 3.6+**
- **Docker** + **Docker Compose** (v3.8+)
- Cliente HTTP: **curl**, **Postman** ou **Insomnia**
- (Windows) Git Bash ou PowerShell para executar scripts

---

## 🚀 Como Rodar

### 1. Clonar o repositório

```bash
git clone https://github.com/kamamijr/projeto_kafka.git
cd projeto_kafka
2. Subir Kafka e criar tópicos

chmod +x infra/setup_inicial.sh
./infra/setup_inicial.sh

O que acontece:
cd infra
docker-compose up -d sobe Zookeeper, Kafka e Kafka-UI

Aguarda ~15 s para Kafka ficar disponível

Cria os tópicos orders e inventory-events com 3 partições cada

Por que isso importa

Evita erros de “tópico não encontrado”

Kafka-UI em http://localhost:8080 permite inspecionar mensagens

3. Iniciar os serviços Java
Em três terminais distintos:

# 3.1 Order-Service
cd order-service
mvn clean spring-boot:run

# 3.2 Inventory-Service
cd inventory-service
mvn clean spring-boot:run

# 3.3 Notification-Service
cd notification-service
mvn clean spring-boot:run

Detalhes técnicos:
Cada serviço usa Spring Boot e spring-kafka

Configuração de Kafka em application.yml

Serialização de mensagens em JSON com JsonSerializer/JsonDeserializer

🧪 Testando o Fluxo
Enviando um pedido (Linux/macOS)
curl -i -X POST http://localhost:8383/orders \
  -H "Content-Type: application/json" \
  -d '{
        "items":[
          { "sku":"ABC", "qty":2 },
          { "sku":"XYZ", "qty":1 }
        ]
      }'
Enviando um pedido (Windows CMD)
curl -i -X POST "http://localhost:8383/orders" -H "Content-Type: application/json" -d "{\"items\":[{\"sku\":\"ABC\",\"qty\":2},{\"sku\":\"XYZ\",\"qty\":1}]"

O que acontece:
Order-Service retorna 200 OK e JSON do pedido criado.

Inventory-Service recebe o JSON, desserializa em OrderDTO e publica InventoryEvent.

Notification-Service recebe o JSON, desserializa em seu InventoryEvent local, e loga:

Notification: Pedido <ID> está com status 'SUCCESS'. Enviando e-mail/SMS...
🔧 Pontos Importantes do Desenvolvimento
DTOs locais em cada serviço (OrderDTO, InventoryEvent) garantem que não haja dependência de classes de outros módulos.

JSON puro:

Producer: JsonSerializer serializa objetos em JSON sem cabeçalhos de tipo.

Consumer: JsonDeserializer usa spring.json.value.default.type para mapear o JSON no DTO correto.

Sem cabeçalhos de tipo (spring.json.add.type.headers=false): simplifica a interoperabilidade entre serviços.

auto-offset-reset=latest: faz com que cada consumidor ignore mensagens antigas (que poderiam ter formatos diferentes) e processe apenas novas.

Script robusto setup_inicial.sh:

Limpa volumes antigos (docker-compose down -v)

Espera containers e a porta 9092 estarem prontos antes de criar tópicos

Portas distintas: facilita testes locais sem colisão de URLs.

📂 Estrutura de Pastas
.
├── .gitignore
├── README.md
├── infra/
│   ├── docker-compose.yml
│   └── setup_inicial.sh
├── order-service/
│   ├── pom.xml
│   └── src/
├── inventory-service/
│   ├── pom.xml
│   └── src/
└── notification-service/
    ├── pom.xml
    └── src/


```
