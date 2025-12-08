## 💾 Documentação da Interface `ExercicioRepository`

Esta interface é o **repositório de dados** para a entidade `Exercicio`, utilizando o Spring Data JPA. Ela facilita a comunicação entre a aplicação e o banco de dados para operações CRUD (Create, Read, Update, Delete) e consultas customizadas relacionadas aos exercícios.

-----

### **🏷️ Informações da Interface**

| Anotação | Descrição |
| :--- | :--- |
| `@Repository` | Indica que esta interface é um componente de persistência (DAO/Repository) do Spring. |
| `extends JpaRepository<Exercicio, Long>` | Herda todos os métodos CRUD básicos e funcionalidades de paginação e ordenação do Spring Data JPA. |
| **Parâmetros Genéricos** | A interface é parametrizada com a entidade **`Exercicio`** e o tipo da sua chave primária (**`Long`**). |

-----

### **🛠️ Métodos Fornecidos (Customizados e Herdados)**

Esta interface fornece, implicitamente, uma vasta gama de métodos, além do método customizado declarado:

#### **1. Método Customizado (Declarado)**

| Método | Retorno | Descrição |
| :--- | :--- | :--- |
| `Exercicio findExercicioById(Long id)` | `Exercicio` | Consulta o banco de dados para encontrar e retornar um exercício específico baseado no seu **identificador (`id`)**. Este é um *query method* gerado automaticamente pelo nome. |

#### **2. Métodos Herdados de `JpaRepository` (Exemplos)**

| Método | Descrição |
| :--- | :--- |
| `save(Exercicio entity)` | Salva ou atualiza uma entidade `Exercicio` no banco de dados. |
| `findById(Long id)` | Retorna um `Optional<Exercicio>` contendo o exercício pelo ID. |
| `findAll()` | Retorna uma lista de todas as entidades `Exercicio` no banco de dados. |
| `delete(Exercicio entity)` | Exclui uma entidade `Exercicio`. |

-----

### **💡 Exemplo de Uso (Conceitual)**

Esta interface seria injetada em um **Service Layer** da aplicação para realizar operações de persistência, como:

```java
// Dentro de uma classe de serviço...

@Autowired
private ExercicioRepository exercicioRepository;

public Exercicio buscarPorId(Long id) {
    // Utiliza o método customizado
    return exercicioRepository.findExercicioById(id);
}

public Exercicio salvarNovoExercicio(Exercicio exercicio) {
    // Utiliza o método herdado
    return exercicioRepository.save(exercicio);
}
```

-----

