## Overview

O `AdminDashboardController` é o controlador responsável por fornecer todos os dados e métricas essenciais para o **Painel de Controle do Administrador/Gestor** no sistema DevFit. Ele atua como a interface entre as requisições HTTP do frontend e a camada de lógica de negócios (`AdminDashboardService` e `UsuarioService`).

### Estrutura e Segurança

- **Endpoint Raiz:** `/admin/dashboard`

- **Acesso:** Todos os endpoints requerem que o usuário tenha o papel (`Role`) de **`GESTOR`** (indicado pela anotação `@PreAuthorize("hasRole('GESTOR')")`), garantindo que apenas usuários com privilégios administrativos possam acessar as métricas sensíveis.


---

## Dependências (Services Injetados)

O Controller depende de dois serviços para executar suas operações:

|**Serviço**|**Função**|
|---|---|
|**`UsuarioService`**|Responsável pela lógica de negócios e acesso a dados relacionados aos usuários do sistema (alunos, gestores, instrutores).|
|**`AdminDashboardService`**|Responsável por agregar dados de diversas fontes e calcular as métricas e estatísticas gerais para o painel.|

---

## Endpoints (Rotas API)

### 1. Obter Lista Detalhada de Usuários Agrupados

Rota: GET /admin/dashboard/usuarios/detalhado

Acesso Requerido: GESTOR

|**Detalhes**|**Descrição**|
|---|---|
|**Função**|Retorna uma lista detalhada de todos os usuários do sistema, agrupados pelo seu papel (`Role`) (e.g., Alunos, Gestores).|
|**Retorno**|`ResponseEntity<Map<String, List<UsuarioDetalhadoDashboardDTO>>>`|
|**DTO de Retorno**|**`UsuarioDetalhadoDashboardDTO`**: Contém informações detalhadas de cada usuário, otimizadas para exibição no painel.|
|**Lógica**|Utiliza o método `usuarioService.findAndGroupByRole()` para buscar e agrupar os dados. Garante que, se nenhum dado for encontrado, retorna um mapa vazio em vez de `null`.|

### 2. Obter Estatísticas Gerais do Sistema

Rota: GET /admin/dashboard/stats

Acesso Requerido: GESTOR

|**Detalhes**|**Descrição**|
|---|---|
|**Função**|Retorna as principais métricas de alto nível da academia (KPIs) para o painel principal.|
|**Retorno**|`ResponseEntity<AdminStatsDTO>`|
|**DTO de Retorno**|**`AdminStatsDTO`**: Objeto que consolida métricas como número total de alunos, receita do mês, novos cadastros, etc.|
|**Lógica**|Utiliza `adminDashboardService.getGeneralStats()` para calcular ou recuperar as estatísticas agregadas.|

### 3. Obter Estatísticas de Pico de Check-in por Hora

Rota: GET /admin/dashboard/stats/picos

Acesso Requerido: GESTOR

|**Detalhes**|**Descrição**|
|---|---|
|**Função**|Retorna estatísticas que indicam os horários de maior fluxo (picos) de check-in na academia para uma data específica.|
|**Parâmetros**|**`@RequestParam String data`** (Opcional): Se fornecido (no formato ISO, YYYY-MM-DD), consulta os picos para essa data. Se omitido, consulta os picos da data atual (`LocalDate.now()`).|
|**Retorno**|`ResponseEntity<List<CheckinStatsByHourDTO>>`|
|**DTO de Retorno**|**`CheckinStatsByHourDTO`**: Lista de objetos, cada um contendo a hora e a contagem de check-ins naquela hora.|
|**Lógica**|Utiliza `adminDashboardService.getPeakHoursStats(dataConsulta)` para processar e retornar a análise de fluxo.|