# 🎮 ProdutoController — Documentação Técnica

O `ProdutoController` expõe os endpoints REST utilizados para gerenciamento de produtos.  
O módulo utiliza rotas com ações explícitas (`adicionar`, `atualizar`, `excluir`) em vez do padrão REST tradicional.

As operações de escrita são protegidas por autorização, permitindo apenas usuários com o papel **GESTOR**.

---

# 🧩 Visão Geral

O controller é responsável por:

- Receber requisições da API
- Validar dados de entrada com `@Valid`
- Delegar regras de negócio ao `ProdutoService`
- Retornar DTOs para o cliente
- Integrar-se com o tratamento global de exceções

---

# 📌 Endpoints Disponíveis

---

## 1️⃣ GET `/produtos` — Listar todos os produtos

### ✔ Descrição
Retorna a lista completa de produtos cadastrados.

### 🔄 Fluxo
- O controller chama `produtoService.listar()`
- O service retorna uma lista de `ProdutoResponse`

### 🔓 Acesso
Público (não requer autenticação)

---

## 2️⃣ GET `/produtos/buscar/{id}` — Buscar produto por ID

### ✔ Descrição
Retorna um produto específico pelo ID informado.

### 🔄 Fluxo
- O controller chama `produtoService.buscarPorId(id)`
- Caso o produto não exista → `ProdutoNaoEncontradoException` (404)

### 🔓 Acesso
Público

---

# 🔐 Endpoints restritos ao ROLE_GESTOR

Todos os endpoints abaixo usam:


@PreAuthorize("hasAuthority('ROLE_GESTOR')")


Isso significa que **apenas usuários com papel GESTOR podem criar, atualizar e remover produtos**.

---

## 3️⃣ POST `/produtos/adicionar` — Criar novo produto

### ✔ Descrição

Cria um novo produto no sistema com base nos dados enviados no corpo da requisição.

### 📝 Corpo da Requisição

`ProdutoRequest` contendo:

* nome
* descricao
* preco
* estoque
* imagemUrl

Validação ocorre automaticamente via `@Valid`.

### 🔄 Fluxo

1. O controller recebe o `ProdutoRequest`
2. A validação é aplicada antes da execução do método
3. O controller chama `produtoService.salvar(request)`
4. O service salva o produto e retorna `ProdutoResponse`
5. O controller devolve **HTTP 201 Created**

### 🧨 Possíveis Erros

| Situação        | Exceção                         | Status |
| --------------- | ------------------------------- | ------ |
| Nome duplicado  | DataIntegrityViolationException | 409    |
| Dados inválidos | MethodArgumentNotValidException | 400    |

### 🔐 Acesso

`GESTOR` somente.

---

## 4️⃣ PUT `/produtos/atualizar/{id}` — Atualizar produto existente

### ✔ Descrição

Atualiza as informações de um produto já cadastrado.

### 📝 Corpo da Requisição

`ProdutoRequest` com os campos atualizados.

### 🔄 Fluxo

1. O controller recebe o `id` e o `ProdutoRequest`
2. Validação ocorre automaticamente pelo `@Valid`
3. O controller chama `produtoService.atualizar(id, request)`
4. O service:

    * Localiza o produto
    * Atualiza os campos via mapper
    * Salva e retorna `ProdutoResponse`
5. O controller devolve **HTTP 200 OK**

### 🧨 Possíveis Erros

| Situação           | Exceção                         | Status |
| ------------------ | ------------------------------- | ------ |
| Produto não existe | ProdutoNaoEncontradoException   | 404    |
| Nome duplicado     | DataIntegrityViolationException | 409    |
| Dados inválidos    | MethodArgumentNotValidException | 400    |

### 🔐 Acesso

`GESTOR` somente.

---

## 5️⃣ DELETE `/produtos/excluir/{id}` — Remover produto

### ✔ Descrição

Remove um produto do banco de dados.

### 🔄 Fluxo

1. O controller recebe o `id`
2. Chama `produtoService.deletar(id)`
3. O service verifica se existe
4. Se houver vínculos com pedidos, o banco impede a remoção
5. O controller retorna **HTTP 204 No Content**

### 🧨 Possíveis Erros

| Situação                    | Motivo           | Exceção                         | Status |
| --------------------------- | ---------------- | ------------------------------- | ------ |
| Produto não encontrado      | ID inexistente   | ProdutoNaoEncontradoException   | 404    |
| Produto vinculado a pedidos | Restrições de FK | DataIntegrityViolationException | 409    |

### 🔐 Acesso

`GESTOR` somente.

---

# 📦 Resumo Geral do ProdutoController

* Todos os endpoints **GET** são públicos e retornam dados de produtos.
* Endpoints de escrita (**POST, PUT, DELETE**) são protegidos por autorização via `ROLE_GESTOR`.
* O controller:

    * Recebe e valida dados (`@Valid`)
    * Converte requisições em chamadas ao `ProdutoService`
    * Retorna DTOs (`ProdutoResponse`)
    * Delega exceções ao tratamento global (`GlobalExceptionHandler`)

