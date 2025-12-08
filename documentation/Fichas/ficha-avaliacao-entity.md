
# 📘 Documentação — Classe `FichaAvaliacao`

```java
package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ficha_avaliacao")
public class FichaAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Usuario instrutor;

    @Column(nullable = false)
    private LocalDate dataAvaliacao;

    @Column(nullable = false) private double pesoKg;
    @Column(nullable = false) private double alturaCm;
    @Column(nullable = false) private double imc;

    @Column private double circunferenciaCinturaCm;
    @Column private double circunferenciaAbdomenCm;
    @Column private double circunferenciaQuadrilCm;

    @Column(length = 1000, columnDefinition = "TEXT")
    private String historicoSaude;

    @Column(length = 500, columnDefinition = "TEXT")
    private String observacoesGerais;

    @PrePersist
    public void prePersist() {
        if (dataAvaliacao == null) {
            dataAvaliacao = LocalDate.now();
        }
    }
}
````

---

## 📌 Descrição Geral

A classe **FichaAvaliacao** representa o registro de uma avaliação física realizada em um aluno por um instrutor dentro do sistema DevFit.
Ela armazena dados antropométricos, histórico de saúde, medidas corporais e informações gerais referentes à avaliação física.

É uma entidade JPA persistida na tabela **`ficha_avaliacao`**.

---

## 🧩 Anotações Utilizadas

| Anotação                                                                   | Função                                                              |
| -------------------------------------------------------------------------- | ------------------------------------------------------------------- |
| `@Entity`                                                                  | Indica que a classe será mapeada como uma tabela no banco de dados. |
| `@Table(name = "ficha_avaliacao")`                                         | Especifica o nome da tabela no banco.                               |
| `@Id`                                                                      | Define o atributo como chave primária.                              |
| `@GeneratedValue(IDENTITY)`                                                | Estratégia de auto incremento.                                      |
| `@ManyToOne`                                                               | Relacionamento muitos-para-um com a entidade `Usuario`.             |
| `@JoinColumn`                                                              | Define a coluna correspondente ao relacionamento.                   |
| `@Column`                                                                  | Configura propriedades da coluna no banco.                          |
| `@PrePersist`                                                              | Executa lógica antes da inserção da entidade no banco.              |
| Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`) | Remove boilerplate criando getters, setters e construtores.         |

---

## 📑 Atributos Detalhados

### `id : Long`

* **Descrição:** Identificador único da ficha de avaliação.
* **Regras:** Auto gerado pelo banco.
* **Anotações:** `@Id`, `@GeneratedValue`.

---

### `aluno : Usuario`

* **Descrição:** Aluno avaliado.
* **Relacionamento:** Muitos avaliações podem pertencer a um mesmo aluno.
* **Regras:** Obrigatório.
* **Anotações:** `@ManyToOne`, `@JoinColumn(nullable = false)`.

---

### `instrutor : Usuario`

* **Descrição:** Instrutor responsável pela avaliação física.
* **Regras:** Obrigatório.
* **Relacionamento:** Muitos registros podem ser feitos pelo mesmo instrutor.
* **Anotações:** `@ManyToOne`, `@JoinColumn(nullable = false)`.

---

### `dataAvaliacao : LocalDate`

* **Descrição:** Data da avaliação física.
* **Comportamento automático:** Se não informado, é definido automaticamente para a data atual.
* **Anotação especial:** `@PrePersist`.

---

### **Medidas corporais obrigatórias**

| Atributo   | Tipo   | Descrição                    | Obrigatório |
| ---------- | ------ | ---------------------------- | ----------- |
| `pesoKg`   | double | Peso corporal em quilogramas | ✔️          |
| `alturaCm` | double | Altura em centímetros        | ✔️          |
| `imc`      | double | Índice de Massa Corporal     | ✔️          |

---

### **Circunferências (opcionais)**

| Atributo                  | Tipo   | Descrição         |
| ------------------------- | ------ | ----------------- |
| `circunferenciaCinturaCm` | double | Medida da cintura |
| `circunferenciaAbdomenCm` | double | Medida do abdômen |
| `circunferenciaQuadrilCm` | double | Medida do quadril |

---

### `historicoSaude : String`

* **Descrição:** Histórico médico e de saúde do aluno.
* **Tipo:** Texto longo (até 1000 caracteres).
* **Opcional.**

---

### `observacoesGerais : String`

* **Descrição:** Comentários, observações e recomendações do instrutor.
* **Tipo:** Texto até 500 caracteres.
* **Opcional.**

---

## 🏗 Estrutura da Tabela no Banco de Dados

| Campo                     | Tipo   | Regras                 |
| ------------------------- | ------ | ---------------------- |
| id                        | BIGINT | PK, auto_increment     |
| aluno_id                  | BIGINT | FK → Usuario, NOT NULL |
| instrutor_id              | BIGINT | FK → Usuario, NOT NULL |
| data_avaliacao            | DATE   | NOT NULL               |
| peso_kg                   | DOUBLE | NOT NULL               |
| altura_cm                 | DOUBLE | NOT NULL               |
| imc                       | DOUBLE | NOT NULL               |
| circunferencia_cintura_cm | DOUBLE | NULL                   |
| circunferencia_abdomen_cm | DOUBLE | NULL                   |
| circunferencia_quadril_cm | DOUBLE | NULL                   |
| historico_saude           | TEXT   | NULL                   |
| observacoes_gerais        | TEXT   | NULL                   |

---

## 🧾 Exemplo JSON da Entidade

```json
{
  "id": 12,
  "aluno": {
    "id": 4,
    "nome": "Carlos Henrique"
  },
  "instrutor": {
    "id": 1,
    "nome": "João Silva"
  },
  "dataAvaliacao": "2025-02-10",
  "pesoKg": 78.5,
  "alturaCm": 179,
  "imc": 24.5,
  "circunferenciaCinturaCm": 82,
  "circunferenciaAbdomenCm": 88,
  "circunferenciaQuadrilCm": 95,
  "historicoSaude": "Aluno relata dores ocasionais na lombar.",
  "observacoesGerais": "Boa postura geral. Recomendado iniciar fortalecimento de core."
}
```

---


