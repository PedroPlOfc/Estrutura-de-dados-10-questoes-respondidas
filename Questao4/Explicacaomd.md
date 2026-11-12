# Questão 4 — Central de Suporte

## 1. Metodologia e Estruturas de Dados Escolhidas

O sistema precisa gerenciar três fluxos distintos de dados com regras próprias de ordenação e acesso:

1. **Solicitações Normais (Ordem de chegada — FIFO):** utiliza uma **Fila Encadeada** com ponteiros para `inicio` e `fim`. Quem chega primeiro é atendido primeiro.
2. **Solicitações Urgentes (Mais recente primeiro — LIFO):** utiliza uma **Pilha Encadeada** com ponteiro para `topo`. A última solicitação urgente registrada é sempre a primeira a sair.
3. **Histórico Geral de Atendimentos (Ordem cronológica de conclusão):** utiliza uma **Lista Simplesmente Encadeada** com ponteiros para `inicio` e `fim`, permitindo anexar cada código atendido diretamente ao final do histórico sem percorrer os registros anteriores.

**Regra de Prioridade no Atendimento (`ATENDER`):**
Ao receber o comando `ATENDER`, o sistema verifica primeiro se a pilha de urgentes possui elementos. Caso possua, desempilha e atende o topo. Somente quando a pilha de urgentes está vazia é que o sistema desenfileira a solicitação mais antiga da fila normal. Em ambos os casos de sucesso, o código atendido é inserido no final da lista de histórico. Se ambas estiverem vazias, imprime `VAZIA`.

### Análise de Complexidade

* **Registrar Normal (`NORMAL X`):** $O(1)$ — inserção no final da fila encadeada via ponteiro `fim`.
* **Registrar Urgente (`URGENTE X`):** $O(1)$ — inserção no topo da pilha encadeada via ponteiro `topo`.
* **Atender (`ATENDER`):** $O(1)$ — remoção no topo da pilha (ou no início da fila) e inserção no final da lista de histórico em tempo constante.
* **Gerar Relatório (`RELATORIO`):** $O(K)$, onde $K$ é a quantidade de atendimentos já concluídos até o momento ($K \le N$).
* **Complexidade de Espaço:** $O(N)$, proporcional à quantidade de comandos processados.

## 2. Código-Fonte (`Main.java`)

```java
// Estruturas de dados escolhidas: Pilha encadeada (urgentes), Fila encadeada (normais)
// e Lista simplesmente encadeada com ponteiro para o fim (histórico), todas com nós e referências.
// Complexidade das operações principais:
// - Registrar Normal (NORMAL X): O(1)
// - Registrar Urgente (URGENTE X): O(1)
// - Atender (ATENDER): O(1)
// - Gerar Relatório (RELATORIO): O(K), onde K é o total de atendimentos realizados

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

    // Estrutura FIFO para solicitações normais
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
    }

    // Estrutura LIFO para solicitações urgentes
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
    }

    // Lista encadeada para registrar o histórico de atendimentos em ordem cronológica
    static class ListaHistorico {
        private No inicio;
        private No fim;

        public ListaHistorico() {
            this.inicio = null;
            this.fim = null;
        }

        public boolean estaVazia() {
            return this.inicio == null;
        }

        public void adicionarNoFim(int valor) {
            No novo = new No(valor);
            if (estaVazia()) {
                this.inicio = novo;
                this.fim = novo;
            } else {
                this.fim.proximo = novo;
                this.fim = novo;
            }
        }

        public String gerarRelatorio() {
            if (estaVazia()) {
                return "VAZIO";
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
        Fila normais = new Fila();
        Pilha urgentes = new Pilha();
        ListaHistorico historico = new ListaHistorico();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = sc.next();

            if (comando.equals("NORMAL")) {
                int x = sc.nextInt();
                normais.enfileirar(x);
            } else if (comando.equals("URGENTE")) {
                int x = sc.nextInt();
                urgentes.empilhar(x);
            } else if (comando.equals("ATENDER")) {
                Integer atendido = null;

                if (!urgentes.estaVazia()) {
                    atendido = urgentes.desempilhar();
                } else if (!normais.estaVazia()) {
                    atendido = normais.desenfileirar();
                }

                if (atendido == null) {
                    saida.append("VAZIA\n");
                } else {
                    historico.adicionarNoFim(atendido);
                    saida.append(atendido).append("\n");
                }
            } else if (comando.equals("RELATORIO")) {
                saida.append(historico.gerarRelatorio()).append("\n");
            }
        }

        System.out.print(saida.toString());
        sc.close();
    }
}
```

## 3. Explicação Detalhada do Código

1. **Classe `No`:** Estrutura base reutilizada pelas três coleções. Armazena o código numérico da solicitação (`valor`) e a referência para o nó seguinte (`proximo`).
2. **Classe `Fila`:** Gerencia as solicitações comuns. `enfileirar` insere novos chamados em `fim`, e `desenfileirar` retira o chamado mais antigo em `inicio`.
3. **Classe `Pilha`:** Gerencia as solicitações prioritárias. `empilhar` coloca o novo chamado urgente no `topo`, e `desempilhar` remove o mais recente.
4. **Classe `ListaHistorico`:** Armazena todos os atendimentos já concluídos. O método `adicionarNoFim` insere novos registros em $O(1)$ usando o ponteiro `fim`, enquanto `gerarRelatorio` retorna `"VAZIO"` caso nenhum atendimento tenha ocorrido ou concatena todos os códigos separados por espaço simples.
5. **Método `main`:**
   * Interpreta cada um dos $N$ comandos.
   * No comando `ATENDER`, prioriza `urgentes.desempilhar()`. Apenas se `urgentes` estiver vazia é que consulta `normais.desenfileirar()`.
   * Atenção à distinção exigida pelo enunciado: imprime **`VAZIA`** quando não há solicitações para atender e **`VAZIO`** quando o histórico está sem registros.

## 4. Como Compilar, Executar e Validar

### Passo 1: Preparar o arquivo
Salve o código em um arquivo chamado **`Main.java`** dentro da pasta `Questao4`

### Passo 2: Compilar e executar no terminal
Abra o terminal na pasta `Questao4` e execute:

```powershell
javac Main.java
java Main
```

### Passo 3: Validar a saída
1. Com o terminal aguardando a entrada, cole o bloco de **Entrada** da seção abaixo e pressione **Enter**.
2. Confirme se as cinco linhas exibidas correspondem exatamente ao bloco de **Saída Esperada**.
3. *(Opcional)* Para validar usando um arquivo `entrada.txt` no PowerShell:

   ```powershell
   Get-Content entrada.txt | java Main
   ```

## 5. Exemplo para Teste (Copiar e Colar)

### Entrada

```text
10
NORMAL 101
NORMAL 102
URGENTE 901
URGENTE 902
ATENDER
ATENDER
NORMAL 103
ATENDER
ATENDER
RELATORIO
```

### Saída Esperada

```text
902
901
101
102
902 901 101 102
```