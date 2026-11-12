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