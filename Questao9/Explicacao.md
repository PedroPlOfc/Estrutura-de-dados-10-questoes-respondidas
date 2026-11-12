# Questão 9 — Triagem no Pronto-Socorro

## 1. Metodologia e Estrutura de Dados Escolhida

No atendimento de emergência descrito, os pacientes precisam ser atendidos sempre pela maior gravidade (prioridade de $1$ a $5$), mas com constantes chegadas intercaladas com atendimentos. Ordenar a lista a cada chegada ou percorrer todos os pacientes a cada atendimento custaria $O(N)$ por operação, o que seria ineficiente para até $N = 100.000$ comandos.

### Estrutura Adotada: Fila de Prioridade com Heap Máximo em Vetor

A solução foi implementada manualmente utilizando um **Heap Máximo (Max-Heap) armazenado em vetor** (`Paciente[] heap`), sem uso de `PriorityQueue` ou outras coleções prontas do Java.

**Por que essa abordagem?**
Uma árvore binária quase completa pode ser mapeada diretamente em um vetor primitivo indexado a partir de $0$, eliminando a necessidade de ponteiros para pai e filhos:

* **Pai de um nó no índice $i$:** $\lfloor (i - 1) / 2 \rfloor$
* **Filho à esquerda do índice $i$:** $2i + 1$
* **Filho à direita do índice $i$:** $2i + 2$

**Critério de Comparação e Desempate:**
Cada paciente armazena seu `nome`, sua `prioridade` e um contador sequencial `ordemChegada` ($1, 2, 3, \dots$). Um paciente $A$ tem precedência sobre um paciente $B$ se:

1. $A.\text{prioridade} > B.\text{prioridade}$; ou
2. $A.\text{prioridade} == B.\text{prioridade}$ e $A.\text{ordemChegada} < B.\text{ordemChegada}$ (quem chegou antes sai primeiro).

### Análise de Complexidade

* **Inserir paciente (`CHEGAR NOME P`):** $O(\log N)$ — o novo paciente é inserido na última posição livre do vetor e sobe pela árvore (`subir` / *heapify-up*) trocando de lugar com seu pai até restaurar a propriedade do heap.
* **Atender próximo (`ATENDER`):** $O(\log N)$ — a raiz (posição $0$) é removida, o último elemento do vetor assume a raiz e desce pela árvore (`descer` / *heapify-down*) trocando de lugar com o maior de seus filhos até se estabilizar.
* **Consultar próximo (`PROXIMO`):** $O(1)$ — acesso direto à raiz do heap na posição `heap[0]`.
* **Complexidade de Espaço:** $O(N)$ para armazenar até $N$ registros no vetor do heap.

## 2. Código-Fonte (`Main.java`)

```java
// Estrutura de dados escolhida: Fila de prioridade com Heap Máximo implementado em vetor (Paciente[]).
// Complexidade das operações principais:
// - Inserir paciente (CHEGAR NOME P): O(log N)
// - Atender paciente mais prioritário (ATENDER): O(log N)
// - Consultar próximo paciente (PROXIMO): O(1)
// - Complexidade de espaço: O(N)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static class LeitorRapido {
        BufferedReader br;
        StringTokenizer st;

        public LeitorRapido() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (st == null || !st.hasMoreElements()) {
                String linha = br.readLine();
                if (linha == null) {
                    return null;
                }
                st = new StringTokenizer(linha);
            }
            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    static class Paciente {
        String nome;
        int prioridade;
        int ordemChegada;

        Paciente(String nome, int prioridade, int ordemChegada) {
            this.nome = nome;
            this.prioridade = prioridade;
            this.ordemChegada = ordemChegada;
        }
    }

    static class HeapMaximo {
        private Paciente[] dados;
        private int tamanho;

        public HeapMaximo(int capacidadeMaxima) {
            this.dados = new Paciente[capacidadeMaxima];
            this.tamanho = 0;
        }

        public boolean estaVazio() {
            return this.tamanho == 0;
        }

        // Retorna true se o paciente 'a' deve ser atendido antes do paciente 'b'
        private boolean vemAntes(Paciente a, Paciente b) {
            if (a.prioridade != b.prioridade) {
                return a.prioridade > b.prioridade;
            }
            return a.ordemChegada < b.ordemChegada;
        }

        private void trocar(int i, int j) {
            Paciente temp = this.dados[i];
            this.dados[i] = this.dados[j];
            this.dados[j] = temp;
        }

        private void subir(int indice) {
            while (indice > 0) {
                int pai = (indice - 1) / 2;
                if (vemAntes(this.dados[indice], this.dados[pai])) {
                    trocar(indice, pai);
                    indice = pai;
                } else {
                    break;
                }
            }
        }

        private void descer(int indice) {
            while (true) {
                int esquerda = 2 * indice + 1;
                int direita = 2 * indice + 2;
                int maior = indice;

                if (esquerda < this.tamanho && vemAntes(this.dados[esquerda], this.dados[maior])) {
                    maior = esquerda;
                }
                if (direita < this.tamanho && vemAntes(this.dados[direita], this.dados[maior])) {
                    maior = direita;
                }

                if (maior != indice) {
                    trocar(indice, maior);
                    indice = maior;
                } else {
                    break;
                }
            }
        }

        public void inserir(Paciente paciente) {
            this.dados[this.tamanho] = paciente;
            subir(this.tamanho);
            this.tamanho++;
        }

        public Paciente removerMaximo() {
            if (estaVazio()) {
                return null;
            }
            Paciente maximo = this.dados[0];
            this.tamanho--;
            if (this.tamanho > 0) {
                this.dados[0] = this.dados[this.tamanho];
                this.dados[this.tamanho] = null;
                descer(0);
            } else {
                this.dados[0] = null;
            }
            return maximo;
        }

        public Paciente consultarMaximo() {
            if (estaVazio()) {
                return null;
            }
            return this.dados[0];
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        HeapMaximo filaPrioridade = new HeapMaximo(n);
        int contadorChegada = 0;
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = in.next();

            if (comando.equals("CHEGAR")) {
                String nome = in.next();
                int prioridade = in.nextInt();
                contadorChegada++;
                filaPrioridade.inserir(new Paciente(nome, prioridade, contadorChegada));
            } else if (comando.equals("ATENDER")) {
                Paciente p = filaPrioridade.removerMaximo();
                if (p == null) {
                    saida.append("VAZIA\n");
                } else {
                    saida.append(p.nome).append(" ").append(p.prioridade).append("\n");
                }
            } else if (comando.equals("PROXIMO")) {
                Paciente p = filaPrioridade.consultarMaximo();
                if (p == null) {
                    saida.append("VAZIA\n");
                } else {
                    saida.append(p.nome).append(" ").append(p.prioridade).append("\n");
                }
            }
        }

        System.out.print(saida.toString());
    }
}
```

## 3. Explicação Detalhada do Código

1. **Classe `Paciente`:** Encapsula o `nome`, a `prioridade` ($1$ a $5$) e o número sequencial `ordemChegada`, garantindo que o desempate respeite estritamente a ordem de entrada no pronto-socorro.
2. **Classe `HeapMaximo`:**
   * `vemAntes(Paciente a, Paciente b)`: define a relação de ordem total do heap, priorizando o maior valor de `prioridade` e desempatando pelo menor valor de `ordemChegada`.
   * `subir(int indice)`: ajusta a posição de um elemento recém-inserido no final do vetor comparando-o sucessivamente com seu pai `(indice - 1) / 2`.
   * `descer(int indice)`: reorganiza o heap após uma remoção comparando o nó atual com seus filhos `2 * indice + 1` e `2 * indice + 2`, promovendo o filho mais prioritário.
   * `inserir`, `removerMaximo` e `consultarMaximo`: implementam as operações principais da fila de prioridade retornando `null` quando `tamanho == 0`.
3. **Método `main`:**
   * Aloca o heap com capacidade máxima $N$ (o maior número possível de pacientes em um teste de $N$ comandos) e mantém `contadorChegada` incrementado a cada comando `CHEGAR`.
   * Imprime `<NOME> <PRIORIDADE>` para `ATENDER` e `PROXIMO`, ou `VAZIA` caso o heap esteja sem pacientes.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo

Salve o código em um arquivo chamado **`Main.java`** dentro da pasta `Questao9` (certifique-se de que não há declaração `package` na primeira linha).

### Passo 2: Compilar e executar no terminal

Abra o terminal na pasta `Questao9` e execute:

```powershell
javac Main.java
java Main
```

### Passo 3: Validar a saída

1. Com o terminal aguardando a entrada (cursor piscando), cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.
2. Confirme se as cinco linhas exibidas correspondem exatamente ao bloco de **Saída Esperada** (note que `BRUNO 5` sai antes de `DAVI 5` devido à ordem de chegada).
3. *(Opcional)* Para validar usando um arquivo `entrada.txt` no PowerShell:

   ```powershell
   Get-Content entrada.txt | java Main
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```text
9
CHEGAR ANA 2
CHEGAR BRUNO 5
CHEGAR CAIO 3
PROXIMO
CHEGAR DAVI 5
ATENDER
ATENDER
ATENDER
ATENDER
```

### Saída Esperada

```text
BRUNO 5
BRUNO 5
DAVI 5
CAIO 3
ANA 2
```