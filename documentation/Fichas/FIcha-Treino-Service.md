## 📝 Documentação da Classe `FichaTreinoService`

A classe `FichaTreinoService` é a camada de **lógica de negócios** responsável por gerenciar as operações relacionadas à criação, consulta e modificação das Fichas de Treino. Ela coordena a interação entre os *Repositories* (dados), os *Mappers* (conversão) e o fluxo principal de requisições.

---

### **🏷️ Informações do Componente**

| Anotação | Descrição |
| :--- | :--- |
| `@Service` | Indica que a classe contém a lógica de negócios e é um componente gerenciado pelo Spring. |
| `@Transactional` | Anotada em métodos que modificam o banco de dados (`criar`, `atualizar`, `deletar`), garantindo que as operações sejam executadas de forma atômica (tudo ou nada). |
| **Injeções** | Injeta `FichaTreinoRepository`, `UsuarioRepository`, `ExercicioRepository` e `FichaTreinoMapper` via construtor, necessárias para acesso a dados e conversão. |

---

### **1. ➕ Criação de Ficha (`criar`)**

Cria uma nova ficha de treino complexa, incluindo seus itens.

| Etapa | Ação | Dependência/Regra de Negócio |
| :--- | :--- | :--- |
| **1. Validação Usuários** | Busca e valida `Aluno` e `Instrutor`. | Usa o método auxiliar `buscarUsuarioPorIdERole()` para garantir que os IDs existam e que tenham as *roles* corretas (`ALUNO`, `INSTRUTOR`). |
| **2. Mapeamento Itens** | Cria a lista de entidades `ItemTreino`. | Usa o método auxiliar `listarItensDoRequest()` para buscar cada `Exercicio` base e mapear o DTO para a entidade `ItemTreino`. |
| **3. Mapeamento Ficha** | Mapeia o DTO principal para a entidade `FichaTreino`. | Usa o `FichaTreinoMapper`, definindo a data de criação como **`LocalDate.now()`**. |
| **4. Persistência** | Salva a entidade `FichaTreino`. | `fichaTreinoRepository.save()`. A persistência dos `ItemTreino` é tratada por **cascade** na relação. |
| **5. Resposta** | Mapeia a entidade salva para `FichaTreinoResponse`. | `fichaTreinoMapper.toResponse()`. |

---

### **2. 🔎 Leitura de Fichas (`listar`, `buscarFichasPorId`, `buscarPorId`)**

Recupera os dados das fichas de treino.

* **`listar()`:** Retorna **todas as fichas** de treino do sistema.
* **`buscarFichasPorId(Long alunoId)`:** Retorna a **lista de fichas** pertencentes a um aluno específico, utilizando o `findByAlunoId()` do repositório.
* **`buscarPorId(Long id)`:** Retorna uma **ficha única** pelo seu ID. Se não for encontrada, lança `ResourceNotFoundException`.

Em todos os métodos de leitura, a entidade é convertida para `FichaTreinoResponse` usando o `FichaTreinoMapper` antes de ser retornada.

---

### **3. ✏️ Atualização de Ficha (`atualizar`)**

Permite a substituição completa dos dados de uma ficha de treino existente.

| Etapa | Ação | Regra de Negócio |
| :--- | :--- | :--- |
| **1. Busca (Existente)** | Busca a ficha existente pelo `id`. | Lança `ResourceNotFoundException` se não encontrada. |
| **2. Validação** | Busca e valida `Aluno` e `Instrutor` do DTO. | Reutiliza `buscarUsuarioPorIdERole()`. |
| **3. Mapeamento** | Cria uma **nova entidade** `FichaTreino` com os dados do DTO. | Reutiliza `listarItensDoRequest()` e `fichaTreinoMapper.toEntity()`. |
| **4. ID** | **Transfere o ID** da ficha existente para a nova entidade. | `atualizada.setId(existente.getId());` Garante que o método `save()` atualize a ficha existente (UPDATE) e não crie uma nova (INSERT). |
| **5. Persistência** | Salva a ficha atualizada. | `fichaTreinoRepository.save(atualizada)`. |

---

### **4. 🗑️ Exclusão de Ficha (`deletar`)**

Remove uma ficha de treino pelo seu ID.

* **Validação:** Busca a ficha pelo ID e lança `ResourceNotFoundException` se não for encontrada.
* **Ação:** Remove a ficha usando `fichaTreinoRepository.delete(ficha)`.

---

### **5. ⚙️ Métodos Auxiliares (Lógica de Busca)**

| Método | Finalidade | Implementação/Regra |
| :--- | :--- | :--- |
| `buscarUsuarioPorIdERole(id, role)` | Garante que o ID pertença à *role* correta. | Usa `usuarioRepository.findByIdAndRoles_Nome()`. Lança `ResourceNotFoundException` se falhar. |
| `buscarExercicioPorId(id)` | Busca um exercício base pelo ID. | Usa `exercicioRepository.findById()`. Lança `ResourceNotFoundException` se falhar. |
| `listarItensDoRequest(request)` | Cria a lista de `ItemTreino` a partir do DTO. | Itera sobre a lista de DTOs aninhados, buscando a entidade `Exercicio` para cada item (via `buscarExercicioPorId()`) e usando o `FichaTreinoMapper` para conversão. |