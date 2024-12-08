import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String caminhoArquivo = "arq.txt"; // Fallback para arquivo padrão
        if (args.length > 0) {
            caminhoArquivo = args[0];
        }

        try {
            // Leitura do arquivo fonte
            String codigoFonte = Files.readString(Paths.get(caminhoArquivo));
            System.out.println("Arquivo lido com sucesso!");

            // Análise léxica
            Lexico lexico = new Lexico(codigoFonte);
            List<Token> tokens = lexico.analisar();
            System.out.println("Análise léxica concluída. Tokens gerados:");
            lexico.imprimeTokens();

            // Análise sintática
            Sintatico sintatico = new Sintatico(tokens);
            System.out.println("Iniciando análise sintática...");
            sintatico.analisar();
            System.out.println("Análise sintática concluída com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Erro durante a análise: " + e.getMessage());
        }
    }
}
