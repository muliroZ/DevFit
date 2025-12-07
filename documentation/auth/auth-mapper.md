# 🗺️ AuthMapper — Documentação Técnica

Componente responsável por converter os DTOs de cadastro em entidades `Usuario`.

## 🧩 Responsabilidade
Isolar a lógica de conversão e criptografia de senha do *Service* e do *Controller*.

## 🛠️ Método `toEntity`

```java
public Usuario toEntity(CadastroBase request, Role role)
```

1. Recebe um DTO que implementa CadastroBase (pode ser aluno, instrutor ou gestor).

2. Recebe a Role já carregada do banco.

3. Cria uma nova instância de Usuario.

4. Mapeia nome e email.

5. Criptografa a senha usando o passwordEncoder injetado.

6. Define o conjunto de roles do usuário.

Essa abordagem centraliza a regra de que "toda senha deve ser criptografada antes de virar entidade".