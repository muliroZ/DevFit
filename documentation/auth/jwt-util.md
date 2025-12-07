# 🛠️ JwtUtil — Documentação Técnica

A classe `JwtUtil` é um componente utilitário (`@Component`) responsável por toda a manipulação de tokens JWT (JSON Web Tokens). Ela centraliza a lógica de criptografia, geração de *claims* e validação.

## ⚙️ Configurações

A classe utiliza valores injetados do arquivo `application.properties`:

* **`jwt.secret`**: Chave secreta usada para assinar digitalmente o token.
* **`validity.time`**: Tempo de expiração do token em milissegundos.

## 🔑 Assinatura

Utiliza o algoritmo HMAC-SHA para assinatura segura:

```java
private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
}
```

## 🚀 Principais Métodos

1️⃣ `generateToken(String email, String role, Long id, String nome)`

#### Gera um novo token JWT contendo informações personalizadas (claims) sobre o usuário.

* **Claims incluídas:**

    - role: O papel do usuário (ex: ROLE_GESTOR).

    - id: O identificador primário do usuário no banco.

    - nome: O nome de exibição do usuário.

    - sub: O email do usuário (Subject).

    - iat: Data de emissão (Issued At).

    - exp: Data de expiração.

2️⃣ `validateToken(String token, String email)`

#### Verifica se um token é válido para um determinado usuário. A validação ocorre em duas etapas:

1. Verifica se o email no token corresponde ao email do usuário fornecido.

2. Verifica se o token não está expirado (!isTokenExpired(token)).

3️⃣ `Métodos de Extração`

#### Permitem ler dados de um token recebido:

* `getEmailFromToken(String token)`: Extrai o subject (email).

* `getRoleFromToken(String token)`: Extrai a permissão do usuário.

* `getAllClaimsFromToken(String token)`: Decodifica o token completo usando a chave de assinatura.

## 📦 Dependência
A classe depende da biblioteca JJWT (io.jsonwebtoken) para o parsing e construção dos tokens.