# 📦 Módulo de Produtos — Documentação Técnica

O módulo de **Produtos** é responsável por gerenciar os itens disponíveis na loja.  
Ele centraliza as operações de cadastro, listagem, atualização, exclusão e fornece dados necessários para controle de estoque pelo módulo de pedidos.

---

# 🧩 Visão Geral da Arquitetura

O módulo é composto por:

- **ProdutoController** → expõe endpoints REST
- **ProdutoService** → contém regras de negócio
- **ProdutoRepository** → abstrai acesso ao banco via Spring Data JPA
- **Produto** (entidade) → representa o produto no banco
- **DTOs** → definem entrada e saída da API
- **ProdutoMapper** → responsável por converter Entidade ↔ DTO
- Integra-se com o **GlobalExceptionHandler**, responsável por padronizar respostas de erro.

---

# 📌 Estrutura da Entidade Produto

A entidade `Produto` representa um item disponível para venda no sistema.  
Ela aplica anotações de validação de domínio e define o modelo persistido no banco usando JPA.

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false, unique = true)
    private String nome;

    @Size(max = 500)
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal preco;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer estoque;

    @Column(length = 500, columnDefinition = "TEXT")
    private String imagemUrl;
}

