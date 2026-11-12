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

    static class No {
        int valor;
        int altura;
        No esquerda;
        No direita;

        No(int valor) {
            this.valor = valor;
            this.altura = 0;
            this.esquerda = null;
            this.direita = null;
        }
    }

    static class ArvoreAVL {
        private No raiz;
        private String statusUltimaInsercao;

        public ArvoreAVL() {
            this.raiz = null;
        }

        private int alturaNo(No no) {
            return (no == null) ? -1 : no.altura;
        }

        private void atualizarAltura(No no) {
            int altEsq = alturaNo(no.esquerda);
            int altDir = alturaNo(no.direita);
            no.altura = 1 + (altEsq > altDir ? altEsq : altDir);
        }

        private int fatorBalanceamento(No no) {
            if (no == null) {
                return 0;
            }
            return alturaNo(no.esquerda) - alturaNo(no.direita);
        }

        private No rotacaoDireita(No y) {
            No x = y.esquerda;
            No t2 = x.direita;

            x.direita = y;
            y.esquerda = t2;

            atualizarAltura(y);
            atualizarAltura(x);

            return x;
        }

        private No rotacaoEsquerda(No x) {
            No y = x.direita;
            No t2 = y.esquerda;

            y.esquerda = x;
            x.direita = t2;

            atualizarAltura(x);
            atualizarAltura(y);

            return y;
        }

        public String inserir(int valor) {
            this.statusUltimaInsercao = "SEM ROTACAO";
            this.raiz = inserirRecursivo(this.raiz, valor);
            return this.statusUltimaInsercao;
        }

        private No inserirRecursivo(No no, int valor) {
            if (no == null) {
                return new No(valor);
            }

            if (valor == no.valor) {
                this.statusUltimaInsercao = "REPETIDO";
                return no;
            } else if (valor < no.valor) {
                no.esquerda = inserirRecursivo(no.esquerda, valor);
            } else {
                no.direita = inserirRecursivo(no.direita, valor);
            }

            atualizarAltura(no);
            int fb = fatorBalanceamento(no);

            // Caso 1: Desequilíbrio à esquerda (fb > 1)
            if (fb > 1) {
                if (valor < no.esquerda.valor) {
                    this.statusUltimaInsercao = "ESQUERDA-ESQUERDA";
                    return rotacaoDireita(no);
                } else {
                    this.statusUltimaInsercao = "ESQUERDA-DIREITA";
                    no.esquerda = rotacaoEsquerda(no.esquerda);
                    return rotacaoDireita(no);
                }
            }

            // Caso 2: Desequilíbrio à direita (fb < -1)
            if (fb < -1) {
                if (valor > no.direita.valor) {
                    this.statusUltimaInsercao = "DIREITA-DIREITA";
                    return rotacaoEsquerda(no);
                } else {
                    this.statusUltimaInsercao = "DIREITA-ESQUERDA";
                    no.direita = rotacaoDireita(no.direita);
                    return rotacaoEsquerda(no);
                }
            }

            return no;
        }

        public String preOrdem() {
            if (this.raiz == null) {
                return "VAZIA";
            }
            StringBuilder sb = new StringBuilder();
            percorrerPreOrdem(this.raiz, sb);
            return sb.toString();
        }

        private void percorrerPreOrdem(No no, StringBuilder sb) {
            if (no == null) {
                return;
            }
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(no.valor);
            percorrerPreOrdem(no.esquerda, sb);
            percorrerPreOrdem(no.direita, sb);
        }

        public int altura() {
            return alturaNo(this.raiz);
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        ArvoreAVL avl = new ArvoreAVL();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = in.next();

            if (comando.equals("INSERIR")) {
                int x = in.nextInt();
                saida.append(avl.inserir(x)).append("\n");
            } else if (comando.equals("PREORDEM")) {
                saida.append(avl.preOrdem()).append("\n");
            } else if (comando.equals("ALTURA")) {
                saida.append(avl.altura()).append("\n");
            }
        }

        System.out.print(saida.toString());
    }
}