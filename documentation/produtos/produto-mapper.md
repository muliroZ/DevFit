# 🧭 ProdutoMapper — Documentação Técnica

O `ProdutoMapper` é responsável por converter dados entre:

* **DTOs → Entidade (`ProdutoRequest` → `Produto`)**
* **Entidade → DTO (`Produto` → `ProdutoResponse`)**

Ele garante que o controller e o service trabalhem apenas com objetos apropriados para cada camada.

Essa classe é marcada como `@Component`, permitindo que o Spring a injete automaticamente nos serviços.

---

# 🧩 Visão Geral

O mapper possui três responsabilidades principais:

1. **Criar uma entidade a partir de um ProdutoRequest**
2. **Atualizar uma entidade existente com base em um ProdutoRequest**
3. **Transformar uma entidade Produto em um ProdutoResponse**

Ele **não possui regras de negócio**, apenas faz a transferência de dados entre objetos.

---

# 📌 Estrutura da Classe

```
@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequest dto) { ... }

    public void updateEntityFromRequest(Produto produto, ProdutoRequest dto) { ... }

    public ProdutoResponse toResponse(Produto entity) { ... }
}
```

---

# 🔧 Métodos do ProdutoMapper

---

## 1️⃣ `toEntity(ProdutoRequest dto)`

### ✔ O que faz

Cria uma nova instância de `Produto` a partir dos dados recebidos em `ProdutoRequest`.

### 🧠 Uso comum

Chamado pelo service durante a criação de um novo produto.

### 🔄 Atribuições realizadas

* Nome
* Descrição
* Preço
* Estoque
* ImagemUrl

### 📄 Código

```java
public Produto toEntity(ProdutoRequest dto) {
    Produto produto = new Produto();
    produto.setNome(dto.nome());
    produto.setDescricao(dto.descricao());
    produto.setPreco(dto.preco());
    produto.setEstoque(dto.estoque());
    produto.setImagemUrl(dto.imagemUrl());
    return produto;
}
```

---

## 2️⃣ `updateEntityFromRequest(Produto produto, ProdutoRequest dto)`

### ✔ O que faz

Atualiza os campos de uma entidade existente com os valores do `ProdutoRequest`.

### 🧠 Uso comum

Chamado no service durante operações de **atualização**.

### 🔄 Campos atualizados

* `nome`
* `descricao`
* `preco`
* `estoque`
* `imagemUrl`

### ⚠ Observação importante

Esse método **substitui todos os campos**, não faz merge parcial.
Ou seja, um `ProdutoRequest` sempre deve conter todos os campos obrigatórios.

### 📄 Código

```java
public void updateEntityFromRequest(Produto produto, ProdutoRequest dto) {
    produto.setNome(dto.nome());
    produto.setDescricao(dto.descricao());
    produto.setPreco(dto.preco());
    produto.setEstoque(dto.estoque());
    produto.setImagemUrl(dto.imagemUrl());
}
```

---

## 3️⃣ `toResponse(Produto entity)`

### ✔ O que faz

Converte uma entidade `Produto` em um DTO `ProdutoResponse`, usado para retornar dados ao cliente.

### 🧠 Uso comum

Chamado no service em:

* listagem
* busca por id
* criação
* atualização

### 📄 Código

```java
public ProdutoResponse toResponse(Produto entity) {
    return new ProdutoResponse(
            entity.getId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getPreco(),
            entity.getEstoque(),
            entity.getImagemUrl()
    );
}
```

---

# 🧠 Regras Técnicas Importantes

### ✔ O mapper não valida dados

Toda validação ocorre no **controller** via `@Valid` ou no banco via constraints.

### ✔ O mapper não aplica lógica de negócio

Ele apenas converte dados.

### ✔ Evita expor a entidade diretamente

O controller nunca retorna `Produto`, apenas `ProdutoResponse`.

### ✔ Mantém o service limpo

O service não precisa saber como montar um Produto ou ProdutoResponse.

---

# 📦 Resumo do ProdutoMapper

* Converte **ProdutoRequest → Produto**
* Converte **Produto → ProdutoResponse**
* Atualiza entidades existentes a partir de requests
* Não faz validações ou regras de negócio
* Facilita o desacoplamento entre camadas

---