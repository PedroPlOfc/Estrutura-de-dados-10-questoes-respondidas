# Questão 3 — Sequência de Robôs

## 1. Metodologia e Estrutura de Dados Escolhida

O problema exige inserções, remoções e consultas em posições arbitrárias (indexadas a partir de $1$) de uma sequência dinâmica de robôs, além da validação imediata de limites e exibição completa da lista.

### Estrutura Adotada: Lista Simplesmente Encadeada

A solução foi construída manualmente com **nós e referências** (sem uso de bibliotecas prontas do Java), mantendo dois atributos de controle na lista:

* **`inicio`:** referência para o primeiro nó da sequência (posição $1$).
* **`tamanho`:** inteiro que contabiliza a quantidade atual de robôs na sequência.

**Por que essa abordagem?**
Ao contrário de um vetor estático — que exigiria deslocar todos os elementos subsequentes na memória a cada inserção ou remoção intermediária —, a lista encadeada realiza essas alterações apenas redirecionando os ponteiros (`proximo`) dos nós vizinhos. Além disso, manter o contador `tamanho` atualizado permite validar se uma posição $P$ é válida em tempo constante antes mesmo de percorrer a lista.

### Análise de Complexidade

* **Validação de posição ($P$):** $O(1)$ — verificação direta usando o atributo `tamanho`.
* **Inserir (`I P X`):** $O(1)$ na primeira posição ($P = 1$) e $O(P)$ (no pior caso $O(N)$, onde $N$ é o tamanho atual da lista) para alcançar o nó anterior à posição $P$.
* **Remover (`R P`):** $O(1)$ na primeira posição ($P = 1$) e $O(P)$ (no pior caso $O(N)$) nas demais posições.
* **Consultar (`C P`):** $O(P)$ (no pior caso $O(N)$) para percorrer a cadeia de referências até o $P$-ésimo nó.
* **Listar (`L`):** $O(N)$ — percorre todos os nós do início ao fim para montar a sequência.
* **Complexidade de Espaço:** $O(N)$, proporcional ao número de robôs armazenados na lista.

## 2. Código-Fonte (`Main.java`)

```java
// Estrutura de dados escolhida: Lista simplesmente encadeada (implementada com nós e referências).
// Complexidade das operações principais:
// - Inserir (I P X): O(1) no início (P = 1) e O(P) / O(N) nas demais posições
// - Remover (R P): O(1) no início (P = 1) e O(P) / O(N) nas demais posições
// - Consultar (C P): O(P) / O(N) no pior caso
// - Listar (L): O(N), onde N é o tamanho atual da lista

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

    static class ListaEncadeada {
        private No inicio;
        private int tamanho;

        public ListaEncadeada() {
            this.inicio = null;
            this.tamanho = 0;
        }

        public boolean estaVazia() {
            return this.tamanho == 0;
        }

        public boolean inserir(int pos, int valor) {
            if (pos < 1 || pos > this.tamanho + 1) {
                return false;
            }

            No novo = new No(valor);
            if (pos == 1) {
                novo.proximo = this.inicio;
                this.inicio = novo;
            } else {
                No atual = this.inicio;
                for (int i = 1; i < pos - 1; i++) {
                    atual = atual.proximo;
                }
                novo.proximo = atual.proximo;
                atual.proximo = novo;
            }

            this.tamanho++;
            return true;
        }

        public Integer remover(int pos) {
            if (pos < 1 || pos > this.tamanho) {
                return null;
            }

            int removido;
            if (pos == 1) {
                removido = this.inicio.valor;
                this.inicio = this.inicio.proximo;
            } else {
                No atual = this.inicio;
                for (int i = 1; i < pos - 1; i++) {
                    atual = atual.proximo;
                }
                removido = atual.proximo.valor;
                atual.proximo = atual.proximo.proximo;
            }

            this.tamanho--;
            return removido;
        }

        public Integer consultar(int pos) {
            if (pos < 1 || pos > this.tamanho) {
                return null;
            }

            No atual = this.inicio;
            for (int i = 1; i < pos; i++) {
                atual = atual.proximo;
            }
            return atual.valor;
        }

        public String listar() {
            if (estaVazia()) {
                return "VAZIA";
            }

            StringBuilder sb = new StringBuilder();
            No atual = this.inicio;
            while (atual != null) {
                sb.append(atual.valor);
                if (atual.proximo != null) {
                    sb.append(" ");
                }
                atual = atual.proximo;
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            sc.close();
            return;
        }

        int n = sc.nextInt();
        ListaEncadeada lista = new ListaEncadeada();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = sc.next();

            if (comando.equals("I")) {
                int p = sc.nextInt();
                int x = sc.nextInt();
                if (!lista.inserir(p, x)) {
                    saida.append("ERRO\n");
                }
            } else if (comando.equals("R")) {
                int p = sc.nextInt();
                Integer removido = lista.remover(p);
                saida.append(removido == null ? "ERRO" : removido).append("\n");
            } else if (comando.equals("C")) {
                int p = sc.nextInt();
                Integer consultado = lista.consultar(p);
                saida.append(consultado == null ? "ERRO" : consultado).append("\n");
            } else if (comando.equals("L")) {
                saida.append(lista.listar()).append("\n");
            }
        }

        System.out.print(saida.toString());
        sc.close();
    }
}
```

## 3. Explicação Detalhada do Código

1. **Classe `No`:** Representa cada robô na sequência, armazenando seu código numérico (`valor`) e a referência (`proximo`) para o robô seguinte.
2. **Classe `ListaEncadeada`:**
   * `inserir(int pos, int valor)`: valida se $1 \le \text{pos} \le \text{tamanho} + 1$. Se for inválido, retorna `false` sem alterar a lista. Caso contrário, cria o novo nó: se $\text{pos} = 1$, encaixa-o no início; se $\text{pos} > 1$, avança até a posição $\text{pos} - 1$ e ajusta as referências para encaixar o novo nó entre os vizinhos, incrementando `tamanho`.
   * `remover(int pos)`: valida se $1 \le \text{pos} \le \text{tamanho}$. Se inválido, retorna `null`. Se $\text{pos} = 1$, avança `inicio` para o segundo nó; caso contrário, avança até $\text{pos} - 1$ e pula o nó da posição `pos` (`atual.proximo = atual.proximo.proximo`), decrementando `tamanho` e retornando o código removido.
   * `consultar(int pos)`: valida os limites e avança `pos - 1` passos a partir de `inicio`, retornando o valor encontrado sem modificar a lista.
   * `listar()`: verifica se `tamanho == 0` para retornar `"VAZIA"`. Caso contrário, percorre todos os nós concatenando os códigos separados por um espaço simples (sem espaço extra ao final).
3. **Método `main`:**
   * Lê a quantidade $N$ de comandos e interpreta cada operação (`I`, `R`, `C`, `L`).
   * Sempre que uma operação retorna `false` (no caso de `I`) ou `null` (nos casos de `R` e `C`), registra `"ERRO"` no `StringBuilder` de saída.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo

Salve o código em um arquivo chamado **`Main.java`** dentro da pasta `Questao3`

### Passo 2: Compilar e executar no terminal

Abra o terminal na pasta `Questao3` e execute:

```powershell
javac Main.java
java Main
```

### Passo 3: Validar a saída

1. Com o terminal aguardando a entrada (cursor piscando), cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.
2. Verifique se as cinco linhas impressas correspondem exatamente ao bloco de **Saída Esperada**.
3. *(Opcional)* Para validar usando um arquivo `entrada.txt` no PowerShell:

   ```powershell
   Get-Content entrada.txt | java Main
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```text
9
I 1 10
I 2 30
I 2 20
L
R 2
C 2
I 1 5
R 4
L
```

### Saída Esperada

```text
10 20 30
20
30
ERRO
5 10 30
```