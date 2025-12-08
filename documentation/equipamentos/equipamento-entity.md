## Overview

A classe `Equipamento` representa o **modelo de dados (Entidade)** para um item de equipamento físico dentro do sistema de gestão DevFit. Esta entidade é a representação direta de uma tabela no banco de dados, sendo crucial para o **inventário e controle de ativos** da academia.

### Mapeamento e Persistência

- **Finalidade:** Armazenar dados sobre cada item de equipamento (máquinas, pesos, acessórios).

- **Anotação de Persistência:** `@Entity` (Indica ao JPA/Hibernate que esta classe deve ser mapeada para uma tabela no banco de dados).

- **Anotações Lombok:** `@Getter`, `@Setter`, `@AllArgsConstructor`, e `@NoArgsConstructor` são usadas para gerar automaticamente os métodos _getters_, _setters_, e construtores, simplificando o código.


---

## Atributos e Estrutura de Dados

A tabela abaixo descreve os campos (colunas) que compõem a Entidade `Equipamento`:

|**Campo**|**Tipo**|**Persistência (JPA)**|**Descrição**|
|---|---|---|---|
|**`id`**|`Long`|`@Id`, `@GeneratedValue`|**Chave Primária** e Identificador único do equipamento. A estratégia `IDENTITY` indica que o valor é gerado automaticamente pelo banco de dados.|
|**`nome`**|`String`|Coluna|Nome de identificação do equipamento (Ex: "Halter 10kg").|
|**`descricao`**|`String`|Coluna|Detalhes adicionais ou modelo do equipamento.|
|**`quantidade`**|`Integer`|Coluna|Quantidade disponível deste item (útil para itens em lote).|
|**`valor`**|`BigDecimal`|Coluna|Custo de aquisição do equipamento.|
|**`dataAquisicao`**|`LocalDate`|Coluna|Data em que o equipamento foi comprado e adicionado ao inventário.|
|**`status`**|`String`|Coluna|Condição atual do equipamento (Ex: "Ativo", "Em Manutenção", "Baixado").|

---

## Relação com Outras Camadas

- **DTOs:** O `Equipamento` é mapeado para o `EquipamentoDashboardDTO` (para visualização) e é criado a partir do `EquipamentoRequest` (via `EquipamentoMapper`).

- **Repositório:** O `EquipamentoRepository` é responsável por realizar as operações CRUD (salvar, buscar, atualizar) nesta Entidade.

- **Serviço:** O `EquipamentoService` manipula objetos `Equipamento` para aplicar regras de negócio, como registrar a necessidade de manutenção ou calcular a depreciação.