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

    static class Paciente {
        String nome;
        int prioridade;
        int ordemChegada;

        Paciente(String nome, int prioridade, int ordemChegada) {
            this.nome = nome;
            this.prioridade = prioridade;
            this.ordemChegada = ordemChegada;
        }
    }

    static class HeapMaximo {
        private Paciente[] dados;
        private int tamanho;

        public HeapMaximo(int capacidadeMaxima) {
            this.dados = new Paciente[capacidadeMaxima];
            this.tamanho = 0;
        }

        public boolean estaVazio() {
            return this.tamanho == 0;
        }

        // Retorna true se o paciente 'a' deve ser atendido antes do paciente 'b'
        private boolean vemAntes(Paciente a, Paciente b) {
            if (a.prioridade != b.prioridade) {
                return a.prioridade > b.prioridade;
            }
            return a.ordemChegada < b.ordemChegada;
        }

        private void trocar(int i, int j) {
            Paciente temp = this.dados[i];
            this.dados[i] = this.dados[j];
            this.dados[j] = temp;
        }

        private void subir(int indice) {
            while (indice > 0) {
                int pai = (indice - 1) / 2;
                if (vemAntes(this.dados[indice], this.dados[pai])) {
                    trocar(indice, pai);
                    indice = pai;
                } else {
                    break;
                }
            }
        }

        private void descer(int indice) {
            while (true) {
                int esquerda = 2 * indice + 1;
                int direita = 2 * indice + 2;
                int maior = indice;

                if (esquerda < this.tamanho && vemAntes(this.dados[esquerda], this.dados[maior])) {
                    maior = esquerda;
                }
                if (direita < this.tamanho && vemAntes(this.dados[direita], this.dados[maior])) {
                    maior = direita;
                }

                if (maior != indice) {
                    trocar(indice, maior);
                    indice = maior;
                } else {
                    break;
                }
            }
        }

        public void inserir(Paciente paciente) {
            this.dados[this.tamanho] = paciente;
            subir(this.tamanho);
            this.tamanho++;
        }

        public Paciente removerMaximo() {
            if (estaVazio()) {
                return null;
            }
            Paciente maximo = this.dados[0];
            this.tamanho--;
            if (this.tamanho > 0) {
                this.dados[0] = this.dados[this.tamanho];
                this.dados[this.tamanho] = null;
                descer(0);
            } else {
                this.dados[0] = null;
            }
            return maximo;
        }

        public Paciente consultarMaximo() {
            if (estaVazio()) {
                return null;
            }
            return this.dados[0];
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        HeapMaximo filaPrioridade = new HeapMaximo(n);
        int contadorChegada = 0;
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = in.next();

            if (comando.equals("CHEGAR")) {
                String nome = in.next();
                int prioridade = in.nextInt();
                contadorChegada++;
                filaPrioridade.inserir(new Paciente(nome, prioridade, contadorChegada));
            } else if (comando.equals("ATENDER")) {
                Paciente p = filaPrioridade.removerMaximo();
                if (p == null) {
                    saida.append("VAZIA\n");
                } else {
                    saida.append(p.nome).append(" ").append(p.prioridade).append("\n");
                }
            } else if (comando.equals("PROXIMO")) {
                Paciente p = filaPrioridade.consultarMaximo();
                if (p == null) {
                    saida.append("VAZIA\n");
                } else {
                    saida.append(p.nome).append(" ").append(p.prioridade).append("\n");
                }
            }
        }

        System.out.print(saida.toString());
    }
}