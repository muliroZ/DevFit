## Overview

`EquipamentoRequest` é um **DTO (Data Transfer Object)** implementado como um **Record** imutável. Sua principal finalidade é servir como o **objeto de requisição (entrada)** para as operações de **criação e atualização** de um equipamento no sistema, como visto no `EquipamentoController`.

Este DTO encapsula todos os dados necessários que o frontend deve enviar ao backend para cadastrar ou modificar um equipamento no inventário da academia.

---

## Estrutura de Campos (Dados de Entrada)

O DTO contém os seguintes atributos, que são esperados no corpo da requisição HTTP (JSON):

|**Campo**|**Tipo**|**Descrição**|**Requisito Típico**|
|---|---|---|---|
|**`nome`**|`String`|Nome do equipamento (ex: "Halter", "Esteira").|Obrigatório|
|**`descricao`**|`String`|Descrição detalhada ou modelo do equipamento.|Opcional/Obrigatório|
|**`quantidade`**|`Integer`|A quantidade deste item a ser cadastrada.|Obrigatório (> 0)|
|**`valor`**|`BigDecimal`|O custo de aquisição ou valor de mercado do equipamento.|Obrigatório|
|**`dataAquisicao`**|`LocalDate`|A data em que o equipamento foi adquirido (formato ISO: YYYY-MM-DD).|Obrigatório|
|**`status`**|`String`|O status inicial do equipamento (ex: "Ativo", "Novo", "Em Manutenção").|Obrigatório|

---

## Uso e Camadas

### 1. Camada Controller

O `EquipamentoController` utiliza este DTO para mapear o corpo da requisição `POST` para a criação de um novo equipamento:

Java

```
// Exemplo de uso no Controller
@PostMapping("/criar")
public ResponseEntity<Equipamento> criar(@RequestBody @Valid EquipamentoRequest request) { ... }
```

A anotação `@Valid` (assumida pela dependência `jakarta.validation`) garante que quaisquer restrições de validação (tamanho, não nulo, etc.) aplicadas aos campos deste DTO sejam verificadas antes que os dados cheguem à camada Service.

### 2. Camada Service

O `EquipamentoService` recebe este DTO, aplica a lógica de negócio (ex: verificar a data, calcular depreciação) e, em seguida, utiliza um Mapper para converter o `EquipamentoRequest` na Entidade `Equipamento` antes de salvá-la no Repositório.