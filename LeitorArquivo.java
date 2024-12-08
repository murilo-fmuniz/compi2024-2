import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LeitorArquivo {
    private InputStream is;

    public LeitorArquivo(String arquivo) throws FileNotFoundException {
        this.is = new FileInputStream(arquivo); // Lança FileNotFoundException se o arquivo não for encontrado
    }

    public int lerProxCaracter() throws IOException {
        // Lê o próximo caractere, lança IOException em caso de erro de leitura
        return is.read();
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }
}
