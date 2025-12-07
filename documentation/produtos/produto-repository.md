# 📚 ProdutoRepository — Documentação Técnica

O `ProdutoRepository` é responsável por realizar operações de acesso ao banco de dados relacionadas à entidade `Produto`.  
Ele utiliza o Spring Data JPA para fornecer métodos prontos de consulta, criação, atualização e exclusão.

---

# 🧩 Função Principal

O repositório abstrai a camada de persistência, permitindo que o `ProdutoService` trabalhe com operações de banco sem precisar escrever SQL manualmente.

Ele estende a interface `JpaRepository`, herdando métodos como:

- `save()`
- `findById()`
- `findAll()`
- `delete()`
- `deleteById()`

---

# 🔧 Estrutura da Interface

```
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
```

---

# 📌 Detalhes Importantes

* A anotação `@Repository` permite que o Spring trate exceções de banco de forma adequada.
* Como estende `JpaRepository`, já possui todos os métodos essenciais de CRUD.
* No momento, não há métodos customizados no repositório.
* Restrições como nome único são definidas na entidade (`@Column(unique = true)`).

---

# 📦 Resumo

O `ProdutoRepository` fornece:

* Operações CRUD prontas via Spring Data JPA
* Integração direta com o `ProdutoService`
* Nenhuma regra de negócio — apenas persistência
* Simplicidade e baixo acoplamento com o restante do sistema

---

