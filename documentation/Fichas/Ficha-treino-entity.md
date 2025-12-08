
# 📄 Documentação da Classe `FichaTreino`

## 🧩 Visão Geral

A classe **`FichaTreino`** representa a ficha de treinamento de um aluno dentro do sistema **DevFit**.
Ela registra o aluno, instrutor responsável, datas importantes e a lista de exercícios (itens de treino) pertencentes à ficha.

---

## 📌 Anotação da Entidade

| Anotação                        | Descrição                                      |
| ------------------------------- | ---------------------------------------------- |
| `@Entity`                       | Define que a classe é uma entidade JPA.        |
| `@Table(name = "ficha_treino")` | Especifica o nome da tabela no banco de dados. |

---

## 🏷️ Atributos

### 🔑 `id`

* **Tipo:** `Long`
* **Anotações:** `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`
* **Descrição:** Identificador único da ficha de treino.

---

### 🧍 `aluno`

* **Tipo:** `Usuario`
* **Anotações:** `@ManyToOne`, `@JoinColumn(name = "aluno_id", nullable = false)`
* **Descrição:** Aluno ao qual a ficha pertence. Deve existir e não pode ser nulo.

---

### 🧑‍🏫 `instrutor`

* **Tipo:** `Usuario`
* **Anotações:** `@ManyToOne`, `@JoinColumn(name = "instrutor_id", nullable = false)`
* **Descrição:** Instrutor responsável pela ficha de treino.

---

### 📅 `dataCriacao`

* **Tipo:** `LocalDate`
* **Descrição:** Data em que a ficha foi registrada no sistema.

    * Valor preenchido automaticamente no método `@PrePersist`.

---

### 📆 `dataVencimento`

* **Tipo:** `LocalDate`
* **Descrição:** Data-limite de validade da ficha de treino.

---

### 🔄 `isAtiva`

* **Tipo:** `boolean`
* **Valor padrão:** `true`
* **Descrição:** Indicador se a ficha está ativa.

---

### 📝 `listaDeItens`

* **Tipo:** `List<ItemTreino>`
* **Anotações:**

    * `@OneToMany(mappedBy = "fichaTreino", cascade = CascadeType.ALL, orphanRemoval = true)`
* **Descrição:** Lista contendo cada exercício, séries, repetições etc.

    * A relação é do tipo **um para muitos**.
    * Remoções são propagadas automaticamente (`orphanRemoval = true`).

---

## ⚙️ Método de ciclo de vida

### `@PrePersist`

```java
@PrePersist
public void prePersist() {
    if (this.dataCriacao == null) {
        this.dataCriacao = LocalDate.now();
    }
}
```

* Preenche automaticamente a data de criação quando a entidade for persistida pela primeira vez.

---

## 📌 Resumo Geral

| Campo          | Tipo             | Obrigatório  | Observações                 |
| -------------- | ---------------- | ------------ | --------------------------- |
| id             | Long             | ✔️           | Auto-gerado                 |
| aluno          | Usuario          | ✔️           | Relacionamento ManyToOne    |
| instrutor      | Usuario          | ✔️           | Relacionamento ManyToOne    |
| dataCriacao    | LocalDate        | ✔️ automático | Preenchido no `@PrePersist` |
| dataVencimento | LocalDate        | ✔️           | Opcional                    |
| isAtiva        | boolean          | ✔️           | Default: `true`             |
| listaDeItens   | List<ItemTreino> | ✔️           | Itens ligados à ficha       |

---

