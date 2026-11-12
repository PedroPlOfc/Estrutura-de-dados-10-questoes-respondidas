# Questão 5 — Linear contra Binária

## 1. Metodologia e Estrutura de Dados Escolhida

O problema compara o desempenho prático entre dois algoritmos clássicos de pesquisa — **Busca Linear** e **Busca Binária** — sobre uma coleção estática de elementos previamente ordenados.

### Estrutura Adotada: Vetor Primitivo (`int[]`)

A coleção é armazenada em um **vetor unidimensional primitivo** (`int[] v = new int[n]`), sem uso de coleções da biblioteca padrão.

**Por que essa abordagem?**
A busca binária depende essencialmente de **acesso direto (indexado)** ao elemento central de qualquer subintervalo do conjunto em tempo constante. Em estruturas encadeadas, alcançar a posição `meio` exigiria percorrer nós sequencialmente, anulando o ganho logarítmico do algoritmo. O vetor primitivo permite acessar `v[i]` e `v[meio]` diretamente via endereço de memória em $O(1)$.

### Análise da Terceira Consulta (`alvo = 3`)

* **Por que a busca linear foi melhor nesse caso?**
  O valor buscado (`3`) está localizado na primeira posição do vetor (`índice 0`), o que configura o **melhor caso** da busca linear (apenas $1$ comparação). Já a busca binária sempre inicia sua inspeção pelo meio do intervalo (`meio = 3`, depois `1` e, por fim, `0`), realizando $3$ comparações até alcançar a extremidade esquerda.

* **Isso contradiz a análise de complexidade?**
  **Não.** A análise assintótica ($O(N)$ contra $O(\log N)$) descreve o comportamento e a taxa de crescimento do algoritmo conforme o tamanho da entrada $N$ aumenta, avaliando principalmente o **pior caso** (como ocorre na segunda consulta, quando o elemento `5` não existe no vetor) ou o **caso médio**. Casos pontuais favoráveis a um algoritmo $O(N)$ em entradas específicas não invalidam sua ordem de crescimento assintótica.

### Análise de Complexidade

* **Busca Linear:**
  * Melhor caso: $O(1)$ (elemento na primeira posição).
  * Pior e caso médio: $O(N)$ (elemento na última posição ou ausente).
* **Busca Binária:**
  * Melhor caso: $O(1)$ (elemento exatamente no meio inicial do vetor).
  * Pior e caso médio: $O(\log N)$ (divide o espaço de busca pela metade a cada passo).
* **Complexidade de Espaço:** $O(N)$ para armazenar os $N$ inteiros no vetor.

## 2. Código-Fonte (`Main.java`)

```java
// Estrutura de dados escolhida: Vetor primitivo unidimensional (int[]), que garante
// acesso direto por índice em O(1), requisito essencial para a busca binária.
// Complexidade das operações principais:
// - Busca Linear: O(1) no melhor caso e O(N) no pior caso
// - Busca Binária: O(1) no melhor caso e O(log N) no pior caso
//
// Resposta sobre a terceira consulta (alvo = 3):
// A busca linear realizou menos comparações (1 contra 3) porque o valor 3 está
// exatamente na primeira posição do vetor (índice 0), que é o melhor caso da busca
// linear. Já a busca binária sempre começa testando o meio do intervalo (índices 3,
// depois 1 e por fim 0). Isso NÃO contradiz a análise de complexidade, pois a notação
// assintótica (O(N) vs O(log N)) avalia o crescimento do custo para entradas grandes
// no pior caso (ou caso médio), e não instâncias isoladas de melhor caso.

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

    // Retorna um vetor de 2 posições: [posicaoEncontrada, quantidadeComparacoes]
    static int[] buscaLinear(int[] v, int alvo) {
        int comparacoes = 0;
        for (int i = 0; i < v.length; i++) {
            comparacoes++;
            if (v[i] == alvo) {
                return new int[]{i, comparacoes};
            }
        }
        return new int[]{-1, comparacoes};
    }

    // Retorna um vetor de 2 posições: [posicaoEncontrada, quantidadeComparacoes]
    static int[] buscaBinaria(int[] v, int alvo) {
        int inicio = 0;
        int fim = v.length - 1;
        int comparacoes = 0;

        while (inicio <= fim) {
            comparacoes++;
            int meio = inicio + (fim - inicio) / 2;

            if (v[meio] == alvo) {
                return new int[]{meio, comparacoes};
            } else if (v[meio] < alvo) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return new int[]{-1, comparacoes};
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = in.nextInt();
        }

        int q = in.nextInt();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < q; i++) {
            int alvo = in.nextInt();
            int[] resLinear = buscaLinear(v, alvo);
            int[] resBinaria = buscaBinaria(v, alvo);

            saida.append(resLinear[0])
                 .append(" ")
                 .append(resLinear[1])
                 .append(" ")
                 .append(resBinaria[1])
                 .append("\n");
        }

        System.out.print(saida.toString());
    }
}
```

## 3. Explicação Detalhada do Código

1. **Classe `LeitorRapido`:** Utiliza `BufferedReader` e `StringTokenizer` para ler até $100.000$ elementos do vetor e $1.000$ consultas de forma eficiente sem sobrecarga de memória ou tempo.
2. **Método `buscaLinear(int[] v, int alvo)`:**
   * Percorre o vetor sequencialmente do índice `0` até `v.length - 1`.
   * Incrementa `comparacoes` a cada verificação `v[i] == alvo`.
   * Se encontrar o elemento, retorna imediatamente o índice `i` e o total de comparações feitas; caso percorra todo o vetor sem sucesso, retorna `-1` e `N` comparações.
3. **Método `buscaBinaria(int[] v, int alvo)`:**
   * Mantém os limites `inicio` e `fim` do intervalo de busca.
   * A cada iteração do laço `while (inicio <= fim)`, incrementa `comparacoes`, calcula `meio = inicio + (fim - inicio) / 2` (evitando estouro de inteiro) e consulta `v[meio]`.
   * Se `v[meio] == alvo`, retorna a posição e o número de comparações. Se `v[meio] < alvo`, descarta a metade esquerda (`inicio = meio + 1`); caso contrário, descarta a metade direita (`fim = meio - 1`).
4. **Método `main`:**
   * Lê o vetor ordenado `v` de tamanho `N`, processa cada uma das `Q` consultas e formata a saída contendo `<posição> <comparações_linear> <comparações_binária>`.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo

Salve o código em um arquivo chamado **`Main.java`** dentro da pasta `Questao5` 

### Passo 2: Compilar e executar no terminal

Abra o terminal na pasta `Questao5` e execute:

```bash
javac Main.java
java Main
```

### Passo 3: Validar a saída

1. Com o terminal aguardando a entrada, cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.
2. Confirme se as três linhas exibidas correspondem exatamente ao bloco de **Saída Esperada**.
3. *(Opcional)* Para validar usando um arquivo `entrada.txt` no PowerShell:

   ```powershell
   Get-Content entrada.txt | java Main
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```text
8
3 8 12 19 25 31 40 47
3
40
5
3
```

### Saída Esperada

```text
6 7 3
-1 8 3
0 1 3
```