# Tech Challenge API - Fase 2

API REST para gerenciamento de restaurantes, usuários e cardápios, desenvolvida com Spring Boot, PostgreSQL e Docker.

O projeto utiliza conceitos de Clean Architecture, separando responsabilidades em camadas para melhorar organização, manutenção e escalabilidade.

---

## 🚀 Funcionalidades

- Gerenciamento de usuários
- Gerenciamento de tipos de usuários (OWNER e CUSTOMER)
- Gerenciamento de restaurantes
- Gerenciamento dos itens do cardápio
- Associação de restaurantes aos seus respectivos donos
- Associação de itens do cardápio aos restaurantes
- Validação de dados de entrada
- Tratamento padronizado de erros utilizando ProblemDetail (RFC 7807)

---

## 🏗️ Arquitetura

O projeto segue uma organização baseada em Clean Architecture:

```text
domain
 ├── model
 └── exception

application
 ├── service
 └── dto

infrastructure
 ├── repository
 ├── config
 └── exception

interfaces
 └── controller
```

### Camadas

- **Domain:** entidades e regras principais do negócio.
- **Application:** casos de uso, serviços e DTOs.
- **Infrastructure:** persistência, configurações e integrações externas.
- **Interfaces:** controllers REST responsáveis pela exposição da API.

---

## 🛠️ Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Docker
- Docker Compose
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Testcontainers
- JaCoCo
- Maven

---

## 📚 Documentação Swagger

Após subir o projeto, acesse:

http://localhost:8080/swagger-ui.html

A documentação contém:

- Endpoints disponíveis
- Contratos de request e response
- Exemplos de chamadas
- Possíveis retornos de erro

---

## 📬 Coleção Postman

Baixar coleção JSON:

https://raw.githubusercontent.com/brayan-schroeder/fiap-techchallenge-fase2/refs/heads/main/postman_collection.json

A coleção possui exemplos para teste dos endpoints disponíveis na API.

---

## 🚀 Como rodar o projeto

### Pré-requisitos

- Docker Desktop instalado
- Docker Compose habilitado

### Subir aplicação + banco de dados

Execute:

```bash
docker-compose up -d --build
```

A aplicação estará disponível em:

```text
http://localhost:8080
```

---

## 🧪 Executando os testes

O projeto possui testes unitários e testes de integração.

### Rodar todos os testes

Execute:

```bash
mvn clean test
```

Esse comando executa:

- Testes unitários dos Services
- Testes de integração dos Controllers
- Geração do relatório de cobertura JaCoCo

---

## 📊 Cobertura de testes

A cobertura é gerada utilizando JaCoCo.

Após executar:

```bash
mvn clean test
```

Abra o arquivo:

```text
target/site/jacoco/index.html
```

Resultado obtido:

```text
Cobertura total: 81%
```

Principais coberturas:

- Application Service: 96%
- Controllers: 87%
- DTOs: 100%
- Domain: 100%

---

## 🧪 Estratégia de testes

### Testes unitários

Implementados utilizando:

- JUnit 5
- Mockito

Camadas testadas:

- UserService
- UserTypeService
- RestaurantService
- MenuItemService

Os testes validam regras de negócio, cenários de sucesso e exceções.

---

### Testes de integração

Implementados utilizando:

- Spring Boot Test
- Testcontainers
- PostgreSQL

Validam o fluxo completo:

```text
HTTP Request
      ↓
Controller
      ↓
Service
      ↓
Repository
      ↓
Database
```

---

## 🗄️ Banco de dados

O banco PostgreSQL é criado automaticamente através do Docker Compose.

Principais tabelas:

- users
- user_types
- restaurants
- menu_items

---

## 📌 Repositório GitHub

https://github.com/brayan-schroeder/fiap-techchallenge-fase2