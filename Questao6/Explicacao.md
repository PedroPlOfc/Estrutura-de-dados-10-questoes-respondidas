# Questão 6 — Classificação da Corrida

## 1. Metodologia e Estrutura de Dados Escolhida

Como a entrada pode conter até $N = 200.000$ atletas, algoritmos quadráticos ($O(N^2)$) como *Bubble Sort*, *Selection Sort* ou *Insertion Sort* realizariam dezenas de bilhões de operações no pior caso, excedendo o limite de tempo. É indispensável utilizar um método de ordenação eficiente de ordem $O(N \log N)$.

### Estrutura Adotada: Vetor de Objetos (`Atleta[]`) e Vetor Auxiliar

Os dados de cada competidor (`peito` e `tempo`) são encapsulados em uma classe `Atleta` e armazenados em um **vetor primitivo** `Atleta[] atletas = new Atleta[n]`, acompanhado de um único **vetor auxiliar** `Atleta[] aux = new Atleta[n]` alocado previamente para a etapa de intercalação.

### Algoritmo de Ordenação Escolhido: *Merge Sort*

O **Merge Sort** (ordenação por intercalação) foi escolhido pelos seguintes motivos técnicos:

* **Garantia no pior caso:** Diferente do *Quick Sort* tradicional — que pode degradar para $O(N^2)$ dependendo da escolha do pivô e da disposição inicial dos dados —, o *Merge Sort* mantém complexidade estritamente $O(N \log N)$ em **todos os casos** (melhor, médio e pior caso).

* **Profundidade de recursão segura:** Como divide o vetor sempre ao meio, sua árvore de recursão possui altura máxima $\lceil \log_2 N \rceil$ (cerca de $18$ níveis para $N = 200.000$), eliminando qualquer risco de estouro de pilha (*StackOverflowError*).

* **Estabilidade:** Preserva a ordem relativa de elementos equivalentes e aplica facilmente a regra composta de comparação: menor `tempo` primeiro e, em caso de empate, menor número de `peito`.

### Análise de Complexidade

* **Tempo (Pior Caso, Caso Médio e Melhor Caso):** $O(N \log N)$ — há $\log_2 N$ níveis de divisão e, em cada nível, a intercalação percorre linearmente os $N$ elementos.

* **Espaço Auxiliar:** $O(N)$ — devido ao vetor auxiliar `aux` utilizado durante o processo de intercalação (*merge*).

## 2. Código-Fonte (`Main.java`)

```java
// Estrutura de dados escolhida: Vetor primitivo de objetos (Atleta[]) com vetor auxiliar (Atleta[]).
// Algoritmo de ordenação utilizado: Merge Sort (ordenação por intercalação).
// Complexidade das operações principais:
// - Ordenação (Merge Sort) no pior caso: O(N log N)
// - Ordenação no caso médio e melhor caso: O(N log N)
// - Complexidade de espaço auxiliar: O(N)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static class Atleta {
        int peito;
        int tempo;

        Atleta(int peito, int tempo) {
            this.peito = peito;
            this.tempo = tempo;
        }
    }

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

    // Define se o atleta 'a' deve aparecer antes do atleta 'b' na classificação
    static boolean vemAntes(Atleta a, Atleta b) {
        if (a.tempo != b.tempo) {
            return a.tempo < b.tempo;
        }
        return a.peito <= b.peito;
    }

    static void mergeSort(Atleta[] v, Atleta[] aux, int inicio, int fim) {
        if (inicio >= fim) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;
        mergeSort(v, aux, inicio, meio);
        mergeSort(v, aux, meio + 1, fim);
        intercalar(v, aux, inicio, meio, fim);
    }

    static void intercalar(Atleta[] v, Atleta[] aux, int inicio, int meio, int fim) {
        for (int k = inicio; k <= fim; k++) {
            aux[k] = v[k];
        }

        int i = inicio;
        int j = meio + 1;
        int k = inicio;

        while (i <= meio && j <= fim) {
            if (vemAntes(aux[i], aux[j])) {
                v[k++] = aux[i++];
            } else {
                v[k++] = aux[j++];
            }
        }

        while (i <= meio) {
            v[k++] = aux[i++];
        }

        while (j <= fim) {
            v[k++] = aux[j++];
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        Atleta[] atletas = new Atleta[n];
        Atleta[] aux = new Atleta[n];

        for (int i = 0; i < n; i++) {
            int peito = in.nextInt();
            int tempo = in.nextInt();
            atletas[i] = new Atleta(peito, tempo);
        }

        mergeSort(atletas, aux, 0, n - 1);

        StringBuilder saida = new StringBuilder();
        for (int i = 0; i < n; i++) {
            saida.append(i + 1)
                 .append(" ")
                 .append(atletas[i].peito)
                 .append(" ")
                 .append(atletas[i].tempo)
                 .append("\n");
        }

        System.out.print(saida.toString());
    }
}
```

## 3. Explicação Detalhada do Código

1. **Classe `Atleta`:** Agrupa os dois atributos de cada corredor (`peito` e `tempo`), garantindo que, durante as trocas e intercalações, o número de peito permaneça sempre vinculado ao seu respectivo tempo.

2. **Classe `LeitorRapido`:** Utiliza `BufferedReader` e `StringTokenizer` para realizar a leitura rápida de até $200.000$ linhas sem o gargalo de desempenho do `Scanner`.

3. **Método `vemAntes(Atleta a, Atleta b)`:** Encapsula o critério de ordenação exigido pelo problema: compara primeiro `a.tempo < b.tempo`; caso os tempos sejam iguais, desempata pelo menor número de peito (`a.peito <= b.peito`).

4. **Métodos `mergeSort` e `intercalar`:**

   * `mergeSort` divide recursivamente o intervalo `[inicio, fim]` ao meio até atingir subvetores de tamanho $1$.

   * `intercalar` copia o trecho atual para o vetor auxiliar `aux` (pré-alocado uma única vez no `main` para economizar memória) e mescla as duas metades ordenadas de volta no vetor original `v`.

5. **Método `main`:**

   * Lê os $N$ atletas, executa `mergeSort(atletas, aux, 0, n - 1)` e monta a saída com `StringBuilder`, imprimindo a classificação enumerada de `1` até `N`.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo

Salve o código em um arquivo chamado **`Main.java`** dentro da pasta `Questao6` 

### Passo 2: Compilar e executar no terminal

Abra o terminal na pasta `Questao6` e execute:

```powershell
javac Main.java
java Main
```

### Passo 3: Validar a saída

1. Com o terminal aguardando a entrada, cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.

2. Confirme se as seis linhas exibidas correspondem exatamente ao bloco de **Saída Esperada** (repare no desempate entre os tempos `3545` e `3610`).

3. *(Opcional)* Para validar usando um arquivo `entrada.txt` no PowerShell:

   ```powershell
   Get-Content entrada.txt | java Main
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```text
6
42 3610
7 3545
15 3610
3 3720
28 3545
11 3499
```

### Saída Esperada

```text
1 11 3499
2 7 3545
3 28 3545
4 15 3610
5 42 3610
6 3 3720
```