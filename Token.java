public class Token {
    private TipoToken tipo;
    private String lexema;
    private int linha;

    public Token(TipoToken tipo, String lexema, int linha) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
    }

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }


    public TipoToken getTipo() {
        return tipo;
    }

    public String getTipoString(){
        return tipo.toString();
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinha() {
        return linha;
    }

    @Override
    public String toString() {
        return "<" + tipo.name() + ", \"" + lexema + "\", " + linha + ">";
    }
}
