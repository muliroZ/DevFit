# 📦 Entidades do Módulo de Pedidos — Documentação Técnica

O módulo de pedidos é composto por duas entidades principais:

- **Pedido** → representa uma compra realizada por um usuário
- **ItemPedido** → representa cada produto incluído no pedido

As entidades são mapeadas com JPA e formam um relacionamento **1:N**, onde um pedido pode conter vários itens.

---

# 🧾 Entidade Pedido

A entidade `Pedido` registra informações essenciais sobre a transação, incluindo o usuário que realizou o pedido, a data de criação, os itens associados e o valor total.

## 🔧 Estrutura Geral

```java
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    private BigDecimal valorTotal;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}
````

---

## 📌 Campos da Classe

| Campo         | Tipo             | Descrição                                              |
| ------------- | ---------------- | ------------------------------------------------------ |
| `id`          | Long             | Identificador do pedido                                |
| `usuario`     | Usuario          | Usuário que realizou o pedido (obrigatório, ManyToOne) |
| `dataCriacao` | LocalDateTime    | Registrada automaticamente ao salvar o pedido          |
| `itens`       | List<ItemPedido> | Itens incluídos no pedido                              |
| `valorTotal`  | BigDecimal       | Soma dos subtotais dos itens                           |

---

## 🔗 Relacionamentos

### ✔ `@ManyToOne` com `Usuario`

* Cada pedido pertence a um único usuário
* Campo obrigatório (`nullable = false`)
* Carregamento LAZY para eficiência

### ✔ `@OneToMany` com `ItemPedido`

* Um pedido pode ter vários itens
* Itens são removidos automaticamente caso saiam da lista (`orphanRemoval = true`)
* `CascadeType.ALL` garante persistência automática dos itens

---

## 🧮 Cálculo do valor total

O campo `valorTotal` representa a soma dos subtotais de todos os itens do pedido.

O cálculo é realizado no **PedidoService**, não na entidade.

---

# 🧾 Entidade ItemPedido

A entidade `ItemPedido` representa cada produto incluído no pedido, com quantidade, valor unitário e subtotal.

## 🔧 Estrutura Geral

```java
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private BigDecimal subtotal;
}
```

---

## 📌 Campos da Classe

| Campo           | Tipo       | Descrição                             |
| --------------- | ---------- | ------------------------------------- |
| `id`            | Long       | Identificador único                   |
| `pedido`        | Pedido     | Pedido ao qual o item pertence        |
| `produto`       | Produto    | Produto selecionado no pedido         |
| `quantidade`    | Integer    | Quantidade comprada                   |
| `precoUnitario` | BigDecimal | Preço do produto no momento da compra |
| `subtotal`      | BigDecimal | quantidade × preçoUnitário            |

---

## 🔗 Relacionamentos

### ✔ `@ManyToOne` com Pedido

Cada item pertence a **um único pedido**.

### ✔ `@ManyToOne` com Produto

Cada item referencia **um único produto registrado**.

---

# 📦 Resumo das Entidades

* Pedido contém:

    * usuário
    * data de criação
    * lista de itens
    * valor total
* ItemPedido contém:

    * pedido
    * produto
    * quantidade
    * preço unitário
    * subtotal
* A remoção e persistência de itens é automatizada via cascata
* Datas são geradas automaticamente via `@PrePersist`

