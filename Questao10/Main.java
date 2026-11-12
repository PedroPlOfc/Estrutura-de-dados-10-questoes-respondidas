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

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    }

    static class No {
        long matricula;
        String nome;
        No proximo;

        No(long matricula, String nome) {
            this.matricula = matricula;
            this.nome = nome;
            this.proximo = null;
        }
    }

    static class TabelaHash {
        private No[] tabela;
        private int capacidade;
        private int tamanho;

        public TabelaHash() {
            this.capacidade = 16;
            this.tabela = new No[this.capacidade];
            this.tamanho = 0;
        }

        private int calcularIndice(long matricula, int cap) {
            int h = (int) (matricula ^ (matricula >>> 32));
            h ^= (h >>> 16);
            return (h & 0x7FFFFFFF) % cap;
        }

        private void redimensionar() {
            int novaCapacidade = this.capacidade * 2;
            No[] novaTabela = new No[novaCapacidade];

            for (int i = 0; i < this.capacidade; i++) {
                No atual = this.tabela[i];
                while (atual != null) {
                    No proximo = atual.proximo;
                    int novoIndice = calcularIndice(atual.matricula, novaCapacidade);
                    atual.proximo = novaTabela[novoIndice];
                    novaTabela[novoIndice] = atual;
                    atual = proximo;
                }
            }

            this.tabela = novaTabela;
            this.capacidade = novaCapacidade;
        }

        public String cadastrar(long matricula, String nome) {
            int indice = calcularIndice(matricula, this.capacidade);
            No atual = this.tabela[indice];

            while (atual != null) {
                if (atual.matricula == matricula) {
                    atual.nome = nome;
                    return "ATUALIZADO";
                }
                atual = atual.proximo;
            }

            No novo = new No(matricula, nome);
            novo.proximo = this.tabela[indice];
            this.tabela[indice] = novo;
            this.tamanho++;

            // Dobra a capacidade quando o fator de carga (tamanho / capacidade) ultrapassa 0,75
            if (this.tamanho * 4 > this.capacidade * 3) {
                redimensionar();
            }

            return "CADASTRADO";
        }

        public String buscar(long matricula) {
            int indice = calcularIndice(matricula, this.capacidade);
            No atual = this.tabela[indice];

            while (atual != null) {
                if (atual.matricula == matricula) {
                    return atual.nome;
                }
                atual = atual.proximo;
            }

            return "NAO ENCONTRADO";
        }

        public String remover(long matricula) {
            int indice = calcularIndice(matricula, this.capacidade);
            No anterior = null;
            No atual = this.tabela[indice];

            while (atual != null) {
                if (atual.matricula == matricula) {
                    if (anterior == null) {
                        this.tabela[indice] = atual.proximo;
                    } else {
                        anterior.proximo = atual.proximo;
                    }
                    this.tamanho--;
                    return "REMOVIDO";
                }
                anterior = atual;
                atual = atual.proximo;
            }

            return "NAO ENCONTRADO";
        }

        public int total() {
            return this.tamanho;
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        TabelaHash cadastro = new TabelaHash();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String comando = in.next();

            if (comando.equals("CADASTRAR")) {
                long matricula = in.nextLong();
                String nome = in.next();
                saida.append(cadastro.cadastrar(matricula, nome)).append("\n");
            } else if (comando.equals("BUSCAR")) {
                long matricula = in.nextLong();
                saida.append(cadastro.buscar(matricula)).append("\n");
            } else if (comando.equals("REMOVER")) {
                long matricula = in.nextLong();
                saida.append(cadastro.remover(matricula)).append("\n");
            } else if (comando.equals("TOTAL")) {
                saida.append(cadastro.total()).append("\n");
            }
        }

        System.out.print(saida.toString());
    }
}