## 📝 Documentação da Interface `FichaTreinoRepository`

Esta interface é o **repositório de dados** para a entidade `FichaTreino` e é responsável por gerenciar as operações de persistência e consulta relacionadas às fichas de treino no banco de dados, utilizando o Spring Data JPA.

---

### **🏷️ Informações da Interface**

| Anotação | Descrição |
| :--- | :--- |
| `@Repository` | Indica que esta interface é um componente de persistência (DAO/Repository) do Spring. |
| `extends JpaRepository<FichaTreino, Long>` | Herda todos os métodos **CRUD** (Create, Read, Update, Delete) básicos, além das funcionalidades de paginação e ordenação. |
| **Parâmetros Genéricos** | A interface é parametrizada com a entidade **`FichaTreino`** e o tipo da sua chave primária (**`Long`**). |

---

### **🛠️ Métodos Fornecidos (Customizado e Herdados)**

A interface declara um método customizado baseado no nome e herda todos os métodos padrão de `JpaRepository`.

#### **1. Método Customizado (Declarado)**

| Método | Retorno | Descrição |
| :--- | :--- | :--- |
| `List<FichaTreino> findByAlunoId(Long alunoId)` | `List<FichaTreino>` | Este é um **Query Method** gerado automaticamente pelo Spring Data JPA. Ele busca e retorna uma **lista de todas as fichas de treino** que estão associadas a um determinado **ID de aluno** (`alunoId`). |

#### **2. Métodos Herdados de `JpaRepository` (Exemplos)**

| Método | Descrição |
| :--- | :--- |
| `save(FichaTreino entity)` | Salva ou atualiza uma entidade `FichaTreino` (o plano de treino). |
| `findById(Long id)` | Retorna um `Optional<FichaTreino>` buscando pelo ID da ficha. |
| `findAll()` | Retorna uma lista de todas as fichas de treino cadastradas. |
| `deleteById(Long id)` | Exclui uma ficha de treino pelo seu ID. |

---

### **💡 Contexto de Uso**

O método customizado é crucial para a funcionalidade de listagem, permitindo que a aplicação encontre rapidamente **todas as fichas de treino ativas ou passadas** de um aluno específico, facilitando a visualização e gestão dos seus planos de exercício.

---

