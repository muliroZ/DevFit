## Overview

A classe `Checkin` representa o **registro individual de presença e acesso** de um usuário na academia. Esta entidade é crucial para a auditoria de acesso e para o cálculo de métricas de fluxo e utilização do espaço (Horários de Pico).

### Mapeamento e Persistência

- **Finalidade:** Armazenar informações sobre quem fez check-in, quando e o tipo de evento (entrada ou saída).

- **Tabela:** Mapeada para a tabela `checkins` no banco de dados (`@Table(name = "checkins")`).

- **Anotações Lombok:** `@Getter`, `@Setter`, e `@NoArgsConstructor` são usadas para gerar automaticamente os métodos _getters_, _setters_ e o construtor sem argumentos.


---

## Atributos e Estrutura de Dados

A tabela abaixo descreve os campos (colunas) que compõem a Entidade `Checkin`:

|**Campo**|**Tipo**|**Persistência (JPA)**|**Descrição**|
|---|---|---|---|
|**`id`**|`Long`|`@Id`, `@GeneratedValue`|**Chave Primária**. Identificador único do registro de check-in.|
|**`usuario`**|`Usuario`|`@ManyToOne`, `@JoinColumn`|**Chave Estrangeira**. Referência ao objeto `Usuario` que realizou o check-in. O `FetchType.LAZY` indica que o objeto `Usuario` só será carregado quando for explicitamente acessado.|
|**`dataHora`**|`LocalDateTime`|Coluna|O **carimbo de data e hora** exato em que o check-in foi registrado.|
|**`tipo`**|`CheckinType`|`@Enumerated(EnumType.STRING)`|Indica se o registro é uma `ENTRADA` ou `SAIDA`. O `EnumType.STRING` garante que o valor seja armazenado como texto no banco de dados.|

---

## Ciclo de Vida do Objeto (`@PrePersist`)

A Entidade `Checkin` possui uma lógica de ciclo de vida para garantir a precisão do registro de tempo:

- **Método:** `public void onCreate()`

- **Anotação:** **`@PrePersist`**

- **Função:** Este método é executado **automaticamente** pelo JPA imediatamente **antes** de a Entidade ser salva (persistida) no banco de dados pela primeira vez. Ele garante que o campo `dataHora` seja preenchido com o **momento exato** da criação do registro (`LocalDateTime.now()`), garantindo a integridade dos dados temporais.


---

## Relação com Outras Camadas

- **Controller:** O `CheckinController` cria e manipula objetos `Checkin` para responder às requisições do usuário.

- **Service:** O `CheckinService` utiliza esta Entidade para aplicar regras de negócio (validação de mensalidade) antes de persistir no Repositório.

- **Repositório:** O `CheckinRepository` gerencia as operações de persistência e consulta, como a contagem de registros por hora para análise de picos.