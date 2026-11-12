# Questão 8 — Diário de Rotações

## 1. Metodologia e Estrutura de Dados Escolhida

Quando inserimos até $N = 100.000$ elementos em ordem crescente ou decrescente em uma árvore binária de busca comum, ela degenera em uma lista linear de altura $O(N)$, tornando as operações lentas. Para garantir desempenho logarítmico mesmo nas piores sequências de entrada, utilizamos uma **Árvore AVL** (árvore binária de busca autobalanceada).

### Estrutura Adotada: Árvore AVL com Nós e Referências

A estrutura foi implementada manualmente com objetos `No`, onde cada nó armazena:

* **`valor`:** o inteiro armazenado no nó.

* **`altura`:** a altura da subárvore enraizada naquele nó (folhas têm altura $0$ e subárvores nulas têm altura $-1$). Armazenar a altura diretamente no nó permite consultar o fator de balanceamento em $O(1)$.

* **`esquerda` e `direita`:** referências para os filhos à esquerda e à direita.

**Detecção e Correção de Desequilíbrios:**
Após cada inserção recursiva, no retorno da pilha de chamadas, atualizamos a altura do nó ancestral e calculamos seu **fator de balanceamento**:

$$FB(\text{nó}) = \text{altura}(\text{esquerda}) - \text{altura}(\text{direita})$$

Se $|FB| > 1$, identificamos o caso de desequilíbrio e aplicamos a rotação correspondente:

1. **`ESQUERDA-ESQUERDA`** ($FB > 1$ e inserção na subárvore esquerda do filho esquerdo): **rotação simples à direita**.

2. **`DIREITA-DIREITA`** ($FB < -1$ e inserção na subárvore direita do filho direito): **rotação simples à esquerda**.

3. **`ESQUERDA-DIREITA`** ($FB > 1$ e inserção na subárvore direita do filho esquerdo): **rotação dupla** (rotação à esquerda no filho esquerdo seguida de rotação à direita no nó desbalanceado).

4. **`DIREITA-ESQUERDA`** ($FB < -1$ e inserção na subárvore esquerda do filho direito): **rotação dupla** (rotação à direita no filho direito seguida de rotação à esquerda no nó desbalanceado).

Caso o valor já exista na árvore, a inserção é abortada e reporta-se `REPETIDO`. Se nenhum nó apresentar $|FB| > 1$, reporta-se `SEM ROTACAO`.

### Análise de Complexidade

Como a propriedade AVL garante que a altura $h$ da árvore é sempre $O(\log N)$:

* **Inserir (`INSERIR X`):** $O(\log N)$ no pior caso — desce até a folha em $O(\log N)$ e atualiza alturas/executa no máximo uma rotação simples ou dupla em $O(1)$ no retorno.

* **Consultar Altura (`ALTURA`):** $O(1)$ — leitura direta do atributo `altura` armazenado na raiz (ou $-1$ se a raiz for nula).

* **Percurso Pré-Ordem (`PREORDEM`):** $O(N)$ — visita cada nó da árvore exatamente uma vez.

* **Complexidade de Espaço:** $O(N)$ para armazenar os nós e $O(\log N)$ na pilha de recursão.

## 2. Código-Fonte (`Main.java`)

```java
// Estrutura de dados escolhida: Árvore AVL (implementada manualmente com nós e referências).
// Complexidade das operações principais:
// - Inserir e rebalancear (INSERIR X): O(log N) no pior caso
// - Consultar altura da árvore (ALTURA): O(1)
// - Percurso pré-ordem (PREORDEM): O(N)
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

    static class No {
        int valor;
        int altura;
        No esquerda;
        No direita;

        No(int valor) {
            this.valor = valor;
            this.altura = 0;
            this.esquerda = null;
            this.direita = null;
        }
    }

    static class ArvoreAVL {
        private No raiz;
        private String statusUltimaInsercao;

        public ArvoreAVL() {
            this.raiz = null;
        }

        private int alturaNo(No no) {
            return (no == null) ? -1 : no.altura;
        }

        private void atualizarAltura(No no) {
            int altEsq = alturaNo(no.esquerda);
            int altDir = alturaNo(no.direita);
            no.altura = 1 + (altEsq > altDir ? altEsq : altDir);
        }

        private int fatorBalanceamento(No no) {
            if (no == null) {
                return 0;
            }
            return alturaNo(no.esquerda) - alturaNo(no.direita);
        }

        private No rotacaoDireita(No y) {
            No x = y.esquerda;
            No t2 = x.direita;

            x.direita = y;
            y.esquerda = t2;

            atualizarAltura(y);
            atualizarAltura(x);

            return x;
        }

        private No rotacaoEsquerda(No x) {
            No y = x.direita;
            No t2 = y.esquerda;

            y.esquerda = x;
            x.direita = t2;

            atualizarAltura(x);
            atualizarAltura(y);

            return y;
        }

        public String inserir(int valor) {
            this.statusUltimaInsercao = "SEM ROTACAO";
            this.raiz = inserirRecursivo(this.raiz, valor);
            return this.statusUltimaInsercao;
        }

        private No inserirRecursivo(No no, int valor) {
            if (no == null) {
                return new No(valor);
            }

            if (valor == no.valor) {
                this.statusUltimaInsercao = "REPETIDO";
                return no;
            } else if (valor < no.valor) {
                no.esquerda = inserirRecursivo(no.esquerda, valor);
            } else {
                no.direita = inserirRecursivo(no.direita, valor);
            }

            atualizarAltura(no);
            int fb = fatorBalanceamento(no);

            // Caso 1: Desequilíbrio à esquerda (fb > 1)
            if (fb > 1) {
                if (valor < no.esquerda.valor) {
                    this.statusUltimaInsercao = "ESQUERDA-ESQUERDA";
                    return rotacaoDireita(no);
                } else {
                    this.statusUltimaInsercao = "ESQUERDA-DIREITA";
                    no.esquerda = rotacaoEsquerda(no.esquerda);
                    return rotacaoDireita(no);
                }
            }

            // Caso 2: Desequilíbrio à direita (fb < -1)
            if (fb < -1) {
                if (valor > no.direita.valor) {
                    this.statusUltimaInsercao = "DIREITA-DIREITA";
                    return rotacaoEsquerda(no);
                } else {
                    this.statusUltimaInsercao = "DIREITA-ESQUERDA";
                    no.direita = rotacaoDireita(no.direita);
                    return rotacaoEsquerda(no);
                }
            }

            return no;
        }

        public String preOrdem() {
            if (this.raiz == null) {
                return "VAZIA";
            }
            StringBuilder sb = new StringBuilder();
            percorrerPreOrdem(this.raiz, sb);
            return sb.toString();
        }

        private void percorrerPreOrdem(No no, StringBuilder sb) {
            if (no == null) {
                return;
            }
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(no.valor);
            percorrerPreOrdem(no.esquerda, sb);
            percorrerPreOrdem(no.direita, sb);
        }

        public int altura() {
            return alturaNo(this.raiz);
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        ArvoreAVL avl = new ArvoreAVL();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = in.next();

            if (comando.equals("INSERIR")) {
                int x = in.nextInt();
                saida.append(avl.inserir(x)).append("\n");
            } else if (comando.equals("PREORDEM")) {
                saida.append(avl.preOrdem()).append("\n");
            } else if (comando.equals("ALTURA")) {
                saida.append(avl.altura()).append("\n");
            }
        }

        System.out.print(saida.toString());
    }
}
```

## 3. Explicação Detalhada do Código

1. **Classe `No`:** Representa cada nó da árvore AVL, mantendo o campo `altura` inicializado em `0` (altura de um nó folha recém-criado).

2. **Métodos Auxiliares de Altura e Balanceamento:**

   * `alturaNo(No no)`: retorna `-1` para referências nulas e `no.altura` caso contrário.

   * `atualizarAltura(No no)`: recalcula a altura do nó com base no maior valor entre seus dois filhos.

   * `fatorBalanceamento(No no)`: calcula a diferença `alturaNo(no.esquerda) - alturaNo(no.direita)`.

3. **Rotações (`rotacaoDireita` e `rotacaoEsquerda`):**

   * Realizam a troca de referências entre o nó desbalanceado e seu filho, atualizando imediatamente a altura do antigo nó raiz da subárvore primeiro e, em seguida, da nova raiz da subárvore.

4. **Método `inserir(int valor)`:**

   * Inicializa `statusUltimaInsercao` como `"SEM ROTACAO"`.

   * Se durante a descida encontrar `valor == no.valor`, marca `"REPETIDO"` e retorna sem modificar a árvore.

   * Ao retornar da recursão, detecta se `fb > 1` ou `fb < -1`, registra o nome exato do caso corrigido (`ESQUERDA-ESQUERDA`, `ESQUERDA-DIREITA`, `DIREITA-DIREITA` ou `DIREITA-ESQUERDA`) e executa a rotação simples ou dupla.

5. **Métodos `preOrdem()` e `altura()`:**

   * `preOrdem()` retorna `"VAZIA"` quando `raiz == null` ou percorre na ordem Raiz–Esquerda–Direita.

   * `altura()` retorna diretamente `alturaNo(this.raiz)` em $O(1)$.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo

Salve o código em um arquivo chamado **`Main.java`** dentro da pasta `Questao8` (sem declaração `package` na primeira linha).

### Passo 2: Compilar e executar no terminal

Abra o terminal na pasta `Questao8` e execute:

```bash
javac Main.java
java Main
```

### Passo 3: Validar a saída

1. Com o terminal aguardando a entrada, cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.

2. Verifique se as dez linhas exibidas correspondem exatamente ao bloco de **Saída Esperada**.

3. *(Opcional)* Para validar usando um arquivo `entrada.txt` no PowerShell:

   ```powershell
   Get-Content entrada.txt | java Main
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```text
10
INSERIR 10
INSERIR 20
INSERIR 30
INSERIR 25
INSERIR 28
INSERIR 5
INSERIR 7
INSERIR 20
PREORDEM
ALTURA
```

### Saída Esperada

```text
SEM ROTACAO
SEM ROTACAO
DIREITA-DIREITA
SEM ROTACAO
ESQUERDA-DIREITA
SEM ROTACAO
ESQUERDA-DIREITA
REPETIDO
20 7 5 10 28 25 30
2
```