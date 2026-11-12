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

        public Integer primeiro() {
            if (estaVazia()) {
                return null;
            }
            return this.inicio.valor;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            sc.close();
            return;
        }

        int n = sc.nextInt();
        Fila fila = new Fila();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = sc.next();

            if (comando.equals("CHEGAR")) {
                int x = sc.nextInt();
                fila.enfileirar(x);
            } else if (comando.equals("ATENDER")) {
                Integer senha = fila.desenfileirar();
                saida.append(senha == null ? "VAZIA" : senha).append("\n");
            } else if (comando.equals("PROXIMO")) {
                Integer senha = fila.primeiro();
                saida.append(senha == null ? "VAZIA" : senha).append("\n");
            }
        }

        System.out.print(saida.toString());
        sc.close();
    }
}