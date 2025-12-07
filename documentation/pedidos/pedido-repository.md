# 📚 **PedidoRepository — Documentação Técnica**

O `PedidoRepository` é responsável pelo acesso aos dados da entidade `Pedido`.
Ele estende `JpaRepository`, herdando automaticamente operações CRUD, e também define uma consulta customizada para sumarizar o valor total de pedidos em um período específico.

---

# 🧩 Visão Geral

O repositório fornece:

* **CRUD completo** para pedidos
* **Consulta customizada** para cálculo de faturamento por período
* Integração automática com Spring Data JPA
* Mapeamento da entidade `Pedido` com `Long` como chave primária

O repositório utiliza `@Repository` para:

* permitir detecção automática pelo Spring
* converter exceções do JPA para exceções Spring

---

# 🔧 Estrutura da Interface

```java
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query(
            "SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p " +
            "WHERE p.dataCriacao BETWEEN :dataInicio AND :dataFinal"
    )
    BigDecimal sumValorTotalByPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                      @Param("dataFinal") LocalDate dataFinal);
}
```

---

# 📌 Métodos disponíveis

### ✔ Métodos herdados do `JpaRepository`

* `findById(Long id)`
* `findAll()`
* `save(Pedido pedido)`
* `delete(Pedido pedido)`
* `deleteById(Long id)`
* `count()`
* outros métodos utilitários do Spring Data

Esses métodos já atendem todas as operações básicas do módulo.

---

# 📊 Método Customizado: `sumValorTotalByPeriodo`

### ✔ Descrição

Calcula o **valor total faturado** pelos pedidos dentro de um intervalo de datas.

### 🔎 Assinatura

```java
BigDecimal sumValorTotalByPeriodo(LocalDate dataInicio, LocalDate dataFinal);
```

### 🧠 Funcionamento

* Converte o intervalo informado em uma consulta JPQL
* Soma **apenas o valorTotal** dos pedidos
* **COALESCE** garante que, se não houver resultados, o retorno é **0**, não `null`
* Útil para relatórios administrativos e dashboards financeiros

### 📄 Consulta JPQL usada

```
SELECT COALESCE(SUM(p.valorTotal), 0)
FROM Pedido p
WHERE p.dataCriacao BETWEEN :dataInicio AND :dataFinal
```

### ✔ Observações importantes

* `dataCriacao` no `Pedido` é um **LocalDateTime**, mas o filtro usa **LocalDate**.
  O Spring Data fará a conversão automática para a faixa de 00:00 até 23:59:59 do dia final.
* Este método **não é obrigatório para o módulo de pedidos**, mas é extremamente útil em módulos de relatórios e estatísticas.

---

# 📦 Resumo do PedidoRepository

* Gerencia operações de persistência da entidade `Pedido`
* Herda todo o CRUD do Spring Data JPA
* Oferece consulta customizada para cálculo de faturamento por período
* Integra-se ao `PedidoService` e módulos administrativos

---

---

# 📚 **ItemPedidoRepository — Documentação Técnica**

O `ItemPedidoRepository` gerencia a persistência da entidade `ItemPedido`.

---

# 🧩 Visão Geral

A entidade `ItemPedido` normalmente é salva via cascade pelo `Pedido`, mas o repositório existe para casos em que se deseja:

* Listar itens diretamente
* Consultas personalizadas
* Depurações ou operações administrativas

Nos fluxos atuais, o mapper e o service realizam todas as operações via `PedidoRepository`, mas este repositório está pronto para expansões futuras.

---

# 🔧 Estrutura da Interface

```java
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {}
```

---

# 📌 Métodos herdados

Como ele estende `JpaRepository`, possui:

* `findById(Long id)`
* `findAll()`
* `save(ItemPedido item)`
* `deleteById(Long id)`
* `count()`
* etc.

Nenhum método customizado foi definido.

---

# 📦 Resumo do ItemPedidoRepository

* Repositório simples, sem customizações
* Permite CRUD completo se necessário
* Útil para evolução futura do módulo
* Integrado automaticamente ao Spring

