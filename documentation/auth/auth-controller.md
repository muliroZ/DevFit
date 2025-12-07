# 🎮 AuthController — Documentação Técnica

O `AuthController` é o ponto de entrada para todas as operações públicas de autenticação e registro de usuários. Ele expõe *endpoints* que permitem login e cadastro de diferentes tipos de perfis.

## 🧩 Visão Geral

Este *controller* é responsável por:
* Receber credenciais de login e retornar o token JWT.
* Registrar novos Alunos (público).
* Registrar novos Instrutores (restrito a Gestores).
* Registrar novos Gestores (requer chave de segurança).

## 📌 Endpoints

### 1️⃣ POST `/auth/login`
Autentica um usuário no sistema.

* **Entrada:** `LoginRequest` (email, senha).
* **Processamento:**
    1. O `AuthService` autentica as credenciais via `AuthenticationManager`.
    2. Gera um token JWT contendo as *claims* do usuário.
* **Saída:** `AuthResponse` (token JWT).
* **Status:** `200 OK`.

### 2️⃣ POST `/auth/cadastro`
Registra um novo **Aluno** no sistema.

* **Entrada:** `CadastroRequest` (nome, email, senha).
* **Acesso:** Público.
* **Processamento:** Cria um usuário com a *role* `ALUNO`.
* **Saída:** Mensagem de sucesso.
* **Status:** `201 Created`.

### 3️⃣ POST `/auth/cadastro/instrutor`
Registra um novo **Instrutor**.

* **Entrada:** `CadastroRequest`.
* **Acesso:** Restrito (`hasAuthority('ROLE_GESTOR')`).
* **Processamento:** Cria um usuário com a *role* `INSTRUTOR`.
* **Saída:** Mensagem de sucesso.
* **Status:** `201 Created`.

### 4️⃣ POST `/auth/cadastro/gestor`
Registra um novo **Gestor**.

* **Entrada:** `CadastroGestorRequest` (inclui `gestorCode`).
* **Acesso:** Público (a segurança é garantida pela validação do código secreto).
* **Processamento:** Valida a chave de segurança (`ADMIN_SECRET`) e cria um usuário com a *role* `GESTOR`.
* **Saída:** Mensagem de sucesso.
* **Status:** `201 Created`.