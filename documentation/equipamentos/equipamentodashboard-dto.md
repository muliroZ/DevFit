## Overview

`EquipamentoDashboardDTO` é um **DTO (Data Transfer Object)** implementado como um **Record** imutável. Sua principal finalidade é servir como o objeto de resposta (saída) para a listagem de equipamentos, otimizado para a exibição em **painéis de controle (Dashboards)**.

Este DTO garante que apenas os dados essenciais para visualização e identificação do equipamento sejam expostos ao frontend, sem a necessidade de transferir toda a complexidade da entidade de banco de dados.

---

## Estrutura de Campos (Dados Essenciais)

O DTO encapsula os seguintes atributos da Entidade `Equipamento`:

|**Campo**|**Tipo**|**Descrição**|
|---|---|---|
|**`id`**|`Long`|Identificador único do equipamento no sistema.|
|**`nome`**|`String`|Nome do equipamento (ex: "Esteira Pro X9", "Halter de 10kg").|
|**`descricao`**|`String`|Descrição detalhada ou modelo do equipamento.|
|**`quantidade`**|`Integer`|A quantidade disponível deste item (útil para itens repetitivos como halteres).|
|**`valor`**|`BigDecimal`|O custo de aquisição ou valor de mercado do equipamento.|
|**`dataAquisao`**|`LocalDate`|A data em que o equipamento foi adquirido.|

---

## Método Estático de Mapeamento (`fromEntity`)

O DTO inclui um método estático de fábrica para facilitar a conversão da Entidade de persistência (`Equipamento`) para o DTO.

### 1. Assinatura do Método

Java

```
public static EquipamentoDashboardDTO fromEntity(Equipamento equipamento)
```

### 2. Função

O método recebe um objeto da **Entidade `Equipamento`** (proveniente da camada Model/Repository) e o transforma em um novo objeto **`EquipamentoDashboardDTO`**.

Este padrão de conversão:

1. **Desacopla** o DTO da Entidade, permitindo que a Entidade mude sem afetar o formato da API.

2. **Simplifica** o código na camada Service, que pode chamar esse método diretamente após recuperar dados do banco.


## Uso

Este DTO é tipicamente utilizado no endpoint:

- **Controller:** `EquipamentoController`

- **Rota:** `GET /dashboard/equipamentos`

- **Serviço:** O `EquipamentoService` é responsável por chamar esse método de mapeamento após buscar os dados no repositório.