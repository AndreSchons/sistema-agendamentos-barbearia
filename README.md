# API de Agendamento para Barbearia

Esta é uma API RESTful desenvolvida em Java com Spring Boot para gerenciar agendamentos em uma barbearia. O sistema permite que clientes agendem serviços com barbeiros específicos, validando a disponibilidade de horários para evitar conflitos.

## ✨ Funcionalidades Principais

*   **Autenticação e Autorização**: Sistema seguro utilizando Spring Security e JSON Web Tokens (JWT).
*   **Criação de Agendamentos**: Permite agendar um serviço com um barbeiro, cliente e horário específicos.
*   **Validação de Conflitos**: A API verifica automaticamente se o barbeiro já possui um agendamento no horário solicitado, evitando agendamentos duplicados (*double booking*).
*   **Consulta de Horários Disponíveis**: Verifica e retorna os horários livres de um barbeiro para um serviço em uma data específica.
*   **Cancelamento de Agendamentos**: Permite o cancelamento de um agendamento existente.
*   **Consulta de Agendamentos**: Busca de um agendamento específico pelo seu ID.
*   **Atualização Automática de Status**: Um processo automatizado (tarefa agendada) roda a cada minuto para marcar os agendamentos que já ocorreram como "Concluídos".

## 🛠️ Tecnologias Utilizadas

*   **Java 17+**
*   **Spring Boot**: Framework principal para a construção da aplicação.
*   **Spring Data JPA**: Para persistência de dados e comunicação com o banco de dados.
*   **Maven**: Gerenciador de dependências e build do projeto.
*   **Banco de Dados**: A aplicação é configurada para se conectar a um banco de dados relacional (ex: H2, PostgreSQL, MySQL).
*   **Swagger / OpenAPI**: Para documentação viva e interativa da API.
*   **JUnit 5 & Mockito**: Frameworks utilizados para a criação de testes unitários e mocks.

## 🚀 Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd agendamento
    ```

2.  **Configure o Banco de Dados:**
    Abra o arquivo `src/main/resources/application.properties` e configure as propriedades de conexão com o seu banco de dados (URL, usuário e senha).

3.  **Compile e Execute a Aplicação com Maven:**
    ```bash
    mvn spring-boot:run
    ```

4.  A API estará disponível em `http://localhost:8080`.

## ✅ Testes Automatizados

A API possui uma cobertura abrangente de **testes unitários** implementados com **JUnit 5** e **Mockito**. Os testes validam as regras de negócio em todas as camadas de serviço, garantindo a confiabilidade do sistema tanto em cenários de sucesso quanto de falha.

Para executar a suíte de testes, utilize o comando:

```bash
mvn test
```

## 🔐 Segurança

A API utiliza **Spring Security** para proteger os endpoints. A autenticação é baseada em **JSON Web Tokens (JWT)**.

Para acessar os endpoints protegidos, você deve primeiro se autenticar através do endpoint `/auth/login` para obter um token. Em seguida, inclua este token no cabeçalho `Authorization` de todas as requisições subsequentes.

**Exemplo de Cabeçalho:**
`Authorization: Bearer <seu-jwt-token>`

## 📄 Documentação Interativa (Swagger UI)

O projeto integra o **Swagger (OpenAPI)** para fornecer uma documentação detalhada e interativa. Através dele, é possível visualizar todos os endpoints, modelos de dados e testar as requisições em tempo real.

*   **Acesso**: `http://localhost:8080/swagger-ui/index.html`
*   **Autenticação no Swagger**:
    Para testar endpoints que exigem segurança:
    1.  Realize o login no endpoint `/auth/login` para receber seu token JWT.
    2.  No topo da página do Swagger, clique no botão **Authorize**.
    3.  Insira o token no campo apropriado (geralmente no formato `Bearer <seu_token>`).
    4.  Agora você pode executar as requisições protegidas diretamente pela interface.

##  Endpoints da API

A seguir estão os detalhes dos endpoints disponíveis na API.

---

### 1. Autenticação

Autentica um usuário e retorna um token JWT.

*   **URL**: `/auth/login`
*   **Método**: `POST`
*   **Corpo da Requisição (Request Body)**:
    ```json
    {
      "username": "user@email.com",
      "password": "your_password"
    }
    ```
*   **Resposta de Sucesso (200 OK)**:
    `{"token": "eyJhbGciOiJIUzI1NiJ9..."}`

---

### 2. Criar um novo Agendamento

Cria um novo agendamento para um cliente com um barbeiro e serviço específicos. O sistema valida se o horário está disponível.

*   **URL**: `/schedulings`
*   **Método**: `POST`
*   **Corpo da Requisição (Request Body)**:

    ```json
    {
      "customerId": 1,
      "barberId": 1,
      "serviceTypeId": 2,
      "startTime": "2025-12-20T10:00:00"
    }
    ```
*   **Cabeçalho de Autenticação**:
    ```
    Authorization: Bearer <seu-jwt-token>
    ```

*   **Resposta de Sucesso (201 Created)**:

    ```json
    {
      "id": 101,
      "customerName": "Nome do Cliente",
      "barberName": "Nome do Barbeiro",
      "serviceName": "Corte de Cabelo",
      "startTime": "2025-12-20T10:00:00",
      "endTime": "2025-12-20T10:30:00",
      "status": "SCHEDULED"
    }
    ```
*   **Resposta de Erro (409 Conflict)**: Retornada se o horário não estiver disponível.
*   **Resposta de Erro (404 Not Found)**: Retornada se o cliente, barbeiro ou serviço não forem encontrados.

---

### 3. Cancelar um Agendamento

Altera o status de um agendamento existente para `CANCELLED`.

*   **URL**: `/schedulings/{id}/cancel`
*   **Método**: `PATCH`
*   **Parâmetros de URL**:
    *   `id` (obrigatório): O ID do agendamento a ser cancelado.
*   **Cabeçalho de Autenticação**:
    ```
    Authorization: Bearer <seu-jwt-token>
    ```
*   **Resposta de Sucesso (204 No Content)**: O corpo da resposta estará vazio, indicando que a operação foi bem-sucedida.
*   **Resposta de Erro (404 Not Found)**: Retornada se o agendamento com o ID informado não for encontrado.

---

### 4. Obter Horários Disponíveis

Retorna uma lista de horários disponíveis para um barbeiro, em uma data específica e para um determinado tipo de serviço.

*   **URL**: `/available-slots`
*   **Método**: `GET`
*   **Parâmetros da Query (Query Params)**:
    *   `barberId` (obrigatório): ID do barbeiro.
    *   `serviceTypeId` (obrigatório): ID do tipo de serviço.
    *   `date` (obrigatório): A data para a consulta (formato: `YYYY-MM-DD`).
*   **Exemplo de URL**: `/available-slots?barberId=1&serviceTypeId=2&date=2025-12-20`
*   **Resposta de Sucesso (200 OK)**:
    ```json
    [
        "09:00:00",
        "09:30:00",
        "11:00:00"
    ]
    ```

---

### 5. Obter Agendamento por ID

Recupera os detalhes de um agendamento específico.

*   **URL**: `/schedulings/{id}`
*   **Método**: `GET`
*   **Parâmetros de URL**:
    *   `id` (obrigatório): O ID do agendamento.
*   **Resposta de Sucesso (200 OK)**:
*   **Cabeçalho de Autenticação**:
    ```
    Authorization: Bearer <seu-jwt-token>
    ```

    ```json
    {
      "id": 101,
      "customerName": "Nome do Cliente",
      "barberName": "Nome do Barbeiro",
      "serviceName": "Corte de Cabelo",
      "startTime": "2025-12-20T10:00:00",
      "endTime": "2025-12-20T10:30:00",
      "status": "SCHEDULED"
    }
    ```
*   **Resposta de Erro (404 Not Found)**: Retornada se o agendamento com o ID informado não for encontrado.

## ⚙️ Processos em Background

### Atualização de Status de Agendamentos

A classe `SchedulingService` contém um método anotado com `@Scheduled` que é executado a cada minuto.

```java
@Scheduled(fixedRate = 60000)
public void updateStatusForCompletedSchedules() { ... }
```

Esta tarefa busca por todos os agendamentos com status `SCHEDULED` cuja data/hora de término já passou e atualiza seu status para `COMPLETED`. Isso garante que o sistema reflita o estado real dos agendamentos sem a necessidade de intervenção manual.
