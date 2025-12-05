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

```java
@PreAuthorize("hasAuthority('ROLE_GESTOR')")
