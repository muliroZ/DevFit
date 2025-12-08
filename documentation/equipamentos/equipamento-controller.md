## Overview

O `EquipamentoController` gerencia a interface de comunicação para todas as operações relacionadas à **Gestão de Equipamentos** da academia. Ele é crucial para o controle de inventário e para a visualização do status dos ativos (máquinas, pesos, etc.) no painel de controle.

### Estrutura e Segurança

- **Endpoint Raiz:** `/dashboard/equipamentos`

- **Responsabilidade:** CRUD (Criação e Leitura) de equipamentos.


---

## Dependências (Service Injetado)

O Controller depende do serviço de lógica de negócios para realizar suas operações:

|**Serviço**|**Função**|
|---|---|
|**`EquipamentoService`**|Responsável por implementar as regras de negócio para a gestão de equipamentos, como validação de dados, salvamento no repositório e transformação de dados para DTOs de dashboard.|

---

## Endpoints (Rotas API)

### 1. Listar Todos os Equipamentos para o Dashboard (Read)

Esta rota é utilizada pelo painel de controle para exibir o inventário completo de equipamentos da academia, incluindo informações de status para manutenção.

Rota: GET /dashboard/equipamentos

Acesso Requerido: GESTOR (@PreAuthorize("hasRole('GESTOR')"))

|**Detalhes**|**Descrição**|
|---|---|
|**Função**|Recupera a lista completa de equipamentos formatada para exibição no Dashboard.|
|**Retorno**|`ResponseEntity<List<EquipamentoDashboardDTO>>`|
|**DTO de Retorno**|**`EquipamentoDashboardDTO`**: Objeto otimizado contendo apenas os campos necessários para visualização no dashboard (ex: nome, ID, status de manutenção).|
|**Lógica**|O Controller invoca `equipamentoService.listarTodosParaDashboard()`. Se a lista estiver vazia, retorna **HTTP 204 No Content**. Caso contrário, retorna **HTTP 200 OK** com os dados.|

### 2. Criar Novo Equipamento (Create)

Esta rota permite o cadastro de um novo item de equipamento no sistema de inventário da academia.

Rota: POST /dashboard/equipamentos/criar

Acesso Requerido: Não especificado no código (presumivelmente aberto ou gerenciado por filtro, mas geralmente exige GESTOR).

|**Detalhes**|**Descrição**|
|---|---|
|**Função**|Persiste um novo equipamento no banco de dados.|
|**Corpo da Requisição**|**`@RequestBody @Valid EquipamentoRequest`**: O corpo deve conter os dados de cadastro do equipamento. A anotação **`@Valid`** garante que as regras de validação (ex: campos obrigatórios) definidas no DTO sejam aplicadas.|
|**DTO de Requisição**|**`EquipamentoRequest`**: Contém os campos necessários para a criação do equipamento (ex: nome, data de compra, custo).|
|**Retorno**|`ResponseEntity<Equipamento>` (Status **HTTP 201 Created**).|
|**Lógica**|Utiliza `equipamentoService.salvar(request)` para processar a requisição e retorna o objeto `Equipamento` salvo (Entidade).|