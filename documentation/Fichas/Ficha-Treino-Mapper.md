## 🛠️ Documentação da Classe `FichaTreinoMapper`

A classe `FichaTreinoMapper` é um componente essencial responsável por **mapear e converter** objetos entre as camadas de Entidades (Modelos JPA) e DTOs (Objetos de Transferência de Dados) específicos para o fluxo de Fichas de Treino.

---

### **🏷️ Informações do Componente**

| Anotação | Descrição |
| :--- | :--- |
| `@Component` | Indica que a classe é um componente gerenciado pelo *Spring Framework* e está disponível para **injeção de dependência**. |
| **Função** | Realiza o mapeamento complexo da estrutura `FichaTreino` que contém uma lista aninhada de `ItemTreino`. |

---

### **1. 🔄 Mapeamento Principal: `FichaTreinoRequest` $\leftrightarrow$ `FichaTreino`**

#### **1.1. `toEntity(FichaTreinoRequest...)` (DTO $\rightarrow$ Entidade Principal)**

Este método constrói a entidade `FichaTreino` a partir dos dados de requisição, associando as entidades relacionadas (`Usuario`) e a lista de itens de treino já mapeada (`ItemTreino`).

| Origem (DTO/Parâmetro) | Destino (Entidade `FichaTreino`) | Observações |
| :--- | :--- | :--- |
| `aluno`, `instrutor` | `setAluno()`, `setInstrutor()` | Entidades `Usuario` já carregadas. |
| `dataCriacao` | `setDataCriacao()` | Data de criação da ficha. |
| `dto.dataVencimento()` | `setDataVencimento()` | Define a validade do plano. |
| `itensTreino` | `setListaDeItens()` | Lista de `ItemTreino` já mapeados. |
| *(Implícito)* | `setAtiva(true)` | Define a ficha como ativa por padrão na criação. |

#### **1.2. `toResponse(FichaTreino entity)` (Entidade Principal $\rightarrow$ DTO de Resposta)**

Este método converte a entidade `FichaTreino` completa para o DTO de resposta, incluindo a **conversão recursiva** de sua lista de itens.

* **Processo:**
    1.  Mapeia cada `ItemTreino` na `ListaDeItens` para um `ItemTreinoResponse` usando `this::toResponse`.
    2.  Mapeia o `Aluno` e o `Instrutor` para DTOs simplificados (`UsuarioInfoDTO`) usando `toUsuarioInfoDTO()`.
    3.  Cria o `FichaTreinoResponse` final com todos os campos e a lista aninhada de respostas.

---

### **2. 🏋️‍♂️ Mapeamentos Aninhados: `ItemTreinoRequest` $\leftrightarrow$ `ItemTreino`**

#### **2.1. `toEntity(ItemTreinoRequest...)` (DTO $\rightarrow$ Entidade Item)**

Este método mapeia os detalhes de um exercício dentro da ficha, associando a entidade `Exercicio` base.

| Origem (DTO/Parâmetro) | Destino (Entidade `ItemTreino`) | Observações |
| :--- | :--- | :--- |
| `exercicioBase` | `setExercicioBase()` | Entidade `Exercicio` já carregada. |
| `dto.series()` | `setSeries()` | Número de séries. |
| `dto.repeticoes()` | `setRepeticoes()` | Número de repetições. |
| `dto.cargaEstimadaKg()` | `setCargaEstimadaKg()` | Carga sugerida. |
| `dto.observacoes()` | `setObservacoes()` | Notas específicas. |

#### **2.2. `toResponse(ItemTreino entity)` (Entidade Item $\rightarrow$ DTO de Resposta)**

Converte a entidade do item de treino para o DTO de resposta.

* **Destaque:** Mapeia a entidade `ExercicioBase` para um DTO simplificado (`ExercicioInfoDTO`) usando `toExercicioInfoDTO()`, evitando o retorno de detalhes desnecessários do exercício base.

---

### **3. 👤 Mapeamentos de Informação Simplificada (Métodos Auxiliares)**

Esses métodos são usados para evitar a exposição de entidades complexas e garantir que apenas informações essenciais sejam retornadas.

| Método | Origem (Entidade) | Destino (DTO) | Campos Incluídos | Uso Principal |
| :--- | :--- | :--- | :--- | :--- |
| `toUsuarioInfoDTO()` | `Usuario` | `UsuarioInfoDTO` | `id`, `nome`, `email` | Usado para `Aluno` e `Instrutor` na `FichaTreinoResponse`. |
| `toExercicioInfoDTO()` | `Exercicio` | `ExercicioInfoDTO` | `id`, `nome`, `musculoPrincipal` | Usado para o `ExercicioBase` na `ItemTreinoResponse`. |

---

