## Overview

`CheckinRequest` é um **DTO (Data Transfer Object)** implementado como um **Record** imutável. Sua principal finalidade é servir como o **objeto de requisição (entrada)** para a operação de **registro de Check-in/Check-out** no sistema DevFit.

Este DTO encapsula os dados mínimos necessários para a camada de Serviço identificar quem está realizando o registro e qual é o tipo de operação (Entrada ou Saída).

---

## Estrutura de Campos (Dados de Entrada)

O DTO contém os seguintes atributos, que são esperados no corpo da requisição HTTP (JSON):

|**Campo**|**Tipo**|**Descrição**|**Regras de Validação**|
|---|---|---|---|
|**`usuarioId`**|`Long`|Identificador único do usuário que está registrando a presença.|**`@NotNull`**: O ID não pode ser nulo.|
|**`tipo`**|`CheckinType`|O tipo de registro de presença que está sendo efetuado (Geralmente um `Enum` com valores como `ENTRADA` ou `SAIDA`).|Nenhum.|

## ❗ Regras de Validação

A anotação `@NotNull` na validação indica que, embora o `CheckinRequest` seja usado para receber dados, o `usuarioId` deve estar presente no _payload_ da requisição.

> **Observação de Uso no Controller:**
>
> No `CheckinController` documentado anteriormente, o `usuarioId` era obtido diretamente do token (`@AuthenticationPrincipal UserDetails`) e não do corpo da requisição.
>
> **Cenário 1 (Uso Prático):** Se este DTO for usado no endpoint onde o `usuarioId` é extraído do token, o campo `usuarioId` no DTO pode ser redundante ou ser usado apenas em endpoints de Gestor que registram check-ins para terceiros (neste caso, o `usuarioId` **não** viria do token, mas do corpo da requisição).
>
> **Cenário 2 (Uso Simples):** Se o DTO for usado apenas para passar o `tipo` (`ENTRADA`/`SAIDA`), o campo `usuarioId` pode ser ignorado no `POST /checkin` principal, ou a validação pode ser aplicada somente onde ele é estritamente necessário.

---

## Uso e Camadas

- **Controller:** É usado pelo `CheckinController` para mapear os dados da requisição `POST /checkin`.

- **Serviço:** O `CheckinService` recebe este DTO para aplicar a lógica de negócio (ex: verificar status da mensalidade) antes de criar o registro `Checkin`.

- **Entidade Auxiliar:** O campo `tipo` utiliza o `Enum` **`CheckinType`** (que deve estar definido na pasta `model` ou em um pacote de utilidade).