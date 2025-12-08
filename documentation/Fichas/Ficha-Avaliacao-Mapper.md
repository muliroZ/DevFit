## 🛠️ Documentação da Classe `FichaAvaliacaoMapper`

A classe `FichaAvaliacaoMapper` é um componente do Spring responsável por realizar a **conversão de objetos (Mapeamento)** entre as diferentes camadas da aplicação. Seu principal papel é transformar:

1.  **DTOs de Requisição (`FichaAvaliacaoRequest`)** em **Entidades de Persistência (`FichaAvaliacao`)**.
2.  **Entidades de Persistência (`FichaAvaliacao`)** em **DTOs de Resposta (`FichaAvaliacaoResponse`)**.

---

### **🏷️ Informações do Componente**

| Anotação | Descrição |
| :--- | :--- |
| `@Component` | Declara que esta classe é um componente gerenciado pelo *Spring Framework*, permitindo sua injeção de dependência em outras classes. |
| **Injeção de Dependência** | Requer o `FichaTreinoMapper` via construtor, pois reutiliza um de seus métodos para mapear informações de usuário. |

---

### **1. 🔄 Método `toEntity` (DTO -> Entidade)**

Este método é responsável por **criar uma nova entidade `FichaAvaliacao`** a partir dos dados recebidos na requisição.

#### **Assinatura:**

`public FichaAvaliacao toEntity(FichaAvaliacaoRequest dto, Usuario aluno, Usuario instrutor, LocalDate dataAvaliacao)`

#### **Mapeamento de Campos:**

| Origem (DTO/Parâmetro) | Destino (Entidade `FichaAvaliacao`) | Observações |
| :--- | :--- | :--- |
| `aluno` (Parâmetro) | `setAluno(aluno)` | Associa a entidade **`Usuario`** do aluno. |
| `instrutor` (Parâmetro) | `setInstrutor(instrutor)` | Associa a entidade **`Usuario`** do instrutor. |
| `dataAvaliacao` (Parâmetro) | `setDataAvaliacao(dataAvaliacao)` | Data da avaliação. |
| `dto.pesoKg()` | `setPesoKg()` | Peso corporal em quilogramas. |
| `dto.alturaCm()` | `setAlturaCm()` | Altura em centímetros. |
| `dto.circunferencia...()` | `setCircunferencia...()` | Valores de circunferências (abdômen, cintura, quadril). |
| `dto.historicoSaude()` | `setHistoricoSaude()` | Informações pré-existentes de saúde. |
| `dto.observacoesGerais()` | `setObservacoesGerais()` | Notas adicionais sobre a avaliação. |

---

### **2. 📤 Método `toResponse` (Entidade -> DTO)**

Este método é responsável por **criar o DTO de resposta (`FichaAvaliacaoResponse`)** que será enviado ao cliente, contendo os dados da entidade e informações adicionais (como os DTOs simplificados de usuário).

#### **Assinatura:**

`public FichaAvaliacaoResponse toResponse(FichaAvaliacao entity)`

#### **Mapeamento de Campos:**

| Origem (Entidade `FichaAvaliacao`) | Destino (DTO `FichaAvaliacaoResponse`) | Observações |
| :--- | :--- | :--- |
| `entity.getId()` | `id` | ID da ficha de avaliação. |
| `entity.getAluno()` | `aluno` | Mapeado usando `fichaTreinoMapper.toUsuarioInfoDTO()`. |
| `entity.getInstrutor()` | `instrutor` | Mapeado usando `fichaTreinoMapper.toUsuarioInfoDTO()`. |
| `entity.getDataAvaliacao()` | `dataAvaliacao` | Data da avaliação. |
| `entity.getPesoKg()`, `entity.getAlturaCm()` | `pesoKg`, `alturaCm` | Medidas básicas. |
| `entity.getImc()` | `imc` | **Índice de Massa Corporal**, provavelmente calculado pela entidade no momento da persistência. |
| `entity.getCircunferencia...()` | `circunferencia...` | Medidas corporais. |
| `entity.getHistoricoSaude()`, `entity.getObservacoesGerais()` | `historicoSaude`, `observacoesGerais` | Dados textuais. |

---

### **⚠️ Dependência Externa**

A dependência de `FichaTreinoMapper` é notável. Ela indica que o mapeamento das informações de **usuário** (tanto para `aluno` quanto para `instrutor`) é centralizado no método `toUsuarioInfoDTO()` dessa outra classe, promovendo **reuso de código** e garantindo que as informações de usuário retornadas em diferentes *endpoints* (Ficha de Treino e Ficha de Avaliação) tenham a **mesma estrutura**.

---

