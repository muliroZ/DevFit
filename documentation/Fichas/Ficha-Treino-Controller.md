## 🌐 Documentação da Classe `FichaTreinoController`

A classe `FichaTreinoController` é um **controlador REST** do Spring Boot responsável por expor os *endpoints* da API para o gerenciamento de Fichas de Treino. Ela recebe requisições HTTP, valida os dados de entrada e delega a lógica de negócios para o `FichaTreinoService`.

-----

### **🏷️ Informações do Controlador**

| Anotação | Descrição |
| :--- | :--- |
| `@RestController` | Combinação de `@Controller` e `@ResponseBody`, indicando que a classe trata requisições REST e que os dados de retorno devem ser serializados diretamente como corpo da resposta (geralmente JSON). |
| `@RequestMapping("/fichas/treino")` | Define o **caminho base** para todos os *endpoints* neste controlador (ex: `/fichas/treino`). |
| **Injeção de Dependência** | O `FichaTreinoService` é injetado via construtor para acesso às regras de negócio. |

-----

### **1. ➕ Criar Ficha de Treino**

| Método | Caminho | Nível de Acesso (`@PreAuthorize`) | Descrição |
| :--- | :--- | :--- | :--- |
| `criar` | `POST /fichas/treino` | `GESTOR`, `INSTRUTOR` | Cria uma nova ficha de treino. O corpo da requisição (`@RequestBody`) é validado pelo `@Valid`. Retorna o objeto criado com *status* **`201 CREATED`**. |

### **2. 🔎 Leitura e Busca de Fichas**

| Método | Caminho | Nível de Acesso (`@PreAuthorize`) | Descrição |
| :--- | :--- | :--- | :--- |
| `listar` | `GET /fichas/treino` | `INSTRUTOR`, `GESTOR` | Retorna uma lista de **todas** as fichas de treino cadastradas. |
| `buscarPorId` | `GET /fichas/treino/{id}` | `INSTRUTOR`, `GESTOR` | Retorna uma ficha de treino específica pelo seu ID de banco de dados (`@PathVariable`). |
| `listarMinhasFichas` | `GET /fichas/treino/minhas-fichas` | `ALUNO`, `INSTRUTOR`, `GESTOR` | Retorna as fichas de treino associadas ao **usuário autenticado**. O ID do aluno é extraído do `SecurityContextHolder` (do *Principal* autenticado). |

### **3. ✏️ Atualização de Ficha**

| Método | Caminho | Nível de Acesso (`@PreAuthorize`) | Descrição |
| :--- | :--- | :--- | :--- |
| `atualizar` | `PUT /fichas/treino/{id}` | `GESTOR`, `INSTRUTOR` | Atualiza uma ficha de treino existente com base no ID (`@PathVariable`). O corpo da requisição é validado (`@Valid`). Retorna o objeto atualizado com *status* **`200 OK`**. |

### **4. 🗑️ Exclusão de Ficha**

| Método | Caminho | Nível de Acesso (`@PreAuthorize`) | Descrição |
| :--- | :--- | :--- | :--- |
| `deletar` | `DELETE /fichas/treino/{id}` | `GESTOR` | Exclui uma ficha de treino pelo ID. A exclusão é restrita apenas ao **GESTOR**. Retorna *status* **`204 NO CONTENT`** (sucesso sem corpo de resposta). |

-----

### **🔒 Controle de Acesso e Segurança**

O controlador utiliza o **Spring Security** para impor restrições de acesso em cada *endpoint* através da anotação `@PreAuthorize`:

* **GESTOR** e **INSTRUTOR** possuem acesso total para criar, listar (todas), buscar e atualizar fichas.
* O **GESTOR** é o único que pode deletar fichas.
* O **ALUNO** só possui permissão para acessar suas próprias fichas através do *endpoint* `/minhas-fichas`.

No método `listarMinhasFichas`, o ID do aluno é obtido diretamente do objeto `Usuario` que está armazenado no contexto de segurança:

```java
Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
Long alunoId = usuarioAutenticado.getId();
```

Essa é uma prática de segurança comum para garantir que os usuários só possam solicitar dados diretamente associados à sua própria identidade, independentemente de qual ID possa ser passado na URL (o ID é ignorado a favor do ID autenticado).

-----

