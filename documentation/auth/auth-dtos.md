# 📦 DTOs de Autenticação — Documentação Técnica

Os Data Transfer Objects (DTOs) deste módulo padronizam a entrada e saída de dados de autenticação e garantem validações prévias.

## 📥 Requests

### `LoginRequest`
Usado para autenticação.
* **Campos:** `email`, `senha`.
* **Validações:** `@NotNull`.

### `CadastroRequest`
Usado para cadastro de Alunos e Instrutores.
* **Campos:**
    * `nome`: `@NotBlank`.
    * `email`: `@NotBlank`, `@Email`.
    * `senha`: `@Size(min=8)`, `@Pattern` (exige maiúscula, minúscula e número).
* **Interface:** Implementa `CadastroBase`.

### `CadastroGestorRequest`
Específico para cadastro de administradores.
* **Campos:** Herda campos de cadastro + `gestorCode`.
* **Lógica:** Possui método `validarCodigo(String codigo)` para checar a chave mestra.

### Interface `CadastroBase`
Interface comum implementada pelos requests de cadastro para permitir polimorfismo no `AuthMapper`. Define os contratos para `nome()`, `email()` e `senha()`.

## 📤 Response

### `AuthResponse`
Retornado após um login bem-sucedido.
* **Estrutura:** Record simples contendo apenas o `token` (String).