# API de Agendamento para Barbearia

Esta é uma API RESTful desenvolvida em Java com Spring Boot para gerenciar agendamentos em uma barbearia. O sistema permite que clientes agendem serviços com barbeiros específicos, validando a disponibilidade de horários para evitar conflitos.

## ✨ Funcionalidades Principais

*   **Criação de Agendamentos**: Permite agendar um serviço com um barbeiro, cliente e horário específicos.
*   **Validação de Conflitos**: A API verifica automaticamente se o barbeiro já possui um agendamento no horário solicitado, evitando agendamentos duplicados (*double booking*).
*   **Cancelamento de Agendamentos**: Permite o cancelamento de um agendamento existente.
*   **Consulta de Agendamentos**: Busca de um agendamento específico pelo seu ID.
*   **Atualização Automática de Status**: Um processo automatizado (tarefa agendada) roda a cada minuto para marcar os agendamentos que já ocorreram como "Concluídos".

## 🛠️ Tecnologias Utilizadas

*   **Java 17+**
*   **Spring Boot**: Framework principal para a construção da aplicação.
*   **Spring Data JPA**: Para persistência de dados e comunicação com o banco de dados.
*   **Maven**: Gerenciador de dependências e build do projeto.
*   **Banco de Dados**: A aplicação é configurada para se conectar a um banco de dados relacional (ex: H2, PostgreSQL, MySQL).

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

## 📖 Endpoints da API

A seguir estão os detalhes dos endpoints disponíveis na API.

---

### 1. Criar um novo Agendamento

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

### 2. Cancelar um Agendamento

Altera o status de um agendamento existente para `CANCELLED`.

*   **URL**: `/schedulings/{id}/cancel`
*   **Método**: `PATCH`
*   **Parâmetros de URL**:
    *   `id` (obrigatório): O ID do agendamento a ser cancelado.
*   **Resposta de Sucesso (204 No Content)**: O corpo da resposta estará vazio, indicando que a operação foi bem-sucedida.
*   **Resposta de Erro (404 Not Found)**: Retornada se o agendamento com o ID informado não for encontrado.

---

### 3. Obter Agendamento por ID

Recupera os detalhes de um agendamento específico.

*   **URL**: `/schedulings/{id}`
*   **Método**: `GET`
*   **Parâmetros de URL**:
    *   `id` (obrigatório): O ID do agendamento.
*   **Resposta de Sucesso (200 OK)**:

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
