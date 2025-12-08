## Overview

A interface `EquipamentoRepository` faz parte da **Camada de Repositório** (Data Access Layer) e é responsável por gerenciar a persistência e a recuperação de dados da Entidade **`Equipamento`** no banco de dados.

O uso do Spring Data JPA simplifica drasticamente a implementação, pois a interface herda métodos CRUD básicos, permitindo que o desenvolvedor se concentre em consultas personalizadas de domínio.

### Estrutura e Herança

- **Tipo:** `interface` (Não é uma classe concreta, o Spring implementa a lógica em _runtime_).

- **Componente:** `@Repository` (Marca a interface como um componente de persistência).

- **Extensão:** `extends JpaRepository<Equipamento, Long>`:

    - **`Equipamento`**: Indica a **Entidade (Model)** que este Repositório irá gerenciar.

    - **`Long`**: Indica o **Tipo da Chave Primária** da Entidade `Equipamento` (que é o `id`).


---

## Métodos de Persistência e Consulta

### 1. Métodos Herdados (CRUD Padrão)

Ao estender `JpaRepository`, a interface herda automaticamente métodos essenciais para a manipulação básica da Entidade `Equipamento`, sem a necessidade de codificação:

- `save(Equipamento)`: Cria ou atualiza um equipamento.

- `findById(Long id)`: Busca um equipamento pelo seu ID.

- `findAll()`: Retorna todos os equipamentos.

- `delete(Equipamento)`: Remove um equipamento.


### 2. Consulta Personalizada: `countByStatus()`

Este método é uma **consulta personalizada** definida usando a anotação `@Query` com a sintaxe **JPQL (Java Persistence Query Language)**.

|**Detalhes**|**Descrição**|
|---|---|
|**Assinatura**|`long countByStatus(@Param("status") String status)`|
|**Função**|Conta quantos equipamentos na base de dados possuem o `status` (ex: "Em Manutenção", "Ativo") especificado no parâmetro.|
|**JPQL**|`"SELECT COUNT(e) FROM Equipamento e WHERE e.status = :status"`|
|**Uso da Query**|Esta consulta é fundamental para a camada de Serviço (`AdminDashboardService`) obter as contagens necessárias para o `AdminStatsDTO` (ex: `equipamentosEmManutencao`).|
|**Anotação**|**`@Param("status")`**: Liga o valor do argumento Java (`status`) ao parâmetro nomeado na query JPQL (`:status`).|