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
