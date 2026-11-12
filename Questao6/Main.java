import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static class Atleta {
        int peito;
        int tempo;

        Atleta(int peito, int tempo) {
            this.peito = peito;
            this.tempo = tempo;
        }
    }

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

    // Define se o atleta 'a' deve aparecer antes do atleta 'b' na classificação
    static boolean vemAntes(Atleta a, Atleta b) {
        if (a.tempo != b.tempo) {
            return a.tempo < b.tempo;
        }
        return a.peito <= b.peito;
    }

    static void mergeSort(Atleta[] v, Atleta[] aux, int inicio, int fim) {
        if (inicio >= fim) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;
        mergeSort(v, aux, inicio, meio);
        mergeSort(v, aux, meio + 1, fim);
        intercalar(v, aux, inicio, meio, fim);
    }

    static void intercalar(Atleta[] v, Atleta[] aux, int inicio, int meio, int fim) {
        for (int k = inicio; k <= fim; k++) {
            aux[k] = v[k];
        }

        int i = inicio;
        int j = meio + 1;
        int k = inicio;

        while (i <= meio && j <= fim) {
            if (vemAntes(aux[i], aux[j])) {
                v[k++] = aux[i++];
            } else {
                v[k++] = aux[j++];
            }
        }

        while (i <= meio) {
            v[k++] = aux[i++];
        }

        while (j <= fim) {
            v[k++] = aux[j++];
        }
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        Atleta[] atletas = new Atleta[n];
        Atleta[] aux = new Atleta[n];

        for (int i = 0; i < n; i++) {
            int peito = in.nextInt();
            int tempo = in.nextInt();
            atletas[i] = new Atleta(peito, tempo);
        }

        mergeSort(atletas, aux, 0, n - 1);

        StringBuilder saida = new StringBuilder();
        for (int i = 0; i < n; i++) {
            saida.append(i + 1)
                 .append(" ")
                 .append(atletas[i].peito)
                 .append(" ")
                 .append(atletas[i].tempo)
                 .append("\n");
        }

        System.out.print(saida.toString());
    }
}