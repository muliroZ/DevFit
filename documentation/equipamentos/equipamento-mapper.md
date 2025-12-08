## Overview

O `EquipamentoMapper` é uma classe de utilidade (`@Component`) dedicada à **conversão de objetos** no contexto da gestão de equipamentos. Especificamente, ele é responsável por transformar o **DTO de Requisição** (`EquipamentoRequest`) na **Entidade** (`Equipamento`) que será persistida no banco de dados.

Este padrão de Mapeador é essencial para manter a separação de responsabilidades. Ele garante que a camada de Serviço (`EquipamentoService`) trabalhe com os dados estruturados da requisição (DTO) e, no momento de salvar, utilize a Entidade (`Model`), isolando a lógica de conversão em um único local.

---

## Detalhes Técnicos e Dependências

|**Detalhe**|**Descrição**|
|---|---|
|**Localização**|`src/main/java/com/devfitcorp/devfit.mappers/EquipamentoMapper.java`|
|**Componente**|`@Component` (É um componente Spring gerenciável).|
|**Função Principal**|Converter **`EquipamentoRequest`** $\rightarrow$ **`Equipamento`**|
|**DTO Envolvido**|**`EquipamentoRequest`** (Objeto de entrada, imutável, com dados da requisição).|
|**Entidade Envolvida**|**`Equipamento`** (Objeto de destino, mutável, mapeado para a tabela do banco de dados).|

---

## Método de Mapeamento: `toEntity()`

O método `toEntity` executa a criação e o preenchimento da Entidade `Equipamento` a partir dos dados contidos na requisição de entrada.

### 1. Assinatura do Método

Java

```
public Equipamento toEntity(EquipamentoRequest request)
```

### 2. Processo de Mapeamento

1. **Instanciação:** Cria uma nova instância da Entidade `Equipamento` (`Equipamento equipamento = new Equipamento();`).

2. **Transferência de Atributos:** Os dados são copiados diretamente dos métodos _accessor_ do `EquipamentoRequest` (que é um Record) para os métodos _setter_ da Entidade `Equipamento`.


|**Origem (EquipamentoRequest)**|**Destino (Equipamento - Entidade)**|
|---|---|
|`request.nome()`|`equipamento.setNome()`|
|`request.descricao()`|`equipamento.setDescricao()`|
|`request.quantidade()`|`equipamento.setQuantidade()`|
|`request.valor()`|`equipamento.setValor()`|
|`request.dataAquisicao()`|`equipamento.setDataAquisicao()`|
|`request.status()`|`equipamento.setStatus()`|

### Benefício

Ao centralizar essa lógica de mapeamento, o código da camada de Serviço permanece limpo e focado nas regras de negócio, simplesmente chamando: `Equipamento equipamento = mapper.toEntity(request);` antes de chamar o Repositório para salvar.