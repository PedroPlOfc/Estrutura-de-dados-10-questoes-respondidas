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

    // Retorna um vetor de 2 posições: [posicaoEncontrada, quantidadeComparacoes]
    static int[] buscaLinear(int[] v, int alvo) {
        int comparacoes = 0;
        for (int i = 0; i < v.length; i++) {
            comparacoes++;
            if (v[i] == alvo) {
                return new int[]{i, comparacoes};
            }
        }
        return new int[]{-1, comparacoes};
    }

    // Retorna um vetor de 2 posições: [posicaoEncontrada, quantidadeComparacoes]
    static int[] buscaBinaria(int[] v, int alvo) {
        int inicio = 0;
        int fim = v.length - 1;
        int comparacoes = 0;

        while (inicio <= fim) {
            comparacoes++;
            int meio = inicio + (fim - inicio) / 2;

            if (v[meio] == alvo) {
                return new int[]{meio, comparacoes};
            } else if (v[meio] < alvo) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return new int[]{-1, comparacoes};
    }

    public static void main(String[] args) throws IOException {
        LeitorRapido in = new LeitorRapido();
        String primeiroToken = in.next();
        if (primeiroToken == null) {
            return;
        }

        int n = Integer.parseInt(primeiroToken);
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = in.nextInt();
        }

        int q = in.nextInt();
        StringBuilder saida = new StringBuilder();

        for (int i = 0; i < q; i++) {
            int alvo = in.nextInt();
            int[] resLinear = buscaLinear(v, alvo);
            int[] resBinaria = buscaBinaria(v, alvo);

            saida.append(resLinear[0])
                 .append(" ")
                 .append(resLinear[1])
                 .append(" ")
                 .append(resBinaria[1])
                 .append("\n");
        }

        System.out.print(saida.toString());
    }
}