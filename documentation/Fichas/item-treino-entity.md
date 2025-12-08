## 🏋️‍♀️ Documentação da Classe `ItemTreino`

Esta classe representa um item específico dentro de uma ficha de treino, detalhando um exercício com suas configurações de séries, repetições, carga e observações. É uma entidade persistente mapeada para a tabela `"item_treino"` no banco de dados.

---

### **🏷️ Informações da Entidade**

| Anotação | Descrição |
| :--- | :--- |
| `@Entity` | Declara que a classe é uma entidade JPA. |
| `@Table(name = "item_treino")` | Mapeia a entidade para a tabela com o nome `"item_treino"`. |
| `@Getter`, `@Setter` | Geram métodos *getter* e *setter* para todos os campos (Lombok). |
| `@AllArgsConstructor` | Gera um construtor com todos os argumentos (Lombok). |
| `@NoArgsConstructor` | Gera um construtor vazio (sem argumentos) (Lombok). |

---

### **🧱 Atributos (Campos)**

| Atributo | Tipo | Anotações JPA | Descrição |
| :--- | :--- | :--- | :--- |
| `id` | `Long` | `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)` | **Identificador único** do item de treino. É a chave primária e é gerado automaticamente pelo banco de dados. |
| `series` | `int` | `@Column(nullable = false)` | **Número de séries** a serem executadas para o exercício. Não pode ser nulo. |
| `repeticoes` | `int` | `@Column(nullable = false)` | **Número de repetições** por série. Não pode ser nulo. |
| `cargaEstimadaKg` | `double` | `@Column` | **Carga estimada** (em quilogramas) a ser usada no exercício. |
| `observacoes` | `String` | `@Column(length = 500, columnDefinition = "TEXT")` | Campo para **observações adicionais** ou instruções sobre a execução do exercício. Suporta até 500 caracteres, definido como `TEXT` no banco de dados para maior flexibilidade. |
| `fichaTreino` | `FichaTreino` | `@ManyToOne`, `@JoinColumn(name = "ficha_treino_id", nullable = false)`, `fetch = FetchType.LAZY` | **Relacionamento Many-to-One** com a entidade `FichaTreino`. Indica a qual ficha de treino este item pertence. O carregamento é **`LAZY`** (preguiçoso). |
| `exercicioBase` | `Exercicio` | `@ManyToOne`, `@JoinColumn(name = "exercicio_base_id", nullable = false)`, `fetch = FetchType.EAGER` | **Relacionamento Many-to-One** com a entidade `Exercicio`. Indica o **exercício base** (nome, tipo, etc.) que está sendo configurado. O carregamento é **`EAGER`** (imediato). |

---

### **🔗 Relacionamentos**

A classe `ItemTreino` atua como uma **tabela de ligação** ou item em uma relação, conectando um exercício base (`Exercicio`) a uma ficha de treino específica (`FichaTreino`).

1.  **Com `FichaTreino` (Many-to-One - LAZY):**
    * Muitos `ItemTreino` estão associados a uma única `FichaTreino`.
    * O *Fetch Type* `LAZY` significa que a entidade `FichaTreino` só será carregada do banco de dados quando for explicitamente acessada (ex: `itemTreino.getFichaTreino()`).

2.  **Com `Exercicio` (Many-to-One - EAGER):**
    * Muitos `ItemTreino` usam o mesmo `Exercicio` base.
    * O *Fetch Type* `EAGER` significa que a entidade `Exercicio` será carregada **imediatamente** junto com o `ItemTreino`, garantindo que as informações básicas do exercício (nome, etc.) estejam disponíveis.

---

### **💡 Exemplo de Uso (Conceitual)**

Um objeto `ItemTreino` representaria algo como:

* **Exercício Base:** Agachamento Livre
* **Séries:** 4
* **Repetições:** 12
* **Carga Estimada (Kg):** 50.0
* **Observações:** Descer até 90 graus, manter a postura.
* **Ficha de Treino:** Treino de Perna A (de 01/12/2025)

---

