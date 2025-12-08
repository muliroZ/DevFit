

## 📦 Documentação Consolidada dos DTOs

Esta seção documenta os DTOs (Data Transfer Objects) utilizados para as requisições (entradas) e respostas (saídas) nas operações de Fichas de Treino e Fichas de Avaliação.

---

## 1. 📋 DTOs de Ficha de Avaliação (`FichaAvaliacao`)

### A. `FichaAvaliacaoRequest` (Entrada/Criação/Atualização)

Representa os dados enviados pelo cliente para criar ou atualizar uma ficha de avaliação.

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `idAluno` | `Long` | **ID do Aluno** sendo avaliado. |
| `idInstrutor` | `Long` | **ID do Instrutor** responsável pela avaliação. |
| `pesoKg` | `Double` | Peso corporal registrado (em quilogramas). |
| `alturaCm` | `Double` | Altura registrada (em centímetros). |
| `circunferenciaAbdomenCm` | `Double` | Medida da circunferência do abdômen (em centímetros). |
| `circunferenciaCinturaCm` | `Double` | Medida da circunferência da cintura (em centímetros). |
| `circunferenciaQuadrilCm` | `Double` | Medida da circunferência do quadril (em centímetros). |
| `historicoSaude` | `String` | Relatório textual sobre o histórico de saúde do aluno. |
| `observacoesGerais` | `String` | Observações adicionais feitas pelo instrutor. |

### B. `FichaAvaliacaoResponse` (Saída/Resposta)

Representa os dados retornados ao cliente após a criação, busca ou atualização de uma ficha de avaliação.

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | ID da ficha de avaliação. |
| `aluno` | `UsuarioInfoDTO` | **DTO simplificado** contendo informações básicas do aluno. |
| `instrutor` | `UsuarioInfoDTO` | **DTO simplificado** contendo informações básicas do instrutor. |
| `dataAvaliacao` | `LocalDate` | Data em que a avaliação foi realizada. |
| `pesoKg` | `Double` | Peso corporal (em kg). |
| `alturaCm` | `Double` | Altura (em cm). |
| `imc` | `Double` | **Índice de Massa Corporal (IMC)** calculado. |
| `circunferenciaCinturaCm` | `Double` | Circunferência da cintura. |
| `circunferenciaAbdomenCm` | `Double` | Circunferência do abdômen. |
| `circunferenciaQuadrilCm` | `Double` | Circunferência do quadril. |
| `historicoSaude` | `String` | Histórico de saúde. |
| `observacoesGerais` | `String` | Observações gerais. |

---

## 2. 💪 DTOs de Ficha de Treino (`FichaTreino`)

### A. `FichaTreinoRequest` (Entrada/Criação/Atualização)

Representa a estrutura complexa enviada pelo cliente para criar ou atualizar um plano de treino, contendo a lista de exercícios.

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `idAluno` | `Long` | **ID do Aluno** que receberá a ficha. |
| `idInstrutor` | `Long` | **ID do Instrutor** que criou a ficha. |
| `dataVencimento` | `LocalDate` | Data em que a ficha de treino deve expirar. |
| `listaDeItens` | `List<ItemTreinoRequest>` | **Lista aninhada** dos exercícios e suas configurações (ver abaixo). |

### B. `ItemTreinoRequest` (DTO Aninhado - Entrada)

Representa um único exercício dentro da lista de exercícios da `FichaTreinoRequest`.

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `exercicioId` | `Long` | **ID do Exercício base** (referência à entidade `Exercicio`). |
| `series` | `int` | Número de séries para o exercício. |
| `repeticoes` | `int` | Número de repetições por série. |
| `cargaEstimadaKg` | `Double` | Carga estimada a ser utilizada. |
| `observacoes` | `String` | Observações específicas sobre a execução (ex: "Foco na cadência"). |

### C. `FichaTreinoResponse` (Saída/Resposta)

Representa a estrutura complexa retornada ao cliente após a manipulação de uma ficha de treino.

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | ID da ficha de treino. |
| `aluno` | `UsuarioInfoDTO` | **DTO simplificado** contendo informações básicas do aluno. |
| `instrutor` | `UsuarioInfoDTO` | **DTO simplificado** contendo informações básicas do instrutor. |
| `dataCriacao` | `LocalDate` | Data em que a ficha foi criada. |
| `dataVencimento` | `LocalDate` | Data de validade da ficha. |
| `ativa` | `boolean` | Indica se a ficha está atualmente ativa. |
| `itensResponse` | `List<ItemTreinoResponse>` | **Lista aninhada** dos exercícios e suas configurações para visualização (ver abaixo). |

### D. `ItemTreinoResponse` (DTO Aninhado - Saída)

Representa um único exercício dentro da `FichaTreinoResponse`.

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | ID do item de treino. |
| `exercicioBase` | `ExercicioInfoDTO` | **DTO simplificado** contendo informações básicas do exercício (nome, músculo principal). |
| `series` | `int` | Número de séries. |
| `repeticoes` | `int` | Número de repetições. |
| `cargaEstimadaKg` | `Double` | Carga estimada. |
| `observacoes` | `String` | Observações específicas. |

---

## 3. ℹ️ DTOs de Informação Simplificada (Reuso)

Esses DTOs são reutilizados nas respostas (`Response`) das fichas para evitar a exposição de entidades completas.

### A. `UsuarioInfoDTO`

Usado para representar o `Aluno` e o `Instrutor` em respostas de `FichaTreino` e `FichaAvaliacao`.

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | ID do usuário. |
| `nome` | `String` | Nome completo do usuário. |
| `email` | `String` | Email do usuário. |

### B. `ExercicioInfoDTO`

Usado para representar o `ExercicioBase` dentro do `ItemTreinoResponse`.

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | `Long` | ID do exercício base. |
| `nome` | `String` | Nome do exercício (ex: "Supino Reto"). |
| `musculoPrincipal` | `String` | Grupo muscular principal trabalhado. |

---

