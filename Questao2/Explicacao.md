# Questão 2 — Manobra dos Vagões

## 1. Metodologia e Estrutura de Dados Escolhida

No desvio ferroviário descrito no problema, apenas o último vagão que entrou pode sair primeiro. Esse comportamento caracteriza rigorosamente o princípio **LIFO** (*Last-In, First-Out* — o último a entrar é o primeiro a sair).

### Estrutura Adotada: Pilha Encadeada

A estrutura foi implementada manualmente utilizando **nós e referências** (sem uso de bibliotecas prontas do Java), mantendo um ponteiro principal:

* **`topo`:** aponta para o nó correspondente ao vagão que está na saída do desvio no momento.

**Estratégia de Simulação:**
Como os vagões chegam sempre na ordem sequencial $1, 2, \dots, N$, mantemos um contador `proximoVagao` (iniciado em $1$) e um índice `idx` percorrendo o vetor com a permutação desejada de saída:
1. Se o vagão no `topo` do desvio for igual ao próximo vagão exigido na saída (`desejado[idx]`), ele é desempilhado e avançamos `idx`.
2. Caso contrário, se ainda houver vagões chegando ($proximoVagao \le N$), encaminhamos `proximoVagao` para o desvio (empilhamento) e incrementamos `proximoVagao`. *(Nota: se esse vagão recém-empilhado for exatamente o que deve sair em seguida, ele será desempilhado imediatamente na próxima iteração, simulando a passagem direta para a saída).*
3. Se o topo não corresponde ao vagão esperado e não há mais vagões chegando, a sequência desejada é impossível (`NAO`). Se todos os $N$ vagões forem posicionados na ordem correta (`idx == N`), a sequência é válida (`SIM`).

### Análise de Complexidade

* **Empilhar (`push`):** $O(1)$ — inserção direta no topo da pilha.
* **Desempilhar (`pop`):** $O(1)$ — remoção direta do nó no topo da pilha.
* **Consultar topo (`peek`):** $O(1)$ — leitura direta do valor armazenado no topo.
* **Simulação por caso de teste:** $O(N)$ — cada um dos $N$ vagões entra na pilha no máximo uma vez e sai no máximo uma vez, resultando em no máximo $2N$ operações de pilha.
* **Complexidade de Espaço:** $O(N)$ por caso de teste, para armazenar a sequência desejada e os nós na pilha.

## 2. Código-Fonte (`Main.java`)

```java
// Estrutura de dados escolhida: Pilha encadeada (implementada com nós e referências).
// Complexidade das operações principais:
// - Empilhar (push): O(1)
// - Desempilhar (pop): O(1)
// - Consultar topo (peek): O(1)
// - Simulação de cada caso de teste com N vagões: O(N)

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

    static class Pilha {
        private No topo;

        public Pilha() {
            this.topo = null;
        }

        public boolean estaVazia() {
            return this.topo == null;
        }

        public void empilhar(int valor) {
            No novo = new No(valor);
            novo.proximo = this.topo;
            this.topo = novo;
        }

        public Integer desempilhar() {
            if (estaVazia()) {
                return null;
            }
            int valor = this.topo.valor;
            this.topo = this.topo.proximo;
            return valor;
        }

        public Integer topo() {
            if (estaVazia()) {
                return null;
            }
            return this.topo.valor;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            sc.close();
            return;
        }

        int t = sc.nextInt();
        StringBuilder saida = new StringBuilder();

        for (int caso = 0; caso < t; caso++) {
            int n = sc.nextInt();
            int[] desejado = new int[n];
            for (int i = 0; i < n; i++) {
                desejado[i] = sc.nextInt();
            }

            Pilha desvio = new Pilha();
            int proximoVagao = 1;
            int idx = 0;

            while (idx < n) {
                if (!desvio.estaVazia() && desvio.topo() == desejado[idx]) {
                    desvio.desempilhar();
                    idx++;
                } else if (proximoVagao <= n) {
                    desvio.empilhar(proximoVagao);
                    proximoVagao++;
                } else {
                    break;
                }
            }

            if (idx == n) {
                saida.append("SIM\n");
            } else {
                saida.append("NAO\n");
            }
        }

        System.out.print(saida.toString());
        sc.close();
    }
}
```

## 3. Explicação Detalhada do Código

1. **Classe `No`:** Representa um vagão dentro do desvio ferroviário, guardando sua numeração (`valor`) e a referência para o vagão imediatamente abaixo dele (`proximo`).
2. **Classe `Pilha`:**
   * `estaVazia()`: retorna `true` quando `topo == null`.
   * `empilhar(int valor)`: cria um novo nó, aponta seu `proximo` para o antigo `topo` e atualiza `topo` para o novo nó.
   * `desempilhar()`: remove e retorna o valor do nó no `topo`, movendo a referência `topo` para `topo.proximo`.
   * `topo()`: lê o número do vagão no topo sem removê-lo, permitindo verificar se ele já pode sair.
3. **Método `main`:**
   * Lê a quantidade $T$ de casos de teste.
   * Para cada caso, lê o inteiro $N$ e preenche o vetor primitivo `desejado` com a ordem alvo.
   * Executa o laço de simulação com a instância `desvio` da `Pilha`. Ao final, verifica se `idx == n` (todos os vagões saíram na ordem pedida) para registrar `SIM` ou `NAO` no `StringBuilder`.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo

Salve o código em um arquivo chamado **`Main.java`** dentro da pasta da Questão 2.

### Passo 2: Compilar e executar no terminal

Abra o terminal na pasta onde o arquivo `Main.java` está salvo e execute:

```bash
javac Main.java
java Main
```

### Passo 3: Validar a saída

1. Com o terminal aguardando a entrada, cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.
2. Verifique se as três linhas impressas correspondem exatamente à **Saída Esperada** (`SIM`, `SIM`, `NAO`, sem acento).
3. *(Opcional)* Para validar via arquivo `entrada.txt` no PowerShell:
   ```powershell
   Get-Content entrada.txt | java Main
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```text
3
5
5 4 3 2 1
5
1 2 5 4 3
5
3 1 2 5 4
```

### Saída Esperada

```text
SIM
SIM
NAO
```