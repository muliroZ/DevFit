# 🛡️ JwtAuthenticationFilter — Documentação Técnica

O `JwtAuthenticationFilter` é um filtro personalizado que estende `OncePerRequestFilter`, garantindo que seja executado uma única vez para cada requisição HTTP recebida pela API.

Ele atua como o "porteiro" da aplicação, verificando se a requisição possui um token válido antes de permitir o acesso aos *controllers*.

## 🔄 Fluxo de Execução (`doFilterInternal`)

O método principal `doFilterInternal` realiza os seguintes passos:

### 1. Verificação de Rotas Públicas (Bypass)
O filtro verifica o caminho da requisição (`request.getServletPath()`). Se a rota for pública (ex: login, cadastro, arquivos estáticos HTML/CSS/JS), o filtro passa a requisição adiante sem verificação.

Exemplos de rotas ignoradas:
* `/auth/**`
* `/*.html`
* `/css/**`, `/js/**`
* `/produtos` (GET)

### 2. Extração do Token
Verifica o cabeçalho `Authorization`.
* Se for nulo ou não começar com "Bearer ", a requisição segue sem autenticação.
* Se presente, o token é extraído (remove-se o prefixo "Bearer ").

### 3. Validação e Autenticação
Se o token existir e não houver autenticação no contexto atual:
1.  Extrai o email e a role do token usando `JwtUtil`.
2.  Carrega os detalhes do usuário via `CustomUserDetailsService`.
3.  Valida o token (`jwtUtil.validateToken`).
4.  Cria um objeto `UsernamePasswordAuthenticationToken` contendo o usuário e suas autoridades (roles).
5.  Registra a autenticação no contexto do Spring Security:
    ```java
    SecurityContextHolder.getContext().setAuthentication(auth);
    ```

## 🧩 Dependências Injetadas

* **`JwtUtil`**: Para operações de leitura e validação do token.
* **`CustomUserDetailsService`**: Para carregar o usuário do banco de dados e verificar sua existência.