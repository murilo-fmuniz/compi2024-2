public class LexicoException extends Exception {
    private final String token;     // O token que causou o erro
    private final int lineNumber;   // O número da linha onde ocorreu o erro

    // Construtor
    public LexicoException(String message, String token, int lineNumber) {
        super(message);  // Passa a mensagem de erro para a superclasse Exception
        this.token = token;
        this.lineNumber = lineNumber;
    }

    // Getter para o token
    public String getToken() {
        return token;
    }

    // Getter para o número da linha
    public int getLineNumber() {
        return lineNumber;
    }

    // Formato de mensagem de erro personalizado
    @Override
    public String toString() {
        return "Erro Léxico na linha " + lineNumber + ": " + getMessage() + " \"" + token + "\"";
    }
}
