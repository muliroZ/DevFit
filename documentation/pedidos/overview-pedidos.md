# 📦 **Módulo de Pedidos — Documentação Técnica (Overview Geral)**

O módulo de **Pedidos** é responsável por processar compras realizadas pelos usuários, garantindo que os produtos tenham estoque suficiente, que o valor total seja calculado corretamente e que todos os itens sejam registrados de forma consistente.

Este módulo integra-se diretamente com:

* **Usuários** (quem realiza o pedido)
* **Produtos** (consumo e atualização de estoque)
* **Itens de Pedido** (itens individuais pertencentes ao pedido)

É um dos módulos centrais da aplicação, responsável pela movimentação de estoque e pelo registro de transações.

---

# 🧩 **Arquitetura do Módulo**

O módulo é composto pelos seguintes componentes:

### ✔ **PedidoController**

Expõe o endpoint público para criação de pedidos (`POST /pedidos`).
Recebe e valida o `PedidoRequest`, delegando o processamento para o serviço.

---

### ✔ **PedidoService**

Camada que concentra as regras de negócio.
Responsável por:

* validar usuário
* validar produtos
* verificar estoque
* criar `ItemPedido`
* calcular subtotal e valor total
* atualizar o estoque
* persistir o pedido e seus itens

Toda a lógica crítica está contida aqui.

---

### ✔ **PedidoRepository**

Gerencia persistência da entidade `Pedido` e oferece:

* CRUD completo
* método customizado `sumValorTotalByPeriodo` para relatórios financeiros

---

### ✔ **ItemPedidoRepository**

CRUD básico para itens de pedido.
Normalmente não é usado diretamente, pois itens são salvos em cascata pelo `Pedido`.

---

### ✔ **PedidoMapper**

Responsável pela conversão entre:

* `ItemPedidoRequest → ItemPedido`
* `ItemPedido → ItemPedidoResponse`
* `Pedido → PedidoResponse`

Centraliza toda a transformação de dados, mantendo o service e o controller limpos.

---

### ✔ **Entidades (`Pedido` e `ItemPedido`)**

Representam as tabelas no banco de dados.

* `Pedido` contém informações gerais da compra: usuário, data, itens, total
* `ItemPedido` representa cada produto comprado, com quantidade, preço unitário e subtotal

`Pedido` possui relacionamento `@OneToMany` com `ItemPedido` com **cascade** e **orphanRemoval**, garantindo persistência automática dos itens.

---

### ✔ **DTOs (Data Transfer Objects)**

Utilizados para entrada e saída nos endpoints.

* `PedidoRequest`
* `ItemPedidoRequest`
* `PedidoResponse`
* `ItemPedidoResponse`

Eles permitem isolar a entidade de domínio da API, mantendo segurança e consistência.

---

### ✔ **Exceções específicas do módulo**

Utilizadas para validar consistência durante a criação do pedido:

* `UsuarioNaoEncontradoException`
* `ProdutoNaoEncontradoException`
* `EstoqueInsuficienteException`

Todas são tratadas pelo `GlobalExceptionHandler`, garantindo retornos amigáveis ao cliente.

---

# 🔄 **Fluxo Completo da Criação de Pedido**

1. **Usuário envia JSON** contendo:

    * ID do usuário
    * lista de itens

2. **Controller valida** (`@Valid`) e chama o serviço.

3. **PedidoService carrega o usuário**.
   Se não existir → lança `UsuarioNaoEncontradoException`.

4. **Para cada item do pedido**:

    * carrega o produto
    * valida estoque
    * atualiza estoque
    * cria um `ItemPedido` com preço e subtotal
    * associa o item ao pedido

5. **Calcula o valor total** somando todos os subtotais.

6. **Salva o pedido** (itens incluídos automaticamente via cascade).

7. **Mapper monta o PedidoResponse** com:

    * dados do pedido
    * itens formatados
    * valor total

8. **Controller retorna 201 Created** com o DTO completo.

---

# 🧠 **Regras de Negócio Importantes**

* Estoque deve ser sempre suficiente para cada item.
* Subtotal = preçoUnitário * quantidade.
* Valor total = soma de todos os subtotais.
* O estoque é atualizado **antes** da persistência do pedido.
* Itens sempre pertencem a um pedido.
* O pedido não pode existir sem usuário.

---

# 📦 **Resumo Geral**

O módulo de pedidos fornece uma implementação sólida, com:

* arquitetura limpa
* separação clara de responsabilidades
* operações transacionais seguras
* DTOs bem definidos
* mapeamento consistente
* cálculos garantidos pelo service e mapper
* retorno estruturado e amigável

Ele é totalmente integrado com os módulos de usuários e produtos, formando a base do fluxo de vendas.

