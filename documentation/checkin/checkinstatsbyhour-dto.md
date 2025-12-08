## Overview

`CheckinStatsByHourDTO` é um **DTO (Data Transfer Object)** implementado como um **Record** imutável. Sua principal finalidade é transportar dados estatísticos de **contagem de check-ins** agrupados por hora, sendo essencial para a análise do fluxo e identificação dos **horários de pico** na academia.

Este DTO é usado como o formato de retorno para a rota de análise de fluxo no `AdminDashboardController` e/ou `CheckinController`.

---

## Estrutura de Campos (Métricas de Fluxo)

O DTO contém os seguintes atributos, que representam um ponto de dado na análise de fluxo diário:

|**Campo**|**Tipo**|**Descrição**|
|---|---|---|
|**`hora`**|`int`|O valor numérico da hora do dia para a qual a contagem de check-ins foi agregada (Ex: `8` para 8:00h, `18` para 18:00h).|
|**`contagem`**|`Long`|O número total de check-ins registrados durante aquela hora específica.|

---

## Uso e Finalidade Analítica

### 1. Obtenção de Dados

Este DTO é populado pela camada de Serviço (`AdminDashboardService` ou `CheckinService`) após executar uma consulta complexa no `CheckinRepository`. Essa consulta agrega e conta todos os registros de check-in para uma data específica, agrupando-os pela hora do dia.

### 2. Análise de Horários de Pico

Ao retornar uma lista de objetos `CheckinStatsByHourDTO`, o frontend pode:

- **Visualizar em Gráfico:** Criar um gráfico de barras ou linha onde o eixo X é a `hora` e o eixo Y é a `contagem`.

- **Identificar Picos:** Determinar visualmente os períodos de maior movimento, o que é útil para a gestão de recursos (escalar pessoal, planejar manutenção de equipamentos).


### 3. Endpoint Consumidor

Este DTO é o objeto de resposta principal para a rota:

- **Controller:** `AdminDashboardController` (ou `CheckinController`)

- **Rota:** `GET /admin/dashboard/stats/picos` ou `GET /checkin/picos`