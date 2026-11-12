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
        No esquerda;
        No direita;

        No(int valor) {
            this.valor = valor;
            this.esquerda = null;
            this.direita = null;
        }
    }

    static class ArvoreBinariaBusca {
        private No raiz;

        public ArvoreBinariaBusca() {
            this.raiz = null;
        }

        public void inserir(int valor) {
            if (this.raiz == null) {
                this.raiz = new No(valor);
                return;
            }

            No atual = this.raiz;
            while (true) {
                if (valor == atual.valor) {
                    // Códigos repetidos são ignorados
                    return;
                } else if (valor < atual.valor) {
                    if (atual.esquerda == null) {
                        atual.esquerda = new No(valor);
                        return;
                    }
                    atual = atual.esquerda;
                } else {
                    if (atual.direita == null) {
                        atual.direita = new No(valor);
                        return;
                    }
                    atual = atual.direita;
                }
            }
        }

        public String buscar(int valor) {
            if (this.raiz == null) {
                return "NAO";
            }

            StringBuilder caminho = new StringBuilder();
            No atual = this.raiz;

            while (atual != null) {
                caminho.append(atual.valor).append(" ");
                if (valor == atual.valor) {
                    caminho.append("SIM");
                    return caminho.toString();
                } else if (valor < atual.valor) {
                    atual = atual.esquerda;
                } else {
                    atual = atual.direita;
                }
            }

            caminho.append("NAO");
            return caminho.toString();
        }

        public boolean remover(int valor) {
            No pai = null;
            No atual = this.raiz;

            while (atual != null && atual.valor != valor) {
                pai = atual;
                if (valor < atual.valor) {
                    atual = atual.esquerda;
                } else {
                    atual = atual.direita;
                }
            }

            // Código não encontrado na árvore
            if (atual == null) {
                return false;
            }

            // Caso 1 e Caso 2: nó com no máximo um filho
            if (atual.esquerda == null || atual.direita == null) {
                No filho = (atual.esquerda != null) ? atual.esquerda : atual.direita;

                if (pai == null) {
                    this.raiz = filho;
                } else if (pai.esquerda == atual) {
                    pai.esquerda = filho;
                } else {
                    pai.direita = filho;
                }
            } else {
                // Caso 3: nó com dois filhos -> substitui pelo sucessor (menor da subárvore direita)
                No paiSucessor = atual;
                No sucessor = atual.direita;

                while (sucessor.esquerda != null) {
                    paiSucessor = sucessor;
                    sucessor = sucessor.esquerda;
                }

                atual.valor = sucessor.valor;

                if (paiSucessor == atual) {
                    paiSucessor.direita = sucessor.direita;
                } else {
                    paiSucessor.esquerda = sucessor.direita;
                }
            }

            return true;
        }

        public String emOrdem() {
            if (this.raiz == null) {
                return "VAZIA";
            }
            StringBuilder sb = new StringBuilder();
            percorrerEmOrdem(this.raiz, sb);
            return sb.toString();
        }

        private void percorrerEmOrdem(No no, StringBuilder sb) {
            if (no == null) {
                return;
            }
            percorrerEmOrdem(no.esquerda, sb);
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(no.valor);
            percorrerEmOrdem(no.direita, sb);
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
            return calcularAltura(this.raiz);
        }

        private int calcularAltura(No no) {
            if (no == null) {
                return -1;
            }
            int altEsq = calcularAltura(no.esquerda);
            int altDir = calcularAltura(no.direita);
            return 1 + (altEsq > altDir ? altEsq : altDir);
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        ArvoreBinariaBusca abb = new ArvoreBinariaBusca();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = in.next();

            if (comando.equals("INSERIR")) {
                int x = in.nextInt();
                abb.inserir(x);
            } else if (comando.equals("BUSCAR")) {
                int x = in.nextInt();
                saida.append(abb.buscar(x)).append("\n");
            } else if (comando.equals("REMOVER")) {
                int x = in.nextInt();
                if (!abb.remover(x)) {
                    saida.append("ERRO\n");
                }
            } else if (comando.equals("EMORDEM")) {
                saida.append(abb.emOrdem()).append("\n");
            } else if (comando.equals("PREORDEM")) {
                saida.append(abb.preOrdem()).append("\n");
            } else if (comando.equals("ALTURA")) {
                saida.append(abb.altura()).append("\n");
            }
        }

        System.out.print(saida.toString());
    }
}