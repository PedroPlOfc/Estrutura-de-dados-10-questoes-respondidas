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