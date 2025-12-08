## 📝 Documentação da Interface `FichaAvaliacaoRepository`

Esta interface é o **repositório de dados** para a entidade `FichaAvaliacao`, utilizando o Spring Data JPA. Ela gerencia as operações de persistência e consulta relacionadas às fichas de avaliação no banco de dados.

-----

### **🏷️ Informações da Interface**

| Anotação | Descrição |
| :--- | :--- |
| `@Repository` | Marca a interface como um componente de persistência (DAO/Repository) do Spring. |
| `extends JpaRepository<FichaAvaliacao, Long>` | Herda métodos CRUD, paginação e ordenação do Spring Data JPA. |
| **Parâmetros Genéricos** | A interface é parametrizada com a entidade **`FichaAvaliacao`** e o tipo de sua chave primária (**`Long`**). |

-----

### **🛠️ Métodos Fornecidos (Customizado e Herdados)**

A interface declara um método customizado baseado no nome e herda todos os métodos padrão de `JpaRepository`.

#### **1. Método Customizado (Declarado)**

| Método | Retorno | Descrição |
| :--- | :--- | :--- |
| `List<FichaAvaliacao> findByAlunoId(Long alunoId)` | `List<FichaAvaliacao>` | Este é um **Query Method** gerado automaticamente pelo Spring Data JPA. Ele consulta o banco de dados e retorna uma **lista de todas as fichas de avaliação** associadas a um determinado **ID de aluno** (`alunoId`). |

#### **2. Métodos Herdados de `JpaRepository` (Exemplos)**

| Método | Descrição |
| :--- | :--- |
| `save(FichaAvaliacao entity)` | Salva ou atualiza uma entidade `FichaAvaliacao`. |
| `findById(Long id)` | Retorna um `Optional<FichaAvaliacao>` buscando pelo ID da ficha. |
| `findAll()` | Retorna uma lista de todas as fichas de avaliação. |
| `deleteById(Long id)` | Exclui uma ficha de avaliação pelo seu ID. |

-----

### **💡 Exemplo de Uso (Contexto)**

A funcionalidade principal desta interface é permitir que um sistema **recupere o histórico de avaliações** de um aluno específico.

```java
// Exemplo de como usar em um Service

@Autowired
private FichaAvaliacaoRepository avaliacaoRepository;

public List<FichaAvaliacao> obterHistoricoAvaliacoesDoAluno(Long idAluno) {
    // Chama o método customizado
    return avaliacaoRepository.findByAlunoId(idAluno);
}
```

-----

