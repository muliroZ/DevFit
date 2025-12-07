# 🧠 AuthService — Documentação Técnica

O `AuthService` encapsula a lógica de negócio relacionada à autenticação e registro de usuários. Ele orquestra o `AuthenticationManager`, repositórios e geradores de token.

## ⚙️ Dependências

* **`AuthenticationManager`**: Para validar credenciais de login.
* **`JwtUtil`**: Para geração do token JWT.
* **`UsuarioRepository` & `RoleRepository`**: Para persistência.
* **`PasswordEncoder`**: Para criptografar senhas antes de salvar.
* **`ADMIN_SECRET`**: Valor injetado via `@Value` para validar o cadastro de gestores.

## 🚀 Principais Funcionalidades

### 🔐 Login (`login`)
Realiza a autenticação completa:
1. Cria um `UsernamePasswordAuthenticationToken`.
2. Delega a validação ao `AuthenticationManager` do Spring Security.
3. Se bem-sucedido, recupera o usuário autenticado.
4. Determina a *role* principal do usuário.
5. Gera e retorna o token JWT.

### 📝 Registro de Usuários
O serviço possui métodos específicos para cada perfil (`registrarAluno`, `registrarInstrutor`, `registrarGestor`), mas todos convergem para um método privado comum `registrarComRole`.

**Fluxo de Registro:**
1. Verifica se o e-mail já existe (lança `UsuariojaExisteException`).
2. Busca a *role* correspondente no banco.
3. Converte o DTO em Entidade usando o `AuthMapper` (que já codifica a senha).
4. Salva o usuário no banco.

### 🛡️ Validação de Gestor
No método `registrarGestor`, o serviço invoca `request.validarCodigo(admSecret)`. Se o código fornecido no DTO não coincidir com a configuração do servidor, uma exceção é lançada, impedindo a criação de administradores não autorizados.