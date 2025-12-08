# **Classe `Exercicio`**

```java
package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exercicio {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String musculoPrincipal;

    @Column(length = 1000, columnDefinition = "TEXT")
    private String descricao;
}
````

---

## 📌 Descrição Geral

A classe **Exercicio** representa um exercício físico cadastrado no sistema DevFit.
Ela é uma entidade JPA usada para persistência no banco de dados e compõe fichas de treino e avaliações físicas.

---

## 🧩 Anotações Utilizadas

| Anotação                                                                   | Função                                                |
| -------------------------------------------------------------------------- | ----------------------------------------------------- |
| `@Entity`                                                                  | Mapeia a classe para uma tabela no banco.             |
| `@Id`                                                                      | Identifica a chave primária.                          |
| `@GeneratedValue(IDENTITY)`                                                | Gera o ID automaticamente.                            |
| `@Column(nullable = false, unique = true)`                                 | Nome obrigatório e único.                             |
| `@Column(length = 1000, columnDefinition = "TEXT")`                        | Permite armazenar textos longos para a descrição.     |
| Lombok (`@Getter`, `@Setter`, `@AllArgsConstructor`, `@NoArgsConstructor`) | Gera getters, setters e construtores sem boilerplate. |

---

## 📑 Atributos

### `id : Long`

* **Descrição:** Identificador único do exercício.
* **Regras:** Gerado automaticamente pelo banco.
* **Anotações:** `@Id`, `@GeneratedValue`.

---

### `nome : String`

* **Descrição:** Nome do exercício (ex.: "Supino Reto", "Agachamento Livre").
* **Regras:** Obrigatório e não pode haver nomes duplicados.
* **Anotações:** `@Column(nullable = false, unique = true)`.

---

### `musculoPrincipal : String`

* **Descrição:** Grupo muscular principal trabalhado pelo exercício.
* **Exemplos:** peito, costas, bíceps, quadríceps.
* **Regras:** Obrigatório.
* **Anotações:** `@Column(nullable = false)`.

---

### `descricao : String`

* **Descrição:** Explicação detalhada da execução do exercício.
* **Regras:** Campo opcional, limite de 1000 caracteres, armazenado como `TEXT` no banco.
* **Anotações:** `@Column(length = 1000, columnDefinition = "TEXT")`.

---

## 🏗 Estrutura da Tabela no Banco de Dados

| Campo             | Tipo    | Regras             |
| ----------------- | ------- | ------------------ |
| id                | BIGINT  | PK, auto-increment |
| nome              | VARCHAR | NOT NULL, UNIQUE   |
| musculo_principal | VARCHAR | NOT NULL           |
| descricao         | TEXT    | NULL               |

---

## 🧾 Exemplo de JSON da Entidade

```json
{
  "id": 1,
  "nome": "Supino Reto",
  "musculoPrincipal": "Peito",
  "descricao": "Deitado em um banco, empurre a barra para cima até esticar os braços."
}
```

---
```
