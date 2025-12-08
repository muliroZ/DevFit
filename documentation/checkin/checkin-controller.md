## Overview

O `CheckinController` é a camada da API dedicada a gerenciar o **fluxo de registro de presença e acesso** dos usuários na academia (o processo de _check-in_). Ele lida com a entrada de dados do usuário e orquestra a lógica de validação na camada de Serviço.

### Estrutura e Segurança

- **Endpoint Raiz:** `/checkin`

- **Responsabilidade:** Registrar entradas e saídas e fornecer dados de fluxo (picos).

- **Segurança:** Utiliza autenticação baseada em token para registrar o check-in do usuário logado e restringe o acesso a dados de picos apenas para Gestores.


---

## Dependências (Services Injetados)

O Controller depende de dois serviços de lógica de negócios:

|**Serviço**|**Função**|
|---|---|
|**`CheckinService`**|Contém a lógica primária para validar, criar e consultar os registros de `Checkin`.|
|**`UsuarioService`**|Utilizado para obter o `ID` interno do usuário a partir das credenciais de segurança (`Username`).|

---

## Endpoints (Rotas API)

### 1. Registrar Check-in (Entrada/Saída)

Esta rota permite que um usuário autenticado registre sua entrada ou saída da academia.

Rota: POST /checkin

Acesso Requerido: Usuário autenticado (autenticação via token/sessão).

|**Detalhes**|**Descrição**|
|---|---|
|**Função**|Cria um novo registro de `Checkin` no sistema, marcando o tipo como `ENTRADA` ou `SAIDA`.|
|**Corpo da Requisição**|**`@RequestBody CheckinRequest`**: Espera um DTO contendo o tipo de check-in (`"ENTRADA"` ou `"SAIDA"`).|
|**Segurança**|**`@AuthenticationPrincipal UserDetails`**: Recupera automaticamente as informações do usuário logado (token) para garantir a identidade do solicitante.|
|**Lógica**|O Controller extrai o `usuarioId` usando o `usuarioService`. O `checkinService.registrarCheckin()` aplica a regra de negócio (ex: verificar se a mensalidade está em dia) antes de salvar o registro.|
|**Retorno**|`ResponseEntity<Checkin>` (Status **HTTP 200 OK**) com o objeto `Checkin` recém-criado.|

### 2. Obter Estatísticas de Horário de Pico

Esta rota fornece dados analíticos sobre o fluxo de pessoas na academia.

Rota: GET /checkin/picos

Acesso Requerido: GESTOR (@PreAuthorize("hasRole('GESTOR')"))

|**Detalhes**|**Descrição**|
|---|---|
|**Função**|Retorna uma lista de estatísticas que detalham a contagem de check-ins por hora para uma determinada data, identificando os horários de maior movimento.|
|**Parâmetros**|**`@RequestParam LocalDate data`** (Opcional): A data para a qual os dados de pico devem ser consultados. Formato esperado é ISO (`YYYY-MM-DD`). Se omitida, a consulta usa a data atual (`LocalDate.now()`).|
|**Retorno**|`ResponseEntity<?>` (Lista de DTOs de Estatísticas de Pico).|
|**Lógica**|Utiliza o `checkinService.getPeakHoursStats(data)` para buscar os dados agregados.|

### 3. Listar Últimos Check-ins (Top 20)

Esta rota é utilizada para monitoramento rápido dos eventos mais recentes.

Rota: GET /checkin/listar

Acesso Requerido: Não especificado no código (presumivelmente aberto ou definido por outra configuração global).

|**Detalhes**|**Descrição**|
|---|---|
|**Função**|Retorna uma lista dos 20 registros de `Checkin` mais recentes no sistema.|
|**Retorno**|`List<Checkin>`|
|**Lógica**|Chama `checkinService.listarTop20()`, que deve implementar a lógica de ordenação e limite de resultados no Repositório.|