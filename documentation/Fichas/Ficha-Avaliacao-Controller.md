## 🌐 Documentação da Classe `FichaAvaliacaoController`

A classe `FichaAvaliacaoController` é um **controlador REST** que gerencia os *endpoints* da API para as operações de Fichas de Avaliação. Ela lida com a entrada de dados (requisições HTTP), validações e controle de acesso, delegando a lógica de negócios para o `FichaAvaliacaoService`.

-----

### **🏷️ Informações do Controlador**

| Anotação | Descrição |
| :--- | :--- |
| `@RestController` | Define a classe como um controlador REST, onde os métodos retornam dados serializados (geralmente JSON). |
| `@RequestMapping("/fichas/avaliacao")` | Define o **caminho base** para todos os recursos controlados por esta classe (ex: `/fichas/avaliacao`). |
| **Injeção de Dependência** | O `FichaAvaliacaoService` é injetado via construtor para executar as operações de serviço. |

-----

### **1. ➕ Criar Ficha de Avaliação**

| Método | Caminho | Nível de Acesso (`@PreAuthorize`) | Descrição |
| :--- | :--- | :--- | :--- |
| `criarFichaAvaliacao` | `POST /fichas/avaliacao/criar` | `INSTRUTOR`, `GESTOR` | **Cria** uma nova ficha de avaliação. O corpo da requisição (`@RequestBody`) é **validado** usando `@Valid`. Retorna o objeto criado com *status* **`201 CREATED`**. |

-----

### **2. 🔎 Leitura e Busca de Fichas**

| Método | Caminho | Nível de Acesso (`@PreAuthorize`) | Descrição |
| :--- | :--- | :--- | :--- |
| `buscarTodasAsFichas` | `GET /fichas/avaliacao` | `INSTRUTOR`, `GESTOR` | Retorna uma lista de **todas** as fichas de avaliação no sistema. |
| `buscarFichasPorAluno` | `GET /fichas/avaliacao/aluno/{alunoId}` | `INSTRUTOR`, `GESTOR` | Retorna a **lista de histórico de avaliações** de um aluno específico, identificado pelo `alunoId` no caminho (`@PathVariable`). |
| `buscarFichaAvaliacaoPorId` | `GET /fichas/avaliacao/{id}` | `INSTRUTOR`, `GESTOR` | Retorna **uma única ficha de avaliação** pelo seu ID. *Nota: Este método assume que a busca por ID de avaliação retorna um único resultado, embora utilize o serviço de busca por aluno para localizar.* |
| `listarMinhasAvaliacoes` | `GET /fichas/avaliacao/minhas-avaliacoes` | `ALUNO`, `INSTRUTOR`, `GESTOR` | Retorna as avaliações associadas ao **usuário autenticado**. O ID do usuário é extraído do `SecurityContextHolder` e usado para buscar suas fichas. |

-----

### **3. ✏️ Atualização de Ficha**

| Método | Caminho | Nível de Acesso (`@PreAuthorize`) | Descrição |
| :--- | :--- | :--- | :--- |
| `atualizarFichaAvaliacao` | `PUT /fichas/avaliacao/atualizar/{id}` | `INSTRUTOR`, `GESTOR` | **Atualiza** uma ficha de avaliação existente, identificada pelo ID (`@PathVariable`). O corpo da requisição (`@Valid`) contém os novos dados. |

-----

### **4. 🗑️ Exclusão de Ficha**

| Método | Caminho | Nível de Acesso (`@PreAuthorize`) | Descrição |
| :--- | :--- | :--- | :--- |
| `deletarFichaAvalicao` | `DELETE /fichas/avaliacao/excluir/{id}` | `GESTOR` | **Exclui** uma ficha de avaliação pelo ID. O acesso é restrito ao *role* **`GESTOR`**. Retorna *status* **`204 NO CONTENT`** (`@ResponseStatus`). |

-----

### **🔒 Controle de Acesso e Segurança**

O acesso a todos os *endpoints* é estritamente controlado pelo **Spring Security** usando `@PreAuthorize`:

* **GESTOR** e **INSTRUTOR** têm permissão para criar, listar, buscar por ID de aluno e atualizar fichas.
* A operação de **deleção** é reservada **apenas** para o `GESTOR`.
* O *endpoint* `/minhas-avaliacoes` permite que qualquer usuário autenticado (`ALUNO`, `INSTRUTOR`, `GESTOR`) acesse suas próprias avaliações, obtendo o ID do usuário do contexto de segurança:
  ```java
  Usuario UsuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  Long alunoId = UsuarioAutenticado.getId();
  ```

-----

