# Questão 7 — Catálogo de Livros

## 1. Metodologia e Estrutura de Dados Escolhida

Para armazenar os códigos inteiros dos livros mantendo uma ordenação dinâmica que permita buscas direcionadas, percursos estruturados e remoções sem deslocamento de blocos de memória, a estrutura adequada é uma **Árvore Binária de Busca (ABB)**.

### Estrutura Adotada: Árvore Binária de Busca com Nós e Referências

A árvore foi construída manualmente utilizando objetos `No`, onde cada nó armazena:

* **`valor`:** o código inteiro do livro.
* **`esquerda`:** referência para o filho à esquerda (subárvore com códigos estritamente menores que `valor`).
* **`direita`:** referência para o filho à direita (subárvore com códigos estritamente maiores que `valor`).

**Regras Específicas Aplicadas:**
1. **Chaves repetidas:** durante a inserção (`INSERIR X`), caso o código já exista na árvore, a operação encerra imediatamente sem criar nós duplicados.
2. **Remoção com dois filhos (`REMOVER X`):** utiliza o **sucessor em-ordem**, localizado buscando o nó mais à esquerda da subárvore direita do nó removido. Seu valor substitui o valor do nó removido e o nó sucessor original (que possui no máximo um filho à direita) é desconectado.
3. **Cálculo da altura (`ALTURA`):** definido recursivamente como $1 + \max(\text{altura}(\text{esquerda}), \text{altura}(\text{direita}))$, atribuindo $-1$ para subárvore nula (vazia) e resultando em $0$ para nós folha.

### Análise de Complexidade

Seja $h$ a altura atual da árvore e $N$ a quantidade de nós armazenados:

* **Inserir (`INSERIR X`):** $O(h)$ — sendo $O(\log N)$ em árvores balanceadas e $O(N)$ no pior caso (árvore degenerada em linha).
* **Buscar (`BUSCAR X`):** $O(h)$ — sendo $O(\log N)$ no caso médio e $O(N)$ no pior caso.
* **Remover (`REMOVER X`):** $O(h)$ — localiza o nó e, se necessário, seu sucessor em $O(h)$.
* **Percursos (`EMORDEM` e `PREORDEM`):** $O(N)$ — visita cada nó da árvore exatamente uma vez.
* **Calcular Altura (`ALTURA`):** $O(N)$ — percorre todos os nós para determinar o maior caminho até uma folha.
* **Complexidade de Espaço:** $O(N)$ para armazenar os nós da árvore.

## 2. Código-Fonte (`Main.java`)

```java
// Estrutura de dados escolhida: Árvore Binária de Busca (implementada manualmente com nós e referências).
// Complexidade das operações principais (onde h é a altura da árvore e N é o total de nós):
// - Inserir (INSERIR X): O(h) -> O(log N) médio, O(N) pior caso
// - Buscar (BUSCAR X): O(h) -> O(log N) médio, O(N) pior caso
// - Remover (REMOVER X): O(h) -> O(log N) médio, O(N) pior caso
// - Percursos (EMORDEM / PREORDEM) e Altura (ALTURA): O(N)

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
        No esquerda;
        No direita;

        No(int valor) {
            this.valor = valor;
            this.esquerda = null;
            this.direita = null;
        }
    }

    static class ArvoreBinariaBusca {
        private No raiz;

        public ArvoreBinariaBusca() {
            this.raiz = null;
        }

        public void inserir(int valor) {
            if (this.raiz == null) {
                this.raiz = new No(valor);
                return;
            }

            No atual = this.raiz;
            while (true) {
                if (valor == atual.valor) {
                    // Códigos repetidos são ignorados
                    return;
                } else if (valor < atual.valor) {
                    if (atual.esquerda == null) {
                        atual.esquerda = new No(valor);
                        return;
                    }
                    atual = atual.esquerda;
                } else {
                    if (atual.direita == null) {
                        atual.direita = new No(valor);
                        return;
                    }
                    atual = atual.direita;
                }
            }
        }

        public String buscar(int valor) {
            if (this.raiz == null) {
                return "NAO";
            }

            StringBuilder caminho = new StringBuilder();
            No atual = this.raiz;

            while (atual != null) {
                caminho.append(atual.valor).append(" ");
                if (valor == atual.valor) {
                    caminho.append("SIM");
                    return caminho.toString();
                } else if (valor < atual.valor) {
                    atual = atual.esquerda;
                } else {
                    atual = atual.direita;
                }
            }

            caminho.append("NAO");
            return caminho.toString();
        }

        public boolean remover(int valor) {
            No pai = null;
            No atual = this.raiz;

            while (atual != null && atual.valor != valor) {
                pai = atual;
                if (valor < atual.valor) {
                    atual = atual.esquerda;
                } else {
                    atual = atual.direita;
                }
            }

            // Código não encontrado na árvore
            if (atual == null) {
                return false;
            }

            // Caso 1 e Caso 2: nó com no máximo um filho
            if (atual.esquerda == null || atual.direita == null) {
                No filho = (atual.esquerda != null) ? atual.esquerda : atual.direita;

                if (pai == null) {
                    this.raiz = filho;
                } else if (pai.esquerda == atual) {
                    pai.esquerda = filho;
                } else {
                    pai.direita = filho;
                }
            } else {
                // Caso 3: nó com dois filhos -> substitui pelo sucessor (menor da subárvore direita)
                No paiSucessor = atual;
                No sucessor = atual.direita;

                while (sucessor.esquerda != null) {
                    paiSucessor = sucessor;
                    sucessor = sucessor.esquerda;
                }

                atual.valor = sucessor.valor;

                if (paiSucessor == atual) {
                    paiSucessor.direita = sucessor.direita;
                } else {
                    paiSucessor.esquerda = sucessor.direita;
                }
            }

            return true;
        }

        public String emOrdem() {
            if (this.raiz == null) {
                return "VAZIA";
            }
            StringBuilder sb = new StringBuilder();
            percorrerEmOrdem(this.raiz, sb);
            return sb.toString();
        }

        private void percorrerEmOrdem(No no, StringBuilder sb) {
            if (no == null) {
                return;
            }
            percorrerEmOrdem(no.esquerda, sb);
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(no.valor);
            percorrerEmOrdem(no.direita, sb);
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
            return calcularAltura(this.raiz);
        }

        private int calcularAltura(No no) {
            if (no == null) {
                return -1;
            }
            int altEsq = calcularAltura(no.esquerda);
            int altDir = calcularAltura(no.direita);
            return 1 + (altEsq > altDir ? altEsq : altDir);
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        ArvoreBinariaBusca abb = new ArvoreBinariaBusca();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = in.next();

            if (comando.equals("INSERIR")) {
                int x = in.nextInt();
                abb.inserir(x);
            } else if (comando.equals("BUSCAR")) {
                int x = in.nextInt();
                saida.append(abb.buscar(x)).append("\n");
            } else if (comando.equals("REMOVER")) {
                int x = in.nextInt();
                if (!abb.remover(x)) {
                    saida.append("ERRO\n");
                }
            } else if (comando.equals("EMORDEM")) {
                saida.append(abb.emOrdem()).append("\n");
            } else if (comando.equals("PREORDEM")) {
                saida.append(abb.preOrdem()).append("\n");
            } else if (comando.equals("ALTURA")) {
                saida.append(abb.altura()).append("\n");
            }
        }

        System.out.print(saida.toString());
    }
}
```

## 3. Explicação Detalhada do Código

1. **Classe `No`:** Representa cada registro de livro na árvore, contendo o código inteiro (`valor`) e os ponteiros `esquerda` e `direita`.
2. **Classe `ArvoreBinariaBusca`:**
   * `inserir(int valor)`: desce pela árvore comparando o código novo com o nó atual; valores menores seguem à esquerda, maiores seguem à direita e valores iguais são descartados sem alterar a estrutura.
   * `buscar(int valor)`: se a árvore estiver vazia, retorna imediatamente `"NAO"`. Caso contrário, registra cada nó visitado a partir da `raiz` e anexa `"SIM"` ao encontrar o código ou `"NAO"` ao atingir uma referência nula.
   * `remover(int valor)`: localiza o nó alvo e seu pai. Se o nó tiver zero ou um filho, conecta o pai diretamente ao único filho (ou `null`). Se tiver dois filhos, encontra o menor nó da subárvore direita (`sucessor`), copia seu valor para o nó alvo e remove o nó sucessor original. Retorna `false` caso o valor não exista, sinalizando ao `main` para imprimir `"ERRO"`.
   * `emOrdem()` e `preOrdem()`: verificam se a árvore está vazia (retornando `"VAZIA"`) ou realizam o caminhamento recursivo correspondente (Esquerda–Raiz–Direita para `EMORDEM`; Raiz–Esquerda–Direita para `PREORDEM`).
   * `altura()`: retorna `-1` para árvore vazia e calcula recursivamente a maior profundidade entre as subárvores esquerda e direita.
3. **Método `main`:** Lê os $N$ comandos via `LeitorRapido` e acumula todas as respostas exigidas em um `StringBuilder`.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo

Salve o código em um arquivo chamado **`Main.java`** dentro da pasta `Questao7` (certifique-se de que não há declaração `package` na primeira linha).

### Passo 2: Compilar e executar no terminal

Abra o terminal na pasta `Questao7` e execute:

```powershell
javac Main.java
java Main
```

### Passo 3: Validar a saída

1. Com o terminal aguardando a entrada, cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.
2. Verifique se as cinco linhas exibidas correspondem exatamente ao bloco de **Saída Esperada**.
3. *(Opcional)* Para validar usando um arquivo `entrada.txt` no PowerShell:

   ```powershell
   Get-Content entrada.txt | java Main
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```text
13
INSERIR 50
INSERIR 30
INSERIR 70
INSERIR 20
INSERIR 40
INSERIR 60
INSERIR 35
BUSCAR 35
BUSCAR 65
REMOVER 30
REMOVER 90
PREORDEM
ALTURA
```

### Saída Esperada

```text
50 30 40 35 SIM
50 70 60 NAO
ERRO
50 35 20 40 70 60
2
```