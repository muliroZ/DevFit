# 🧭 PedidoMapper — Documentação Técnica

O `PedidoMapper` é responsável por converter dados entre:

* **ItemPedidoRequest → ItemPedido (entidade)**
* **ItemPedido → ItemPedidoResponse**
* **Pedido → PedidoResponse**

Ele garante que o `PedidoService` trabalhe apenas com entidades e que o controller retorne apenas DTOs apropriados ao cliente.

Essa conversão isolada mantém o service mais simples e evita que detalhes da modelagem interna vazem para a API.

---

# 🧩 Visão Geral

O mapper possui três responsabilidades essenciais:

1. **Montar um ItemPedido completo a partir do DTO e das entidades Produto e Pedido**
2. **Converter ItemPedido em ItemPedidoResponse**
3. **Converter Pedido em PedidoResponse**

Nenhuma lógica de negócio é aplicada aqui — apenas transformação de dados.

---

# 📌 Estrutura da Classe

```
@Component
public class PedidoMapper {

    public ItemPedido toEntity(ItemPedidoRequest dto, Produto produto, Pedido pedido) { ... }

    public ItemPedidoResponse toItemResponse(ItemPedido item) { ... }

    public PedidoResponse toResponse(Pedido pedido) { ... }
}
```

---

# 🔧 Métodos do PedidoMapper

---

## 1️⃣ `toEntity(ItemPedidoRequest dto, Produto produto, Pedido pedido)`

### ✔ O que faz

Converte um `ItemPedidoRequest` em uma entidade `ItemPedido`.
Esse método é utilizado durante a criação de pedidos no `PedidoService`.

### 🔄 Atribuições realizadas

O mapper:

* associa o item ao pedido
* associa o item ao produto
* define a quantidade solicitada
* define o preço unitário baseado no produto atual
* calcula automaticamente o subtotal (`precoUnitario * quantidade`)

### 📄 Código real

```java
public ItemPedido toEntity(ItemPedidoRequest dto, Produto produto, Pedido pedido) {
    ItemPedido item = new ItemPedido();

    item.setPedido(pedido);
    item.setProduto(produto);
    item.setQuantidade(dto.quantidade());
    item.setPrecoUnitario(produto.getPreco());
    item.setSubtotal(produto.getPreco()
            .multiply(BigDecimal.valueOf(dto.quantidade())));

    return item;
}
```

### 🧠 Observações importantes

* O subtotal é calculado no mapper, garantindo consistência.
* O preço unitário sempre reflete o **preço atual do produto no momento do pedido**, não o preço original no banco.
* Não há regras de negócio no método — apenas conversão e cálculo matemático simples.

---

## 2️⃣ `toItemResponse(ItemPedido item)`

### ✔ O que faz

Converte a entidade `ItemPedido` em um DTO de saída `ItemPedidoResponse`, usado dentro de `PedidoResponse`.

### 🧠 Quando é utilizado?

* Na listagem dos itens de um pedido
* Na resposta da criação de pedido

### 📄 Código real

```java
public ItemPedidoResponse toItemResponse(ItemPedido item) {
    return new ItemPedidoResponse(
            item.getProduto().getId(),
            item.getProduto().getNome(),
            item.getQuantidade(),
            item.getPrecoUnitario(),
            item.getSubtotal()
    );
}
```

### 📌 Campos retornados

| Campo         | Fonte                       | Descrição                  |
| ------------- | --------------------------- | -------------------------- |
| produtoId     | item.getProduto().getId()   | ID do produto comprado     |
| nomeProduto   | item.getProduto().getNome() | Nome exibido ao usuário    |
| quantidade    | item.getQuantidade()        | Quantidade no pedido       |
| precoUnitario | item.getPrecoUnitario()     | Preço no momento da compra |
| subtotal      | item.getSubtotal()          | Preço × quantidade         |

---

## 3️⃣ `toResponse(Pedido pedido)`

### ✔ O que faz

Converte um `Pedido` completo em `PedidoResponse`, incluindo:

* dados gerais do pedido
* lista transformada de itens
* valor total

### 📄 Código real

```java
public PedidoResponse toResponse(Pedido pedido) {
    return new PedidoResponse(
            pedido.getId(),
            pedido.getUsuario().getId(),
            pedido.getDataCriacao(),
            pedido.getItens().stream()
                    .map(this::toItemResponse)
                    .toList(),
            pedido.getValorTotal()
    );
}
```

### 📌 O que o mapper retorna ao cliente?

| Campo       | Fonte                       | Descrição                       |
| ----------- | --------------------------- | ------------------------------- |
| id          | pedido.getId()              | Identificador do pedido         |
| usuarioId   | pedido.getUsuario().getId() | Quem fez o pedido               |
| dataCriacao | pedido.getDataCriacao()     | Data registrada automaticamente |
| itens       | mapped list                 | Lista de itens convertidos      |
| valorTotal  | pedido.getValorTotal()      | Soma de todos os subtotais      |

---

# 🧠 Regras Técnicas Importantes

### ✔ O mapper não valida dados

Toda validação acontece:

* no controller (`@Valid` em PedidoRequest)
* no service (estoque, existência de usuário/produto)

### ✔ O mapper não executa lógica de negócio

Ele apenas converte DTOs em entidades e vice-versa.

### ✔ Evita expor entidades

Todo retorno da API passa por `PedidoResponse`, nunca pela entidade `Pedido`.

### ✔ Mantém o service limpo

O `PedidoService` delega ao mapper a criação e montagem das entidades.

---

# 📦 Resumo do PedidoMapper

O mapper:

* cria itens de pedido corretamente a partir do request
* calcula subtotal por item
* monta respostas amigáveis ao cliente
* monta o DTO completo do pedido
* ajuda a separar responsabilidades entre service e controller

É uma peça fundamental para manter o módulo bem estruturado, limpo e organizado.

