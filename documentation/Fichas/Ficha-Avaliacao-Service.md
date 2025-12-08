## 📝 Documentação da Classe `FichaAvaliacaoService`

A classe `FichaAvaliacaoService` é o **serviço principal** responsável pela lógica de negócios e gerenciamento das operações CRUD (Criar, Ler, Atualizar, Deletar) para as Fichas de Avaliação. Ela atua como uma camada intermediária entre o *Controller* (web) e os *Repositories* (dados).

---

### **🏷️ Informações do Componente**

| Anotação | Descrição |
| :--- | :--- |
| `@Service` | Indica que a classe contém lógica de negócios e é um componente gerenciado pelo Spring. |
| `@Transactional` | Anotada em métodos que modificam o estado do banco de dados (`criar`, `atualizar`, `deletar`), garantindo a atomicidade das operações. |
| **Injeções** | Injeta `FichaAvaliacaoRepository`, `UsuarioRepository` e `FichaAvaliacaoMapper` via construtor. |

---

### **1. ➕ Criação de Ficha (`criar`)**

Este método é responsável por registrar uma nova ficha de avaliação no sistema.

| Etapa | Ação | Dependência/Regra de Negócio |
| :--- | :--- | :--- |
| **1. Validação** | Busca as entidades `Aluno` e `Instrutor`. | Ambos devem ser encontrados no `UsuarioRepository` e possuir as *roles* (`ALUNO`/`INSTRUTOR`) correspondentes, ou uma `ResourceNotFoundException` é lançada. |
| **2. Mapeamento** | Mapeia o DTO (`FichaAvaliacaoRequest`) para a entidade `FichaAvaliacao`. | Usa o `FichaAvaliacaoMapper`. A data de avaliação é definida como **`LocalDate.now()`**. |
| **3. Cálculo** | Calcula o **IMC** (Índice de Massa Corporal). | Usa o método privado `calcularImc(pesoKg, alturaCm)` e salva o resultado na entidade. $$IMC = \frac{\text{Peso} (kg)}{(\text{Altura} / 100)^2}$$ |
| **4. Persistência** | Salva a ficha no banco de dados. | Usa `fichaAvaliacaoRepository.save()`. |
| **5. Resposta** | Mapeia a entidade salva para o DTO de resposta. | Usa `fichaAvaliacaoMapper.toResponse()`. |

---

### **2. 🔎 Leitura de Fichas (`buscarFichasPorId`, `listar`)**

Estes métodos recuperam as fichas de avaliação.

* **`buscarFichasPorId(Long alunoId)`:** Retorna a **lista de todas as fichas de avaliação** de um aluno específico, utilizando o método `findByAlunoId()` do repositório.
* **`listar()`:** Retorna **todas as fichas de avaliação** cadastradas no sistema, utilizando o método `findAll()` do repositório.

Em ambos os casos, o resultado é convertido de uma lista de entidades para uma lista de `FichaAvaliacaoResponse` usando *streams* e o `FichaAvaliacaoMapper`.

---

### **3. ✏️ Atualização de Ficha (`atualizar`)**

Permite a modificação completa de uma ficha de avaliação existente.

| Etapa | Ação | Regra de Negócio |
| :--- | :--- | :--- |
| **1. Busca (Antiga)** | Busca a ficha existente pelo `id`. | Deve existir, caso contrário lança `ResourceNotFoundException`. |
| **2. Validação** | Busca e valida as entidades `Aluno` e `Instrutor` do DTO de requisição. | Reutiliza o método `buscarUsuarioPorIdERole()`. |
| **3. Mapeamento** | Mapeia o DTO para uma **nova entidade temporária**. | Usa o `FichaAvaliacaoMapper`. |
| **4. ID e Cálculo** | Define o `id` da entidade temporária para o `id` da ficha antiga e **recalcula o IMC**. | Garante que a operação de `save()` na próxima etapa seja um **UPDATE** e não um INSERT, e que o IMC esteja atualizado. |
| **5. Persistência** | Salva a ficha atualizada no banco de dados. | `fichaAvaliacaoRepository.save(fichaAtualizada)`. |

---

### **4. 🗑️ Exclusão de Ficha (`deletar`)**

Remove uma ficha de avaliação pelo seu ID.

* **Validação:** Verifica se a ficha existe (`existsById`). Se não existir, lança `ResourceNotFoundException`.
* **Ação:** Chama `fichaAvaliacaoRepository.deleteById(id)`.

---

### **5. ⚙️ Métodos Auxiliares**

| Método | Finalidade | Regra de Negócio |
| :--- | :--- | :--- |
| `calcularImc(pesoKg, alturaCm)` | Calcula o IMC. | Lança `IllegalArgumentException` se peso for $\le 0$ ou altura for $\le 0$. A altura em cm é convertida para metros para o cálculo. |
| `buscarUsuarioPorIdERole(id, role)` | Centraliza a busca de usuários com *role* específica. | Reutilizado em `criar` e `atualizar` para garantir que os IDs de usuário pertençam à *role* esperada (ex: um Aluno não pode ser um Instrutor). |

---

