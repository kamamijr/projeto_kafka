## 📌 README.md – Projeto Mensageria em Java (V1)

### 🖥️ Disciplina: Software Concorrente e Distribuído  
### 📚 Curso: Bacharelado em Engenharia de Software  

---  

## ✅ Resumo do Projeto

Este projeto é uma prova de conceito de **microserviços distribuídos** usando **Apache Kafka** como backbone de mensageria. Até agora você conta com:

1. **Docker Compose** que orquestra Zookeeper, Kafka e Kafka-UI  
2. **Setup script** (`setup_inicial.sh`) para subir tudo e criar tópicos  
3. **Skeletons Java** de três serviços Spring Boot:
   - **Order-Service** → publica pedidos em `orders`  
   - **Inventory-Service** → consome `orders` e publica em `inventory-events`  
   - **Notification-Service** → consome `inventory-events` e loga notificações  

O objetivo desta V1 é atender aos requisitos funcionais **RF-1** a **RF-4** de forma simples e colaborativa em GitHub.

---

## ✅ Requisitos para rodar

- **Java 11 ou superior**  
- **Apache Maven 3.6+**  
- **Docker** + **Docker Compose** (v3.8+)  
- **curl** (ou Postman / Insomnia) para testar APIs REST  

---

## ✅ Passo a passo de execução

### 📂 1. Clonar repositório
```bash
git clone https://github.com/seu-usuario/turing.git
cd turing
