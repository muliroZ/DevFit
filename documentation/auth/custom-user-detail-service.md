# 👤 CustomUserDetailsService — Documentação Técnica

A classe `CustomUserDetailsService` implementa a interface padrão do Spring Security, `UserDetailsService`.
Seu objetivo é servir como uma ponte entre o sistema de segurança do Spring e o repositório de dados da aplicação (`UsuarioRepository`).

## 🎯 Responsabilidade Principal

O Spring Security não sabe como os usuários são armazenados no banco de dados da aplicação (tabela `usuario`). Esta classe ensina ao Spring como buscar um usuário baseado no login (email).

## 🔧 Método Implementado

### `loadUserByUsername(String email)`

Este método é chamado automaticamente pelo Spring Security durante o processo de autenticação.

**Fluxo:**
1.  Recebe o email como parâmetro.
2.  Chama `usuarioRepository.findByEmail(email)` para buscar no banco PostgreSQL.
3.  Se o usuário for encontrado:
    * Retorna o objeto `Usuario`.
    * *Nota:* A entidade `Usuario` do DevFit implementa a interface `UserDetails`, permitindo que seja retornada diretamente.
4.  Se o usuário não for encontrado:
    * Lança a exceção `UsernameNotFoundException` ("Usuário não encontrado.").

## 🏗️ Estrutura

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) ...
}
```

Essa implementação permite que o `JwtAuthenticationFilter` verifique se o usuário contido no token ainda existe e é válido no banco de dados antes de autenticar a requisição.