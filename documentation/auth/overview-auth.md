# 🔐 **Módulo de Autenticação e Segurança — Overview**

O módulo de **Autenticação (Auth)** é o núcleo de segurança da aplicação DevFit. Ele é responsável por gerenciar o acesso dos usuários, garantir a proteção dos endpoints e gerenciar os níveis de permissão (Roles) dentro do sistema.

A arquitetura de segurança é baseada em **Spring Security 6** e utiliza **JWT (JSON Web Token)** para autenticação *stateless* (sem sessão no servidor).

---

# 🧩 **Arquitetura do Módulo**

O módulo integra componentes de configuração, controle, serviço e persistência para criar um fluxo seguro.

### ✔ **AuthController**
O ponto de entrada da API pública. Expõe os endpoints para:
* Login (`/auth/login`).
* Cadastro de Alunos (público).
* Cadastro de Instrutores (restrito a Gestores).
* Cadastro de Gestores (protegido por chave secreta).

### ✔ **AuthService**
Contém as regras de negócio para autenticação e registro.
* Gerencia o `AuthenticationManager` para validar credenciais.
* Verifica a existência de usuários duplicados (Email único).
* Valida a chave mestra (`ADMIN_SECRET`) para criação de novos administradores.
* Gera o token JWT após o login bem-sucedido.

### ✔ **Security Components (Pacote `security`)**
* **`SecurityConfig`**: Define a cadeia de filtros, desabilita CSRF (para API), configura CORS e define as permissões de URL (quem pode acessar o quê).
* **`JwtUtil`**: Responsável por gerar, assinar e validar tokens JWT, além de extrair *claims* (dados) do token.
* **`JwtAuthenticationFilter`**: Intercepta todas as requisições HTTP para verificar a presença e validade do token no cabeçalho `Authorization`.
* **`CustomUserDetailsService`**: Implementação da interface do Spring Security para carregar usuários do banco de dados.

### ✔ **Modelagem de Dados (Usuário e Roles)**
* **`Usuario`**: Entidade principal que implementa `UserDetails`. Possui relacionamento Many-to-Many com `Role`.
* **`Role` / `UsuarioRole`**: Define os níveis de acesso (`ALUNO`, `INSTRUTOR`, `GESTOR`).
* **`RoleInitializer`**: Garante que as Roles existam no banco de dados ao iniciar a aplicação.

---

# 🔄 **Fluxos Principais**

## 1. Login e Geração de Token
1. O cliente envia `LoginRequest` (email e senha).
2. `AuthController` repassa para `AuthService`.
3. `AuthenticationManager` valida as credenciais contra o banco (usando `CustomUserDetailsService` e `PasswordEncoder`).
4. Se válido, `JwtUtil` gera um token assinado contendo o ID, Email e Role do usuário.
5. O sistema retorna `AuthResponse` com o token.

## 2. Autenticação de Requisições (Filtro)
1. O cliente faz uma requisição (ex: `GET /treinos`) enviando o cabeçalho `Authorization: Bearer <token>`.
2. `JwtAuthenticationFilter` intercepta a chamada.
3. O filtro valida a assinatura e a expiração do token via `JwtUtil`.
4. Se válido, o contexto de segurança (`SecurityContextHolder`) é preenchido com o usuário autenticado.
5. A requisição prossegue para o Controller.

## 3. Cadastro de Usuários
* **Aluno**: Fluxo aberto. A senha é criptografada via `AuthMapper` antes de salvar.
* **Instrutor**: Requer que quem está fazendo a requisição já tenha a Role `GESTOR`.
* **Gestor**: Requer o envio de um código especial (`gestorCode`) que deve bater com a variável de ambiente `ADMIN_SECRET`.

---

# 🛡️ **Níveis de Acesso (Roles)**

O sistema utiliza controle de acesso baseado em papéis (RBAC - Role Based Access Control), configurado no `SecurityConfig` e via anotações `@PreAuthorize`.

| Role | Descrição | Permissões Exemplo |
| :--- | :--- | :--- |
| **ALUNO** | Usuário padrão do sistema. | Visualizar treinos, avaliações, fazer compras. |
| **INSTRUTOR** | Profissional de educação física. | Criar/Editar treinos e avaliações dos alunos. |
| **GESTOR** | Administrador do sistema. | Acesso total: Financeiro, Estoque, Cadastro de Instrutores, Métricas. |

---

# 📦 **Resumo Geral**

O módulo de Auth é a base de segurança do DevFit. Ele garante que:
* Senhas sejam sempre armazenadas criptografadas (`BCryptPasswordEncoder`).
* A API seja Stateless e escalável via JWT.
* O acesso aos recursos sensíveis (Financeiro, Gestão) seja restrito apenas a usuários autorizados.