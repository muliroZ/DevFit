# 🛠 ProdutoService — Documentação Técnica

O `ProdutoService` é a camada de regras de negócio do módulo de produtos.  
Ele coordena o fluxo entre controller → validações → persistência no banco via repository.

O objetivo principal dessa classe é garantir que:

- Produtos sejam criados com dados válidos
- Atualizações respeitem regras de integridade
- Estoque nunca assuma valores inválidos
- Exceções adequadas sejam lançadas quando necessário

---

# 📌 Responsabilidades Principais

O `ProdutoService` centraliza as operações:

- Criar novo produto
- Listar produtos
- Buscar produto por ID
- Atualizar produto existente
- Remover produto
- Garantir integridade das informações (nome único, estoque válido, etc.)

---

# 🔎 Métodos do ProdutoService

Abaixo está o funcionamento detalhado de cada método.

---

## 1️⃣ **criarProduto(ProdutoRequest request)**

### ✔ O que faz
- Converte o DTO recebido em entidade (`ProdutoMapper`)
- Aplica validações do Bean Validation automaticamente
- Salva o produto no banco
- Retorna um `ProdutoResponse`

### 🔄 Fluxo Interno

1. Controller envia `ProdutoRequest`
2. Service chama o `mapper.toEntity(request)`
3. Service salva usando `produtoRepository.save(produto)`
4. Service retorna `mapper.toResponse(produtoSalvo)`

### 🧨 Exceções Possíveis
| Situação | Exceção | Status |
|----------|----------|--------|
| Nome duplicado | `DataIntegrityViolationException` | 409 |
| Campos inválidos | `MethodArgumentNotValidException` | 400 |

---

## 2️⃣ **listarProdutos()**

### ✔ O que faz
Retorna a lista completa de produtos cadastrados.

### 🔄 Fluxo Interno
- Consulta o banco com `produtoRepository.findAll()`
- Converte todos para `ProdutoResponse`
- Retorna a lista

---

## 3️⃣ **buscarPorId(Long id)**

### ✔ O que faz
Busca um produto específico pelo ID.

### 🔄 Fluxo Interno

1. Service tenta localizar o produto
2. Se não encontrar → lança `ProdutoNaoEncontradoException`
3. ControllerAdvice transforma em HTTP 404

### 🧨 Exceção
- `ProdutoNaoEncontradoException` → **404 NOT FOUND**

---

## 4️⃣ **atualizarProduto(Long id, ProdutoRequest request)**

### ✔ O que faz
Atualiza os dados do produto existente.

### 🔄 Fluxo Interno

1. Busca o produto existente
2. Se não existir → lança `ProdutoNaoEncontradoException`
3. Aplica mudanças aos campos:
    - nome
    - descricao
    - preco
    - estoque
    - imagemUrl
4. Salva novamente
5. Retorna `ProdutoResponse`

### 🧨 Exceções Possíveis
| Situação | Exceção | Status |
|----------|----------|--------|
| Produto inexistente | ProdutoNaoEncontradoException | 404 |
| Nome duplicado | DataIntegrityViolationException | 409 |
| Campos inválidos | MethodArgumentNotValidException | 400 |

---

## 5️⃣ **removerProduto(Long id)**

### ✔ O que faz
Remove um produto do banco.

### 🔄 Fluxo Interno

1. Verifica se existe
2. Se não existir → not found
3. Tenta remover pelo repositório
4. Se houver pedidos vinculados → o banco bloqueia (FK constraint)

### 🧨 Possíveis erros
| Caso | Motivo | Tratamento |
|------|---------|------------|
| Produto não existe | ID inválido | 404 |
| Produto vinculado a pedidos | FK constraint | 409 |

O tratamento é feito pelo `GlobalExceptionHandler`.

---

# ⚠️ Regras de Negócio Importantes

## ✔ Validação de Nome Único
O banco impede nomes duplicados com `unique = true`.

## ✔ Preço e estoque nunca negativos
Validados por:
- `@PositiveOrZero`
- e pelos DTOs (opcional)

## ✔ Produto só pode ser deletado se não houver pedidos vinculados
Se houver → disparará `DataIntegrityViolationException`.

---

# 🧠 Integração com PedidoService

O `ProdutoService` não gerencia diretamente o estoque durante pedidos.  
Quem faz isso é o `PedidoService`, que:

- valida estoque
- decrementa quantidade
- salva produto após alteração

O `ProdutoService` apenas gerencia **operações diretas** sobre produtos.

---

# 📌 Resumo Geral do ProdutoService

O serviço implementa:

- CRUD completo
- Validações automáticas via Bean Validation
- Tratamento de exceções via ControllerAdvice
- Conversão limpa com ProdutoMapper
- Regras simples de integridade (nome único, estoque ≥ 0)

Ele é um serviço simples, estável e reutilizável em qualquer parte da aplicação.

---

# 📄 Fim da Documentação do ProdutoService
