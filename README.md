# Kono Sudoku Da!

Este é um projeto de jogo de Sudoku desenvolvido em Java. O projeto oferece duas interfaces: uma visual moderna (JavaFX) e uma clássica via terminal. O projeto foi criado com base nos fundamentos de Java aprendidos na [DIO (Digital Innovation One)](https://www.dio.me/) na Formação Java Fundamentals, inspirado no repositório [digitalinnovationone/sudoku](https://github.com/digitalinnovationone/sudoku).

## 📋 Sobre o Projeto

O objetivo deste projeto é fornecer uma implementação completa e jogável de Sudoku, aplicando conceitos de Orientação a Objetos, manipulação de estruturas de dados e lógica de programação em Java.

O jogo permite configurar o tamanho do tabuleiro e a dificuldade, gerando desafios únicos a cada partida através de algoritmos de embaralhamento.

## 🚀 Funcionalidades

- **Múltiplas Dimensões:**
  - **4x4 (Pequeno):** Ideal para iniciantes ou testes rápidos.
  - **9x9 (Médio):** O formato clássico do Sudoku.
  - **16x16 (Grande):** Um desafio maior utilizando números e letras (1-9, A-G).
  
- **Níveis de Dificuldade:**
  - Aprendiz (Fácil)
  - Estrategista (Médio)
  - Mestre (Difícil)
  
- **Geração com Solução Única:** O algoritmo de geração garante que cada desafio tenha uma e apenas uma solução, evitando ambiguidades.
  
- **Interface Responsiva (GUI):** A geração do tabuleiro ocorre em segundo plano, exibindo um indicador de carregamento e mantendo a interface fluida.

- **Sistema de Jogo Completo:**
  - Inserção e remoção de números.
  - Validação de jogadas (impede alteração de números fixos).
  - Verificação de status (incompleto, com erros, etc.).
  - Opção de limpar o tabuleiro ou finalizar a partida.

## 🧠 Lógica de Geração e Embaralhamento

A geração de cada desafio é um processo robusto para garantir que cada partida seja justa e tenha uma única solução.

1. **Geração de Base Resolvida:** Primeiro, uma grade completa e válida é criada usando um padrão matemático.

2. **Embaralhamento:** Para criar variedade, esta grade base é embaralhada de múltiplas formas, preservando sua validade:

   - **Números:** Os valores são mapeados aleatoriamente (ex: todos os 1s se tornam 5s, 2s se tornam 8s, etc.).
   - **Linhas e Colunas:** Linhas e colunas são trocadas aleatoriamente, mas **apenas dentro de seus respectivos blocos** (ex: em um 9x9, as linhas 0-2 podem trocar entre si, mas nunca com as linhas 3-5).
  
3. **Criação do Quebra-Cabeça (Puzzle):** Esta é a etapa crucial. Células são removidas uma a uma em ordem aleatória. Após remover cada célula, um **algoritmo de backtracking (solver)** verifica se o tabuleiro continua tendo **uma e apenas uma solução**. Se a remoção criar múltiplas soluções ou nenhuma, a célula é restaurada.

Este processo garante que todo jogo gerado seja desafiador e livre de ambiguidades.

## 🛠️ Tecnologias Utilizadas

- **Java 21:** Linguagem principal (definida no `pom.xml`).
- **JavaFX:** Framework para a interface gráfica.
- **Maven:** Gerenciamento de dependências e build.

## 📦 Como Executar

### Pré-requisitos

Certifique-se de ter o **JDK 21** (ou superior) instalado.
Também é necessário ter o **Maven** instalado para compilar o projeto e gerar o Fat JAR.

### Passos

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/gabrielmkv/sudoku-project.git
   cd sudoku-project
   ```

2. **Compile e execute (via Maven):**
   O projeto utiliza o `maven-shade-plugin` para gerar um JAR executável (Fat JAR) contendo todas as dependências.
   ```bash
   mvn clean package
   ```
   
   - **Para jogar com Interface Gráfica (Padrão):**
      ```bash
      java -jar target/sudoku-project-1.0-SNAPSHOT.jar
      ```

   - **Para jogar no Terminal (Console):** 
      ```bash
      java -jar target/sudoku-project-1.0-SNAPSHOT.jar --console
      ```

### 💡 Dica: Criando um atalho (Alias)

Caso queira executar o jogo digitando apenas `sudoku` (GUI) ou `sudoku-cli` (Terminal), adicione os seguintes aliases no seu arquivo `.bashrc` (ou `.zshrc`):

```bash
alias sudoku='java -jar /caminho/completo/ate/o/projeto/target/sudoku-project-1.0-SNAPSHOT.jar' # Abre GUI
alias sudoku-cli='java -jar /caminho/completo/ate/o/projeto/target/sudoku-project-1.0-SNAPSHOT.jar --console' # Abre Terminal
```

## 🎮 Como Jogar

### Interface Gráfica (GUI)

1. **Menu Inicial:** Selecione a dificuldade e o tamanho do tabuleiro nos menus suspensos e clique em "INICIAR JOGO".

2. **Carregamento:** Um indicador de carregamento aparecerá enquanto um novo desafio único é preparado para você.

3. **Tabuleiro:**

   - Clique em uma célula para selecioná-la.
   - Digite o número desejado (ou letras A-G para o modo 16x16).
   - Células fixas (em negrito) não podem ser alteradas.
  
4. **Controles:** Utilize os botões na parte inferior para verificar status, limpar o jogo ou finalizar a partida.

### Modo Terminal (Console)

Ao iniciar com a flag `--console`, siga as instruções no menu interativo:

1. **Configuração:** Digite os números correspondentes para escolher o tamanho e a dificuldade.

2. **Menu Principal:** Utilize as opções numéricas para interagir.

   - Para jogar, selecione a opção de inserir número, informe a **Coluna** (vertical) e a **Linha** (horizontal).
   - Em tabuleiros **16x16**, utilize letras de **A** a **G** para representar os valores de 10 a 16.

## 👤 Autor

Desenvolvido por **Gabriel** ([gabriel-mkv](https://github.com/gabriel-mkv)).

---
*Projeto desenvolvido para fins de estudo.*