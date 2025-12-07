# 🧾 **PedidoDTOs — Documentação Técnica**

Os DTOs do módulo de pedidos definem os formatos de **entrada** e **saída** utilizados pelo `PedidoController` e pelo `PedidoService`.

Eles garantem:

* isolamento entre entidade e camada externa
* segurança de dados
* padronização da API
* clareza na comunicação entre front e back-end

O módulo possui **quatro DTOs**:

* **PedidoRequest** → entrada principal
* **ItemPedidoRequest** → entrada de itens
* **PedidoResponse** → saída principal
* **ItemPedidoResponse** → saída de itens

---

# 📥 **ItemPedidoRequest — Dados de Entrada**

Representa um item enviado na criação de um pedido.

---

## 🔧 Estrutura (Código real)

```java
public record ItemPedidoRequest(
        Long produtoId,
        Integer quantidade
) {}
```

---

## 📌 Campos

| Campo      | Tipo    | Obrigatório | Descrição                            |
| ---------- | ------- | ----------- | ------------------------------------ |
| produtoId  | Long    | Sim         | ID do produto desejado.              |
| quantidade | Integer | Sim         | Quantidade solicitada para o pedido. |

### ✔ Observações

* A existência do produto é validada no **PedidoService**.
* A verificação de estoque também ocorre no service.
* A quantidade deve ser maior que zero — o service garante isso.

---

# 📤 **ItemPedidoResponse — Dados de Saída**

Usado para mostrar cada item dentro do `PedidoResponse`.

---

## 🔧 Estrutura (Código real)

```java
public record ItemPedidoResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {}
```

---

## 📌 Campos

| Campo         | Tipo       | Descrição                              |
| ------------- | ---------- | -------------------------------------- |
| produtoId     | Long       | ID do produto comprado.                |
| nomeProduto   | String     | Nome do produto exibido ao usuário.    |
| quantidade    | Integer    | Quantidade adquirida.                  |
| precoUnitario | BigDecimal | Preço do produto no momento do pedido. |
| subtotal      | BigDecimal | preçoUnitário × quantidade.            |

---

# 📥 **PedidoRequest — Dados de Entrada**

DTO usado no endpoint **POST /pedidos**.

---

## 🔧 Estrutura (Código real)

```java
public record PedidoRequest(
        Long usuarioId,
        List<ItemPedidoRequest> itens
) {}
```

---

## 📌 Campos

| Campo     | Tipo                    | Obrigatório | Descrição                          |
| --------- | ----------------------- | ----------- | ---------------------------------- |
| usuarioId | Long                    | Sim         | Identificador do usuário comprador |
| itens     | List<ItemPedidoRequest> | Sim         | Lista de itens do pedido           |

### ✔ Observações

* O pedido não é criado sem usuário válido → validado no service.
* O pedido não pode ter lista vazia de itens → também validado no service.

---

# 📤 **PedidoResponse — Dados de Saída**

Retornado após a criação de um pedido.

---

## 🔧 Estrutura (Código real)

```java
public record PedidoResponse(
        Long id,
        Long usuarioId,
        LocalDateTime dataCriacao,
        List<ItemPedidoResponse> itens,
        BigDecimal valorTotal
) {}
```

---

## 📌 Campos

| Campo       | Tipo                     | Descrição                                          |
| ----------- | ------------------------ | -------------------------------------------------- |
| id          | Long                     | Identificador do pedido.                           |
| usuarioId   | Long                     | ID do usuário que realizou a compra.               |
| dataCriacao | LocalDateTime            | Data e hora gravada automaticamente pela entidade. |
| itens       | List<ItemPedidoResponse> | Lista detalhada dos itens comprados.               |
| valorTotal  | BigDecimal               | Soma dos subtotais dos itens.                      |

### ✔ Observações

* O valor total é calculado no `PedidoService`.
* `dataCriacao` é definida no método `@PrePersist` da entidade `Pedido`.

---

# 🔁 **Relação entre os DTOs**

| Direção         | Conversão                       | Responsável                  |
| --------------- | ------------------------------- | ---------------------------- |
| Entrada         | PedidoRequest → Pedido          | PedidoService + PedidoMapper |
| Entrada (itens) | ItemPedidoRequest → ItemPedido  | PedidoMapper                 |
| Saída (itens)   | ItemPedido → ItemPedidoResponse | PedidoMapper                 |
| Saída geral     | Pedido → PedidoResponse         | PedidoMapper                 |

---

# 🧠 Regras Gerais

* Os DTOs **não expõem entidades** diretamente.
* Toda validação crítica ocorre no service (usuário, produto, estoque).
* Os DTOs são **records**, garantindo:

    * imutabilidade
    * segurança
    * clareza

---

# 📦 **Resumo do Módulo DTO**

Os DTOs de pedidos:

* estruturam a comunicação entre cliente e servidor
* garantem entrada consistente
* padronizam a saída da API
* funcionam perfeitamente com o `PedidoMapper`
* tornam o controller limpo e expressivo
* isolam a camada de domínio
