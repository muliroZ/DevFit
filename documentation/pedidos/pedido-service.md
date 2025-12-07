
# 🧠 PedidoService — Documentação Técnica

O `PedidoService` é responsável pela lógica central do módulo de pedidos.
Ele gerencia:

* criação de pedidos
* validação de usuário e produtos
* controle de estoque
* cálculo de subtotal e valor total
* persistência do pedido e seus itens

Este serviço integra múltiplos componentes, garantindo integridade dos dados durante a criação de um pedido.

---

# 🧩 Dependências

O `PedidoService` utiliza:

* **PedidoRepository** → persistência do pedido
* **ProdutoRepository** → consulta e atualização de estoque
* **UsuarioRepository** → validação de usuário
* **PedidoMapper** → conversão DTO ↔ Entidade

Todas são injetadas via construtor.

---

# 🚀 Método Principal

## 1️⃣ **criarPedido(PedidoRequest request)**

Este é o único método público do service e contém todo o fluxo de criação de pedido.

---

# 🔄 Fluxo Completo da Operação

### **1. Criar instância vazia de Pedido**

```java
Pedido pedido = new Pedido();
```

O pedido é criado inicialmente sem itens e sem valor total.

---

### **2. Validar e carregar o Usuário**

```
Usuario usuario = usuarioRepository.findById(request.usuarioId())
        .orElseThrow(() -> new UsuarioNaoEncontradoException(request.usuarioId()));
```

* Busca o usuário pelo ID fornecido
* Se não encontrar → lança **UsuarioNaoEncontradoException**
* O pedido recebe o usuário carregado

```
pedido.setUsuario(usuario);
```

---

### **3. Processar e validar cada ItemPedidoRequest**

Para cada item enviado no DTO:

1. Buscar o produto
2. Validar estoque
3. Atualizar estoque
4. Criar o ItemPedido através do mapper

Trecho real:

```java
List<ItemPedido> itens = request.itens().stream().map(i -> {

    Produto produto = produtoRepository.findById(i.produtoId())
            .orElseThrow(() -> new ProdutoNaoEncontradoException(i.produtoId()));

    if (produto.getEstoque() < i.quantidade()) {
        throw new EstoqueInsuficienteException(i.produtoId(), produto.getNome());
    }

    produto.setEstoque(produto.getEstoque() - i.quantidade());
    produtoRepository.save(produto);

    return mapper.toEntity(i, produto, pedido);

}).toList();
```

### ✔ Validando produto

Se não existir: **ProdutoNaoEncontradoException**

### ✔ Validando estoque

Se a quantidade solicitada for maior que o estoque atual:
**EstoqueInsuficienteException**

### ✔ Atualizando estoque

O estoque é decrementado imediatamente, antes mesmo de persistir o pedido.

### ✔ Criando item

`mapper.toEntity(i, produto, pedido)` monta:

* quantidade
* preço unitário
* subtotal
* referência ao pedido
* referência ao produto

---

### **4. Associar os itens ao pedido**

```
pedido.setItens(itens);
```

Os itens agora pertencem ao pedido recém-criado.

---

### **5. Calcular o valor total**

```
BigDecimal valorTotal = itens.stream()
        .map(ItemPedido::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
```

O total é a soma dos subtotais dos itens.

```
pedido.setValorTotal(valorTotal);
```

---

### **6. Persistir o pedido no banco**

```
Pedido salvo = pedidoRepository.save(pedido);
```

O `cascade = CascadeType.ALL` salva automaticamente todos os itens.

---

### **7. Retornar DTO de resposta**

```
return mapper.toResponse(salvo);
```

Nenhuma entidade é exposta diretamente — apenas DTOs.

---

# 🧨 Exceções que o método pode lançar

| Situação               | Exceção                       | Motivo                                       |
| ---------------------- | ----------------------------- | -------------------------------------------- |
| Usuário não encontrado | UsuarioNaoEncontradoException | ID inválido                                  |
| Produto não encontrado | ProdutoNaoEncontradoException | ID do produto inexistente                    |
| Estoque insuficiente   | EstoqueInsuficienteException  | Quantidade solicitada maior que a disponível |

Todas são tratadas pelo `GlobalExceptionHandler`.

---

# 📦 Resumo Técnico do PedidoService

* Cria pedidos completos com itens associados
* Valida usuário, produtos e estoque
* Atualiza estoque imediatamente
* Calcula subtotal e total
* Salva pedido e itens em uma única transação (`@Transactional`)
* Utiliza mapper para conversão entre DTO e entidades
* Retorna um `PedidoResponse` final estruturado

