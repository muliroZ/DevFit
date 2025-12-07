# 🗃️ Modelos de Domínio (Usuário e Role)

As entidades mapeiam a estrutura de segurança no banco de dados relacional.

## 👤 Entidade `Usuario`

Representa o usuário do sistema. Implementa `UserDetails` para integração direta com o Spring Security.

* **Tabela:** `usuario`
* **Relacionamentos:**
    * **`roles`**: `@ManyToMany` com a tabela `Role` (tabela de junção `usuario_roles`). Carregamento `EAGER` para garantir que as permissões estejam disponíveis no login.
    * **`matricula`**: `@OneToOne`.
    * **`mensalidades`**: `@OneToMany`.
* **Métodos de Segurança (`UserDetails`):**
    * `getAuthorities()`: Converte as *roles* do banco em `SimpleGrantedAuthority` (ex: `ROLE_ALUNO`).
    * `getPassword()` / `getUsername()`: Mapeados para `senha` e `email`.

## 🛡️ Entidade `Role` e Enum `UsuarioRole`

Gerenciam os níveis de acesso.

* **Enum `UsuarioRole`**: Define os valores fixos: `ALUNO`, `INSTRUTOR`, `GESTOR`.
* **Entidade `Role`**: Persiste esses valores no banco.
    * **Campo principal:** `nome` (do tipo Enum `UsuarioRole`, armazenado como STRING).