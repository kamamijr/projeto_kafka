## 📌 README.md – Projeto de Mensageria com Kafka (Java)

### 🖥️ Disciplina: Software Concorrente e Distribuído

### 📚 Curso: Bacharelado em Engenharia de Software

### 👥 Integrantes:

- Guilherme Gonçalves Dutra de Mendonça [202201692]
- Hugo Moreno I Veiga Jardim [202201693]
- Mikael Borges de Oliveira e Silva Junior [202201708]

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

## 🚀 Como Rodar o Projeto

### 1. Clonar o Repositório

```bash
git clone https://github.com/kamamijr/projeto_kafka.git
cd projeto_kafka
```

chmod +x infra/startup_infra.sh
./infra/startup_infra.sh

### 2. Subir o Kafka e Criar os Tópicos

```bash
chmod +x infra/startup_infra.sh
./infra/startup_infra.sh
```

#### 🔍 O que esse script faz:

- Entra na pasta `infra`
- Sobe os containers do **Zookeeper**, **Kafka** e **Kafka-UI**:

  ```bash
  cd infra
  docker-compose up -d
  ```

- Aguarda \~15 segundos até o Kafka estar disponível
- Cria os tópicos `orders` e `inventory-events`, cada um com **3 partições**

#### ✅ Por que isso importa:

- Evita erros como **"tópico não encontrado"**
- Kafka-UI disponível em: [http://localhost:8080](http://localhost:8080)

---

### 3. Iniciar os Serviços Java

> ⚠️ Execute cada serviço em **um terminal separado**:

#### 3.1 Order-Service

```bash
cd order-service
mvn clean spring-boot:run
```

#### 3.2 Inventory-Service

```bash
cd inventory-service
mvn clean spring-boot:run
```

#### 3.3 Notification-Service

```bash
cd notification-service
mvn clean spring-boot:run
```

#### ⚙️ Detalhes Técnicos:

- Cada serviço usa **Spring Boot** com **spring-kafka**
- Configuração do Kafka feita via `application.yml`
- Mensagens serializadas em **JSON** com `JsonSerializer` / `JsonDeserializer`

---

### 🔪 Testando o Fluxo

#### Enviar um Pedido (Linux/macOS)

```bash
curl -i -X POST http://localhost:8383/orders \
  -H "Content-Type: application/json" \
  -d '{
        "items":[
          { "sku":"ABC", "qty":2 },
          { "sku":"XYZ", "qty":1 }
        ]
      }'
```

#### Enviar um Pedido (Windows CMD)

```cmd
curl -i -X POST "http://localhost:8383/orders" -H "Content-Type: application/json" -d "{\"items\":[{\"sku\":\"ABC\",\"qty\":2},{\"sku\":\"XYZ\",\"qty\":1}]}"
```

#### 📬 O que acontece:

- **Order-Service** retorna `200 OK` com o JSON do pedido criado.
- **Inventory-Service** consome o JSON, desserializa em `OrderDTO` e publica um `InventoryEvent`.
- **Notification-Service** consome o evento, desserializa em `InventoryEvent` e loga:

```bash
Notification: Pedido <ID> está com status 'SUCCESS'. Enviando e-mail/SMS...
```

---

### 🔧 Pontos Importantes do Desenvolvimento

- **DTOs locais** em cada serviço (`OrderDTO`, `InventoryEvent`) evitam dependência entre módulos.

- **JSON puro:**

  - `JsonSerializer` envia dados em JSON sem cabeçalhos de tipo.
  - `JsonDeserializer` usa `spring.json.value.default.type` para mapear JSON para DTO.
  - `spring.json.add.type.headers=false`: evita cabeçalhos extras, melhorando a interoperabilidade.

- **auto-offset-reset=latest**: garante que consumidores leiam apenas mensagens recentes, ignorando antigas com formatos diferentes.

- **Script startup_infra.sh:**

  - Remove volumes antigos com `docker-compose down -v`
  - Espera os containers e a porta `9092` estarem prontos antes de criar tópicos

- **Portas distintas** para os serviços: facilita testes locais sem conflitos

---

### 📁 Estrutura de Pastas

```
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

## Diagramas

## Diagrama de Classes

![Diagrama-de-classes2-mensageria-java](https://github.com/user-attachments/assets/0b78ef1d-0d5a-49b3-9f7d-02d381aa5b98)

## Diagrama de Sequência

![Diagrama-de-sequência-mensageria-java](https://github.com/user-attachments/assets/af5cc485-5dda-4802-8d77-93a3153047c1)

## Diagrama de Casos de Uso

![Diagrama-casos-de-uso-mensageria-java](https://github.com/user-attachments/assets/c7b9b7ce-bc39-40aa-a466-79f5c1ba2b5f)

## Requisitos Não Funcionais

## ✅ 1. Escalabilidade – Como conseguir com Kafka?

O **Apache Kafka** é altamente escalável por natureza, pois suporta:

- **Partições por tópico**: cada tópico (como `orders` e `inventory-events`) pode ter múltiplas partições, permitindo que várias instâncias de consumidores leiam dados em paralelo.
- **Grupos de consumidores**: serviços como o `Inventory-Service` podem ser replicados (escalados horizontalmente), e o Kafka balanceia automaticamente as partições entre as instâncias do mesmo grupo (`inventory-group`).
- **Produtores e consumidores independentes**: os serviços são desacoplados, o que permite escalar cada um separadamente conforme a carga (por exemplo, posso escalar o `Notification-Service` sem impactar os demais).

---

## ✅ 2. Tolerância à falha – O que significa e como o Kafka ajuda?

**Tolerância à falha** é a capacidade de o sistema continuar funcionando mesmo que partes dele falhem.

Se o `Inventory-Service` cair enquanto consome o tópico `orders`, o Kafka **mantém as mensagens na fila**. Quando o serviço for reiniciado, ele continuará consumindo a partir do **último offset salvo**.

Além disso, o Kafka oferece:

- **Configuração de `acks`, `retries` e armazenamento persistente**, garantindo que mensagens não se percam em falhas.
- **Replicação de partições**, aumentando a resiliência caso um nó do Kafka falhe (ex: `--replication-factor=2` ou `3`, com múltiplos brokers).

---

## ✅ 3. Idempotência – O que é e como garantir?

**Idempotência** significa que a repetição da mesma operação **tem o mesmo efeito** da primeira execução.

Em sistemas distribuídos, mensagens podem ser reenviadas. Então é essencial evitar efeitos colaterais duplicados.

Por exemplo:  
O `Inventory-Service` pode receber duas vezes o mesmo pedido. Para ser idempotente, é necessário verificar se o `orderId` **já foi processado** antes de reservar estoque novamente ou publicar outro evento.

### 🛠️ Como garantir:

- Usar um **banco de dados** ou **cache** (como Redis ou Postgres) para registrar pedidos processados.
- Em sistemas simples, usar um **mapa em memória** para guardar `orderId`s já tratados.

Além disso:

- Os **producers Kafka** suportam `enable.idempotence=true`, que evita que mensagens duplicadas sejam publicadas, mesmo em caso de falhas ou reenvios.

## 🧪 Teste de Integração Automatizado

O projeto inclui um teste de integração no `inventory-service` que valida o fluxo real de eventos entre os microserviços usando o Kafka rodando via Docker. O teste publica um pedido no tópico `orders` e verifica se o evento correspondente é publicado corretamente no tópico `inventory-events`. Isso garante que o sistema está funcionando de ponta a ponta, simulando o ambiente de produção.
