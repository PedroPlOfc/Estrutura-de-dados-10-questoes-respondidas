# Questão 10 — Cadastro de Matrículas

## 1. Metodologia e Estrutura de Dados Escolhida

Como os números de matrícula possuem até $10$ dígitos ($1 \le M \le 9.999.999.999$, exigindo o tipo `long`) e o sistema pode processar até $N = 200.000$ operações, não é viável utilizar a matrícula diretamente como índice de um vetor (o que demandaria bilhões de posições de memória), nem realizar buscas lineares em lista (que custariam $O(N)$ por consulta).

### Estrutura Adotada: Tabela Hash com Encadeamento Separado e Redimensionamento Dinâmico

A solução foi implementada manualmente utilizando um **vetor de listas encadeadas** (`No[] tabela`), sem uso de `HashMap`, `HashSet`, `Hashtable` ou outras coleções prontas da biblioteca Java.

**Funcionamento Interno:**

1. **Função de Espalhamento (*Hash*):** Converte a chave `long matricula` em um inteiro de $32$ bits combinando seus bits superiores e inferiores (`(int) (matricula ^ (matricula >>> 32))`), aplica uma mistura adicional de bits para dispersar matrículas sequenciais e mapeia o resultado para o intervalo $[0, \text{capacidade} - 1]$.

2. **Tratamento de Colisões (Encadeamento):** Cada posição (*bucket*) do vetor armazena o início de uma lista simplesmente encadeada de nós (`No`). Quando duas matrículas diferentes caem no mesmo índice, ambas coexistem na lista encadeada daquela posição.

3. **Fator de Carga e Redimensionamento (*Rehashing*):** O fator de carga é definido por $\alpha = \frac{\text{tamanho}}{\text{capacidade}}$. Sempre que uma nova matrícula é cadastrada e $\alpha > 0,75$ (verificado exata e rapidamente via `tamanho * 4 > capacidade * 3`), a tabela dobra sua capacidade (`capacidade * 2`) e redistribui todos os nós existentes nos novos *buckets*.

### Análise de Complexidade

Mantendo o fator de carga limitado a $\alpha \le 0,75$, o comprimento médio das listas encadeadas em cada posição permanece constante:

* **Cadastrar / Atualizar (`CADASTRAR M NOME`):** $O(1)$ amortizado e em média ($O(N)$ no pior caso durante o redimensionamento ou se todas as chaves colidirem no mesmo índice).

* **Buscar (`BUSCAR M`):** $O(1)$ em média ($O(N)$ no pior caso teórico).

* **Remover (`REMOVER M`):** $O(1)$ em média ($O(N)$ no pior caso teórico).

* **Total de Alunos (`TOTAL`):** $O(1)$ — leitura direta do contador `tamanho`.

* **Complexidade de Espaço:** $O(N)$, proporcional ao número de alunos cadastrados.

## 2. Código-Fonte (`Main.java`)

```
// Estrutura de dados escolhida: Tabela Hash com encadeamento separado (vetor de listas
// simplesmente encadeadas com nós e referências) e redimensionamento dinâmico (fator de carga > 0,75).
// Complexidade das operações principais:
// - Cadastrar / Atualizar (CADASTRAR M NOME): O(1) amortizado em média (O(N) no pior caso)
// - Buscar (BUSCAR M): O(1) em média (O(N) no pior caso)
// - Remover (REMOVER M): O(1) em média (O(N) no pior caso)
// - Total de registros (TOTAL): O(1)

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

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }

    static class No {
        long matricula;
        String nome;
        No proximo;

        No(long matricula, String nome) {
            this.matricula = matricula;
            this.nome = nome;
            this.proximo = null;
        }
    }

    static class TabelaHash {
        private No[] tabela;
        private int capacidade;
        private int tamanho;

        public TabelaHash() {
            this.capacidade = 16;
            this.tabela = new No[this.capacidade];
            this.tamanho = 0;
        }

        private int calcularIndice(long matricula, int cap) {
            int h = (int) (matricula ^ (matricula >>> 32));
            h ^= (h >>> 16);
            return (h & 0x7FFFFFFF) % cap;
        }

        private void redimensionar() {
            int novaCapacidade = this.capacidade * 2;
            No[] novaTabela = new No[novaCapacidade];

            for (int i = 0; i < this.capacidade; i++) {
                No atual = this.tabela[i];
                while (atual != null) {
                    No proximo = atual.proximo;
                    int novoIndice = calcularIndice(atual.matricula, novaCapacidade);
                    atual.proximo = novaTabela[novoIndice];
                    novaTabela[novoIndice] = atual;
                    atual = proximo;
                }
            }

            this.tabela = novaTabela;
            this.capacidade = novaCapacidade;
        }

        public String cadastrar(long matricula, String nome) {
            int indice = calcularIndice(matricula, this.capacidade);
            No atual = this.tabela[indice];

            while (atual != null) {
                if (atual.matricula == matricula) {
                    atual.nome = nome;
                    return "ATUALIZADO";
                }
                atual = atual.proximo;
            }

            No novo = new No(matricula, nome);
            novo.proximo = this.tabela[indice];
            this.tabela[indice] = novo;
            this.tamanho++;

            // Dobra a capacidade quando o fator de carga (tamanho / capacidade) ultrapassa 0,75
            if (this.tamanho * 4 > this.capacidade * 3) {
                redimensionar();
            }

            return "CADASTRADO";
        }

        public String buscar(long matricula) {
            int indice = calcularIndice(matricula, this.capacidade);
            No atual = this.tabela[indice];

            while (atual != null) {
                if (atual.matricula == matricula) {
                    return atual.nome;
                }
                atual = atual.proximo;
            }

            return "NAO ENCONTRADO";
        }

        public String remover(long matricula) {
            int indice = calcularIndice(matricula, this.capacidade);
            No anterior = null;
            No atual = this.tabela[indice];

            while (atual != null) {
                if (atual.matricula == matricula) {
                    if (anterior == null) {
                        this.tabela[indice] = atual.proximo;
                    } else {
                        anterior.proximo = atual.proximo;
                    }
                    this.tamanho--;
                    return "REMOVIDO";
                }
                anterior = atual;
                atual = atual.proximo;
            }

            return "NAO ENCONTRADO";
        }

        public int total() {
            return this.tamanho;
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        TabelaHash cadastro = new TabelaHash();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = in.next();

            if (comando.equals("CADASTRAR")) {
                long matricula = in.nextLong();
                String nome = in.next();
                saida.append(cadastro.cadastrar(matricula, nome)).append("\n");
            } else if (comando.equals("BUSCAR")) {
                long matricula = in.nextLong();
                saida.append(cadastro.buscar(matricula)).append("\n");
            } else if (comando.equals("REMOVER")) {
                long matricula = in.nextLong();
                saida.append(cadastro.remover(matricula)).append("\n");
            } else if (comando.equals("TOTAL")) {
                saida.append(cadastro.total()).append("\n");
            }
        }

        System.out.print(saida.toString());
    }
}

```

## 3. Explicação Detalhada do Código

1. **Classe `No`:** Representa cada aluno cadastrado, armazenando `long matricula` (para comportar até $10$ dígitos), `String nome` e o ponteiro `proximo` para o próximo nó em caso de colisão no mesmo *bucket*.

2. **Classe `TabelaHash`:**

   * `calcularIndice(long matricula, int cap)`: aplica *XOR* entre os $32$ bits mais significativos e os $32$ bits menos significativos do `long`, garantindo um índice positivo dentro dos limites do vetor atual.

   * `redimensionar()`: cria um novo vetor com o dobro do tamanho (`capacidade * 2`) e reaproveita os próprios objetos `No` existentes, religando-os nas novas posições sem alocar nós extras.

   * `cadastrar(long matricula, String nome)`: percorre o *bucket* correspondente; se encontrar a matrícula, apenas atualiza `atual.nome` e retorna `"ATUALIZADO"`. Caso contrário, insere o novo nó no início da lista daquele *bucket*, incrementa `tamanho`, verifica se `tamanho * 4 > capacidade * 3` ($\alpha > 0,75$) para redimensionar e retorna `"CADASTRADO"`.

   * `buscar(long matricula)`: localiza o *bucket* pelo hash e percorre sua lista encadeada até encontrar a matrícula, retornando o nome ou `"NAO ENCONTRADO"`.

   * `remover(long matricula)`: localiza o nó da matrícula no *bucket*, ajusta o ponteiro do nó anterior (ou o início do *bucket*) para desconectá-lo, decrementa `tamanho` e retorna `"REMOVIDO"` (ou `"NAO ENCONTRADO"`).

3. **Método `main`:**

   * Lê os $N$ comandos via `LeitorRapido` (usando `nextLong()` para as matrículas) e acumula todas as saídas em um `StringBuilder`.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo

Salve o código em um arquivo chamado **`Main.java`** dentro da pasta `Questao10` (certifique-se de que não há declaração `package` na primeira linha).

### Passo 2: Compilar e executar no terminal

Abra o terminal na pasta `Questao10` e execute:

```
javac Main.java
java Main

```

### Passo 3: Validar a saída

1. Com o terminal aguardando a entrada (cursor piscando), cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.

2. Confirme se as dez linhas exibidas correspondem exatamente ao bloco de **Saída Esperada**.

3. *(Opcional)* Para validar usando um arquivo `entrada.txt` no PowerShell:

   ```
   Get-Content entrada.txt | java Main
   
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```
10
CADASTRAR 20231047 ANA
CADASTRAR 20232817 BRUNO
CADASTRAR 20231033 CARLA
BUSCAR 20232817
CADASTRAR 20232817 BRUNA
BUSCAR 20232817
REMOVER 20231047
BUSCAR 20231047
REMOVER 20239999
TOTAL

```

### Saída Esperada

```
CADASTRADO
CADASTRADO
CADASTRADO
BRUNO
ATUALIZADO
BRUNA
REMOVIDO
NAO ENCONTRADO
NAO ENCONTRADO
2

```