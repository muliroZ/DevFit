# 📚 Repositórios de Autenticação

Interfaces que estendem `JpaRepository` para acesso a dados.

## `UsuarioRepository`
Responsável por buscar usuários para autenticação e validação.

* **Métodos Customizados:**
    * `findByEmail(String email)`: Essencial para o login (`UserDetailsService`).
    * `findByIdAndRoles_Nome(...)`: Usado para buscar usuários específicos (ex: buscar um Instrutor pelo ID).
    * `findByRoles_Nome(...)`: Usado para listar usuários por perfil (ex: listar todos os Alunos).

## `RoleRepository`
Responsável por gerenciar os papéis de acesso.

* **Métodos Customizados:**
    * `findByNome(UsuarioRole nome)`: Busca uma *role* pelo seu Enum correspondente. Usado durante o cadastro para associar permissões ao novo usuário.