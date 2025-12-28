# 💈 API de Agendamento para Barbearia

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

Esta é uma API RESTful robusta desenvolvida em **Java** com **Spring Boot** para gerenciar agendamentos em uma barbearia. O sistema oferece controle total sobre horários, barbeiros e serviços, garantindo a integridade dos dados e prevenindo conflitos de agenda (*double booking*).

---

## ✨ Funcionalidades

*   🔐 **Autenticação e Autorização**: Segurança via Spring Security e JWT.
*   📅 **Gestão de Agendamentos**: Criação, leitura e cancelamento.
*   🛡️ **Validação de Conflitos**: Algoritmo que impede agendamentos sobrepostos.
*   🔍 **Disponibilidade**: Consulta de horários livres por barbeiro e data.
*   ⏱️ **Background Jobs**: Atualização automática de status de agendamentos passados.
*   🐳 **Containerização**: Suporte completo a Docker e Docker Compose.

## ️ Tecnologias

*   **Java 17** & **Spring Boot**
*   **Spring Data JPA** & **PostgreSQL**
*   **Spring Security** & **JWT**
*   **Docker** & **Docker Compose**
*   **Swagger (OpenAPI)**
*   **JUnit 5** & **Mockito**

---

## 🚀 Como Executar

### Opção 1: Via Docker (Recomendado)

A maneira mais rápida de iniciar a aplicação com o banco de dados configurado.

1.  **Pré-requisitos**: Tenha o Docker e Docker Compose instalados.

2.  **Configuração de Ambiente**:
    Crie um arquivo `.env` na raiz do projeto (`/agendamento`) com as variáveis necessárias. Exemplo:
    ```env
    POSTGRES_USER=postgres
    POSTGRES_PASSWORD=postgres
    POSTGRES_DB=agendamento_db
    # URL para o container da API acessar o container do DB
    SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/agendamento_db
    SPRING_DATASOURCE_USERNAME=postgres
    SPRING_DATASOURCE_PASSWORD=postgres
    JWT_SECRET=SuaChaveSecretaAqui
    ```

3.  **Iniciar Serviços**:
    ```bash
    docker-compose up -d --build
    ```

4.  **Acessar**: A API estará disponível em `http://localhost:8080`.

### Opção 2: Execução Local (Maven)

1.  Certifique-se de ter um banco de dados (PostgreSQL) rodando localmente.
2.  Configure o arquivo `src/main/resources/application.properties` com as credenciais do seu banco local.
3.  Execute o comando:
    ```bash
    mvn spring-boot:run
    ```

---

## 📖 Guia de Uso da API

A documentação completa e interativa está disponível no Swagger UI.

👉 **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`

### Passo a Passo

#### 1. Autenticação (Login)
Para acessar os recursos, você precisa de um token.

*   **POST** `/auth/login`
*   **Body**:
    ```json
    {
      "username": "user@email.com",
      "password": "your_password"
    }
    ```
*   **Resposta**: Copie o token retornado (`eyJhbGci...`).

#### 2. Autorizando Requisições
No Swagger ou no seu cliente HTTP (Postman/Insomnia), adicione o cabeçalho:
`Authorization: Bearer <SEU_TOKEN_AQUI>`

#### 3. Consultar Disponibilidade
Antes de agendar, verifique os horários livres.

*   **GET** `/available-slots?barberId=1&serviceTypeId=2&date=2025-12-20`
*   **Resposta**:
    ```json
    [ "09:00:00", "09:30:00", "11:00:00" ]
    ```

#### 4. Criar Agendamento
Com um horário livre, realize o agendamento.

*   **POST** `/schedulings`
*   **Body**:
    ```json
    {
      "customerId": 1,
      "barberId": 1,
      "serviceTypeId": 2,
      "startTime": "2025-12-20T09:00:00"
    }
    ```

#### 5. Cancelar Agendamento
Se necessário, cancele o serviço.

*   **PATCH** `/schedulings/{id}/cancel`

---

## ⚙️ Processos em Background

O sistema possui um **Job Agendado** (`@Scheduled`) que roda a cada minuto.

```java
@Scheduled(fixedRate = 60000)
public void updateStatusForCompletedSchedules() {
    // Busca agendamentos 'SCHEDULED' com data/hora fim < agora
    // Atualiza status para 'COMPLETED'
}
```

Esta tarefa busca por todos os agendamentos com status `SCHEDULED` cuja data/hora de término já passou e atualiza seu status para `COMPLETED`. Isso garante que o sistema reflita o estado real dos agendamentos sem a necessidade de intervenção manual.
