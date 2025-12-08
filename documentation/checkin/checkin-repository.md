## Overview

A interface `CheckinRepository` é a camada de **Acesso a Dados (Repository)** responsável pela persistência e consulta dos registros da Entidade **`Checkin`**. Ela é fundamental para registrar a presença dos usuários e fornecer dados analíticos sobre o fluxo de pessoas na academia.

### Estrutura e Herança

- **Tipo:** `interface` (Implementada automaticamente pelo Spring Data JPA).

- **Entidade Gerenciada:** **`Checkin`**.

- **Chave Primária:** **`Long`**.

- **Função:** Fornece os métodos CRUD básicos e métodos de consulta personalizados para a análise de fluxo.


---

## Métodos de Consulta Personalizados

Este Repositório define métodos que vão além do CRUD básico, focando em regras de negócio e análise de dados.

### 1. Análise de Picos por Hora (`findCheckinCountByHour`)

Este método é crucial para a funcionalidade de Dashboard, permitindo a identificação dos horários de maior movimento.

|**Detalhes**|**Descrição**|
|---|---|
|**Assinatura**|`List<Object[]> findCheckinCountByHour(@Param("data") LocalDate data)`|
|**Função**|**Agrega a contagem** de check-ins por hora para uma data específica.|
|**JPQL/Native Query**|Utiliza uma **`nativeQuery = true`** para aproveitar funções específicas do banco de dados (provavelmente MySQL ou PostgreSQL) como `HOUR(c.dataHora)`.|
|**Retorno**|Uma lista de arrays de objetos (`Object[]`), onde cada array contém a hora (`hora` - int) e a contagem (`count` - long). Estes dados são mapeados posteriormente para o `CheckinStatsByHourDTO`.|

### 2. Listagem Recente (`findTop20ByOrderByIdDesc`)

Usado para mostrar um feed dos últimos eventos de check-in.

|**Detalhes**|**Descrição**|
|---|---|
|**Assinatura**|`List<Checkin> findTop20ByOrderByIdDesc()`|
|**Função**|Retorna os **20 registros de `Checkin` mais recentes** no banco de dados, ordenados pelo `id` em ordem decrescente (o ID mais alto é o mais recente).|
|**Uso**|Típico para telas de monitoramento em tempo real (como a rota `/checkin/listar` no Controller).|

### 3. Contagem Diária (`countByDataHoraBetween`)

|**Detalhes**|**Descrição**|
|---|---|
|**Assinatura**|`long countByDataHoraBetween(LocalDateTime startOfDay, LocalDateTime endOfDay)`|
|**Função**|Conta o número total de check-ins que ocorreram dentro de um intervalo de tempo, tipicamente usado para obter a **contagem total do dia**.|
|**Parâmetros**|Recebe um `LocalDateTime` que marca o início do dia (00:00:00) e o fim do dia (23:59:59).|

### 4. Prevenção de Spam ou Duplicidade Rápida (`existsByUsuarioIdAndDataHoraBetween`)

Este é um método de validação crucial para a lógica de negócio.

|**Detalhes**|**Descrição**|
|---|---|
|**Assinatura**|`boolean existsByUsuarioIdAndDataHoraBetween(Long usuarioId, LocalDateTime dezMinutosAtras, LocalDateTime agora)`|
|**Função**|**Verifica a existência** de qualquer registro de check-in para um `usuarioId` específico dentro de um pequeno intervalo de tempo recente (ex: nos últimos 10 minutos).|
|**Uso na Lógica**|É usado pelo `CheckinService` para **evitar que o usuário faça check-in/check-out repetidamente** em um período muito curto, garantindo a integridade dos dados e prevenindo erros.|