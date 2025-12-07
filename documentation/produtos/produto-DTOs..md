# 🧾 ProdutoDTOs — Documentação Técnica

Os DTOs (Data Transfer Objects) do módulo de produtos são responsáveis por representar os dados que entram e saem dos endpoints do `ProdutoController`.
Eles permitem que a API:

* Controle exatamente o que recebe e o que retorna
* Mantenha a entidade de domínio (`Produto`) isolada da camada externa
* Aplique validações claras e consistentes

O módulo de produtos utiliza dois DTOs:

* **ProdutoRequest** → Entrada da API
* **ProdutoResponse** → Saída da API

---

# 📥 ProdutoRequest — Dados de Entrada

`ProdutoRequest` é utilizado para **criação** e **atualização** de produtos.
Ele contém validações essenciais para garantir que apenas dados coerentes cheguem ao service.

---

## 🔧 Estrutura

```java
public record ProdutoRequest(
        @NotBlank
        @Size(min = 1, max = 100)
        String nome,

        @Size(max = 500)
        String descricao,

        @NotNull
        @PositiveOrZero
        BigDecimal preco,

        @NotNull
        @PositiveOrZero
        Integer estoque,

        @Size(max = 500)
        String imagemUrl
) {}
```

---

## 📌 Campos

| Campo     | Tipo       | Validações                    | Obrigatório | Descrição                                |
| --------- | ---------- | ----------------------------- | ----------- | ---------------------------------------- |
| nome      | String     | `@NotBlank`, `@Size`          | Sim         | Nome do produto (único no banco).        |
| descricao | String     | `@Size(max = 500)`            | Não         | Descrição detalhada do produto.          |
| preco     | BigDecimal | `@NotNull`, `@PositiveOrZero` | Sim         | Preço do produto, não pode ser negativo. |
| estoque   | Integer    | `@NotNull`, `@PositiveOrZero` | Sim         | Quantidade disponível em estoque.        |
| imagemUrl | String     | `@Size(max = 500)`            | Não         | URL opcional para imagem do produto.     |

---

## 🔍 Como funciona na execução

1. O controller recebe o JSON da requisição.
2. O Spring valida automaticamente com `@Valid`.
3. Se houver erro → `MethodArgumentNotValidException` (400).
4. Se estiver tudo certo → encaminha ao `ProdutoService`.

---

# 📤 ProdutoResponse — Dados de Saída

`ProdutoResponse` representa o formato retornado pela API para qualquer consulta ou operação de criação/atualização.

Ele evita expor a entidade diretamente e padroniza o retorno.

---

## 🔧 Estrutura

```java
public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        String imagemUrl
) {}
```

---

## 📌 Campos

| Campo     | Tipo       | Descrição                       |
| --------- | ---------- | ------------------------------- |
| id        | Long       | Identificador único do produto. |
| nome      | String     | Nome do produto.                |
| descricao | String     | Descrição detalhada.            |
| preco     | BigDecimal | Preço atual do produto.         |
| estoque   | Integer    | Quantidade em estoque.          |
| imagemUrl | String     | URL da imagem, caso fornecida.  |

---

# 🔁 Relação entre ProdutoRequest e ProdutoResponse

* **Request → usado para enviar dados ao sistema**
* **Response → usado para retornar dados ao cliente**

O mapper é responsável pela conversão entre os dois.

---

# 🧠 Regras Importantes

### ✔ Validação ocorre antes do service

O service só recebe dados já validados.

### ✔ Entidade nunca é exposta

Sempre retornamos `ProdutoResponse`, nunca `Produto`.

### ✔ Campos opcionais

* `descricao`
* `imagemUrl`

### ✔ Campos obrigatórios

* `nome`
* `preco`
* `estoque`

---

# 📦 Resumo do Módulo DTO

* Define contratos claros de entrada e saída
* Garante segurança e consistência na API
* Trabalha em conjunto com Bean Validation
* Facilita integração com o `ProdutoMapper`
* Torna o controller mais limpo e expressivo

---
