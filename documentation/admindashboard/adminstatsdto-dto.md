## Overview

`AdminStatsDTO` (Data Transfer Object para Estatísticas do Administrador) é um objeto imutável (implementado como um **Record** em Java) projetado para **consolidar e transferir** todas as métricas e estatísticas de alto nível necessárias para a exibição no **Painel de Controle do Administrador/Gestor**.

Este DTO é o principal resultado da operação do `AdminDashboardService.getGeneralStats()`, fornecendo uma visão 360 graus da saúde operacional e financeira da academia em um único pacote.

---

## Estrutura de Campos (Métricas Consolidadas)

O `AdminStatsDTO` agrega métricas chave de diversas áreas do negócio:

### 1. Métricas Financeiras e de Matrícula

|**Campo**|**Tipo**|**Descrição**|
|---|---|---|
|**`faturamentoMensalPrevisto`**|`BigDecimal`|O valor total esperado (previsto) de faturamento das mensalidades para o mês corrente.|
|**`totalAlunosAtivos`**|`long`|Contagem total de alunos que possuem uma matrícula ativa.|
|**`totalAlunosInativos`**|`long`|Contagem total de alunos com matrículas inativas, canceladas ou trancadas.|
|**`taxaRetencao`**|`double`|O percentual de retenção de alunos (proporção de alunos que permanecem ativos ao longo do tempo).|

### 2. Métricas Operacionais e de Recursos

|**Campo**|**Tipo**|**Descrição**|
|---|---|---|
|**`totalUsuariosCadastrados`**|`long`|Contagem total de todas as pessoas cadastradas no sistema (inclui alunos, gestores, instrutores).|
|**`capacidadeMaxima`**|`int`|O limite máximo de pessoas que a academia pode receber (dado definido no sistema).|
|**`equipamentosEmManutencao`**|`long`|Contagem de equipamentos que estão atualmente listados como fora de uso ou em manutenção.|
|**`equipamentosTotais`**|`long`|Contagem total de todos os equipamentos cadastrados na academia.|
|**`checkinsHoje`**|`long`|Contagem total de check-ins registrados no dia atual.|

### 3. Sub-Objeto Financeiro Detalhado

|**Campo**|**Tipo**|**Descrição**|
|---|---|---|
|**`financeiroDashboardDTO`**|`FinanceiroDashboardDTO`|Um DTO aninhado que fornece métricas financeiras mais detalhadas, como receita, despesas e fluxo de caixa do período.|

---

## Uso

Este DTO é utilizado no endpoint:

- **Controller:** `AdminDashboardController`

- **Rota:** `GET /admin/dashboard/stats`

- **Serviço:** Os dados são calculados pelo `AdminDashboardService`.


O uso de um Record garante que o objeto seja imutável e leve, ideal para a transferência rápida de dados de estatísticas.