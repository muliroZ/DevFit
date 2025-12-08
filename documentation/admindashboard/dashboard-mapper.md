## Overview

O `DashboardMapper` é uma classe de utilidade (`@Component`) responsável por **mapear e converter** dados brutos ou agregados (neste caso, de um `HashMap<String, Object>`) para o formato de DTO (Data Transfer Object) estruturado, especificamente o `AdminStatsDTO`.

Sua função essencial é isolar a lógica de conversão de tipos de dados e formatação, garantindo que o `AdminDashboardService` possa se concentrar puramente na agregação dos dados sem se preocupar com a criação final do objeto de resposta.

---

## Detalhes Técnicos e Dependências

|**Detalhe**|**Descrição**|
|---|---|
|**Localização**|`src/main/java/com/devfitcorp/devfit/mappers/DashboardMapper.java`|
|**Componente**|`@Component` (Indica que é um componente Spring gerenciável e pode ser injetado automaticamente).|
|**Função Principal**|Converter `HashMap<String, Object>` $\rightarrow$ `AdminStatsDTO`|
|**DTOs Envolvidos**|**`AdminStatsDTO`** (Objeto de destino) e **`FinanceiroDashboardDTO`** (Objeto aninhado).|

---

## Método de Mapeamento: `toResponse()`

O método `toResponse` executa a lógica de conversão necessária para preencher o DTO de estatísticas do administrador.

### 1. Assinatura do Método

Java

```
public AdminStatsDTO toResponse(HashMap<String, Object> stats)
```

### 2. Processo de Conversão

O método assume que o `HashMap` de entrada (`stats`) contém todas as chaves necessárias com os dados brutos (geralmente resultados de consultas ou agregações do Repositório/Serviço).

A conversão envolve as seguintes etapas:

- **Extração e Cast Explícito:** Os valores são extraídos do `HashMap` usando a chave (`stats.get("chave")`).

- **Conversão Numérica (Tipos Long):** Para campos como `totalAlunosOn` e `checkinsHoje`, os valores são extraídos como `Object` e, em seguida, convertidos explicitamente para `Long` via `((Number) ...).longValue()`. Isso trata a possibilidade de o valor ser retornado como `Integer` ou outro subtipo de `Number` pelo processo de agregação.

- **Cast Direto:** Tipos complexos ou específicos (`BigDecimal`, `Double`, `Integer`, e o DTO aninhado `FinanceiroDashboardDTO`) são convertidos diretamente (`(Tipo) stats.get("chave")`).


### 3. Mapeamento de Chaves e Campos

|**Chave no HashMap**|**Tipo de Dados no HashMap**|**Campo no AdminStatsDTO**|
|---|---|---|
|`faturamentoMensalPrevisto`|`BigDecimal`|`faturamentoMensalPrevisto`|
|`totalAlunosOn`|`Number`|`totalAlunosAtivos`|
|`totalAlunosOff`|`Number`|`totalAlunosInativos`|
|`taxaRetencao`|`Double`|`taxaRetencao`|
|`totalAlunos`|`Number`|`totalUsuariosCadastrados`|
|`capacidadeMax`|`Integer`|`capacidadeMaxima`|
|`manutencaoEquip`|`Number`|`equipamentosEmManutencao`|
|`totalEquip`|`Number`|`equipamentosTotais`|
|`checkinsHoje`|`Number`|`checkinsHoje`|
|`financeiro`|`FinanceiroDashboardDTO`|`financeiroDashboardDTO`|

---

### Benefício

O uso deste Mapper centraliza a complexidade do _casting_ e da criação do DTO. Isso torna o `AdminDashboardService` mais limpo, focando apenas na recuperação e processamento dos dados, e não na sua representação final.