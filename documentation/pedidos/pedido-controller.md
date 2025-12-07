# 🎮 PedidoController — Documentação Técnica

O `PedidoController` é responsável por expor o endpoint REST de criação de pedidos.
Ele recebe a requisição do cliente, valida os dados, delega ao `PedidoService` e retorna o `PedidoResponse`.

O controller segue uma arquitetura simples e direta, sem regras de negócio — toda a lógica permanece no service.

---

# 🧩 Visão Geral

Responsabilidades do controller:

* Receber pedidos via API
* Validar o corpo da requisição com `@Valid`
* Encaminhar o DTO para o `PedidoService`
* Retornar a resposta estruturada (`PedidoResponse`)
* Controlar o status HTTP adequado (201 Created)

---

# 📌 Endpoints Disponíveis

O módulo de pedidos, na sua versão atual, possui **apenas um endpoint**.

---

## 1️⃣ **POST `/pedidos` — Criar novo pedido**

### ✔ Descrição

Cria um pedido completo no sistema, contendo:

* usuário que realizou a compra
* lista de itens
* validações de estoque
* cálculo do valor total

A requisição deve conter um **PedidoRequest** válido.

---

### 📝 Corpo da Requisição

Exemplo de JSON baseado no `PedidoRequest`:

```json
{
  "usuarioId": 1,
  "itens": [
    { "produtoId": 10, "quantidade": 2 },
    { "produtoId": 5, "quantidade": 1 }
  ]
}
```

### 🔐 Validação automática

O controller utiliza:

```
@Valid
```

O que significa:

* campos obrigatórios são verificados
* erros geram `MethodArgumentNotValidException` (400)
* o service só recebe dados válidos

---

### 🔄 Fluxo do Endpoint

1. O cliente envia um JSON contendo os dados do pedido
2. O Spring valida o payload usando `@Valid`
3. O controller chama:

```
service.criarPedido(request);
```

4. O `PedidoService`:

    * valida usuário
    * valida produtos
    * verifica estoque
    * cria itens
    * calcula subtotal e total
    * persiste o pedido

5. O controller retorna:

* **Status: 201 Created**
* **Body: PedidoResponse**

---

### 📄 Código Real do Método

```
@PostMapping
public ResponseEntity<PedidoResponse> criar(@RequestBody @Valid PedidoRequest request) {
    return ResponseEntity.status(201).body(service.criarPedido(request));
}
```

---

### 🧨 Possíveis Erros

O método pode retornar erros vindos do service:

| Situação                | Exceção                         | Status                             |
| ----------------------- | ------------------------------- | ---------------------------------- |
| Usuário não existe      | UsuarioNaoEncontradoException   | 404                                |
| Produto não existe      | ProdutoNaoEncontradoException   | 404                                |
| Estoque insuficiente    | EstoqueInsuficienteException    | 400 ou 409 (dependendo do handler) |
| Dados inválidos no JSON | MethodArgumentNotValidException | 400                                |

Todos são tratados pelo `GlobalExceptionHandler`.

---

# 📦 Resumo Geral do PedidoController

* Expõe apenas um endpoint: **POST /pedidos**
* Valida o corpo da requisição automaticamente (`@Valid`)
* Delegação total ao `PedidoService`
* Retorna **PedidoResponse** com status **201 Created**
* Não contém regra de negócio
* Mantém a camada de apresentação simples e expressiva

