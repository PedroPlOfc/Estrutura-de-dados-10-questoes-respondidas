# Questão 1 — Senhas de Atendimento

## 1. Metodologia e Estrutura de Dados Escolhida

Para resolver o problema de atendimento por ordem rigorosa de chegada, o comportamento exigido segue o princípio **FIFO** (*First-In, First-Out* — o primeiro a entrar é o primeiro a sair).

### Estrutura Adotada: Fila Encadeada Simples
A implementação foi construída manualmente utilizando **nós e referências** (sem bibliotecas prontas do Java), mantendo dois ponteiros principais na estrutura da fila:
* **`inicio` (cabeça):** aponta para o primeiro elemento da fila (próximo a ser atendido).
* **`fim` (cauda):** aponta para o último elemento inserido na fila.

**Por que essa abordagem?**
Manter referências diretas tanto para o `inicio` quanto para o `fim` permite inserir novos pacientes no final e remover pacientes atendidos no início sem precisar percorrer a estrutura, evitando deslocamentos de memória e limitações de tamanho fixo de vetores.

### Análise de Complexidade
* **Enfileirar (`CHEGAR X`):** $O(1)$ — inserção direta através da referência `fim`.
* **Desenfileirar (`ATENDER`):** $O(1)$ — remoção direta através da referência `inicio`.
* **Consultar próximo (`PROXIMO`):** $O(1)$ — leitura direta do valor em `inicio`.
* **Complexidade de Espaço:** $O(N)$, onde $N$ é o número máximo de senhas aguardando simultaneamente na fila.

---

## 2. Código-Fonte (`Main.java`)

```java
// Estrutura de dados escolhida: Fila encadeada (implementada com nós e referências).
// Complexidade das operações principais:
// - Enfileirar (CHEGAR): O(1)
// - Desenfileirar (ATENDER): O(1)
// - Consultar início (PROXIMO): O(1)

import java.util.Scanner;

public class Main {

    static class No {
        int valor;
        No proximo;

        No(int valor) {
            this.valor = valor;
            this.proximo = null;
        }
    }

    static class Fila {
        private No inicio;
        private No fim;

        public Fila() {
            this.inicio = null;
            this.fim = null;
        }

        public boolean estaVazia() {
            return this.inicio == null;
        }

        public void enfileirar(int valor) {
            No novo = new No(valor);
            if (estaVazia()) {
                this.inicio = novo;
                this.fim = novo;
            } else {
                this.fim.proximo = novo;
                this.fim = novo;
            }
        }

        public Integer desenfileirar() {
            if (estaVazia()) {
                return null;
            }
            int valor = this.inicio.valor;
            this.inicio = this.inicio.proximo;
            if (this.inicio == null) {
                this.fim = null;
            }
            return valor;
        }

        public Integer primeiro() {
            if (estaVazia()) {
                return null;
            }
            return this.inicio.valor;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            sc.close();
            return;
        }

        int n = sc.nextInt();
        Fila fila = new Fila();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = sc.next();

            if (comando.equals("CHEGAR")) {
                int x = sc.nextInt();
                fila.enfileirar(x);
            } else if (comando.equals("ATENDER")) {
                Integer senha = fila.desenfileirar();
                saida.append(senha == null ? "VAZIA" : senha).append("\n");
            } else if (comando.equals("PROXIMO")) {
                Integer senha = fila.primeiro();
                saida.append(senha == null ? "VAZIA" : senha).append("\n");
            }
        }

        System.out.print(saida.toString());
        sc.close();
    }
}
```

---

## 3. Explicação Detalhada do Código

1. **Classe `No`:** Representa cada paciente na fila. Guarda o número inteiro da senha (`valor`) e uma referência (`proximo`) para o nó seguinte na sequência.
2. **Classe `Fila`:**
   * `estaVazia()`: verifica se `inicio == null`.
   * `enfileirar(int valor)`: cria um novo `No`. Se a fila estiver vazia, tanto `inicio` quanto `fim` passam a apontar para esse novo nó. Caso contrário, o antigo último nó aponta para o novo, e o ponteiro `fim` é atualizado.
   * `desenfileirar()`: retorna `null` se a fila estiver vazia. Caso contrário, salva a senha de `inicio`, avança `inicio` para `inicio.proximo` e, se a fila ficar vazia após a remoção (`inicio == null`), ajusta `fim` para `null` para evitar referências antigas na memória.
   * `primeiro()`: consulta `inicio.valor` sem alterar os ponteiros.
3. **Método `main`:**
   * Lê a quantidade $N$ de operações via `Scanner`.
   * Utiliza `StringBuilder` para acumular as respostas dos comandos `ATENDER` e `PROXIMO` em memória e imprimi-las de uma única vez ao final, garantindo melhor desempenho de entrada/saída.

---

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo
Salve o código em um arquivo chamado **`Main.java`** dentro da pasta da questão.

### Passo 2: Compilar e executar no terminal
Abra o terminal na pasta onde o arquivo `Main.java` está salvo e execute:

```powershell
javac Main.java
java Main
```

### Passo 3: Validar a saída
1. Com o terminal aguardando a entrada (cursor piscando), cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.
2. Compare as linhas exibidas no terminal com o bloco de **Saída Esperada**.
3. *(Opcional)* Para testar usando um arquivo `entrada.txt` no PowerShell:
   ```powershell
   Get-Content entrada.txt | java Main
   ```

---

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada
```text
8
CHEGAR 101
CHEGAR 102
PROXIMO
ATENDER
PROXIMO
ATENDER
ATENDER
PROXIMO
```

### Saída Esperada
```text
101
101
102
102
VAZIA
VAZIA
```