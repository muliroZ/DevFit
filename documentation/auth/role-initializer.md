# ⚙️ RoleInitializer — Documentação Técnica

Uma classe de configuração (`@Configuration`) que garante a integridade dos dados de segurança ao iniciar a aplicação.

## 🎯 Objetivo
Garantir que todas as Roles definidas no Enum `UsuarioRole` existam na tabela `role` do banco de dados. Isso evita erros ao tentar cadastrar usuários em um banco vazio.

## 🔧 Funcionamento
Define um Bean `CommandLineRunner` que:
1. Itera sobre todos os valores de `UsuarioRole`.
2. Verifica se a Role já existe no `RoleRepository`.
3. Se não existir, salva uma nova entrada no banco.

Isso automatiza o *seed* inicial de permissões.