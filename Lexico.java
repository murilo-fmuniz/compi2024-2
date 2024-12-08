import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexico {
    private String codigoFonte;
    private List<Token> tokens;
    private List<String> erros;  // Armazena mensagens de erro para tokens não reconhecidos
    

    // Define o padrão de token
    private static final Pattern tokenPattern = Pattern.compile(
        "#.*|\"([^\"]*)\"|[a-zA-Z_][a-zA-Z0-9_]*|[0-9]+(\\.[0-9]+)?|:=|<=|>=|==|!=|\\S"
    );

    // Lista de operadores específicos
    private static final List<String> OPERADORES_ARITMETICOS = List.of("*", "/", "+", "-", "–");
    private static final List<String> OPERADORES_RELACIONAIS = List.of("<", "<=", ">", ">=", "==", "!=");
    private static final List<String> OPERADORES_BOOLEANOS = List.of("E", "OU");

    public Lexico(String codigoFonte) {
        this.codigoFonte = codigoFonte;  // Fonte
        this.tokens = new ArrayList<>();
        this.erros = new ArrayList<>();
    }

    public List<Token> analisar() {
        String[] linhas = codigoFonte.split("\n");  // Divide o código fonte por linhas para rastrear números de linha
        int lineNumber = 0;

        for (String linha : linhas) {
            lineNumber++;
            Matcher matcher = tokenPattern.matcher(linha);

            while (matcher.find()) {
                String tokenStr = matcher.group();

                try {
                    if (tokenStr.startsWith("#")) {
                        // Ignora comentários
                        break;
                    } else if (tokenStr.trim().isEmpty()) {
                        continue;  // Ignora espaços em branco
                    } else if (tokenStr.equals(":=")) {
                        tokens.add(new Token(TipoToken.Atrib, ":=", lineNumber));
                    } else if (tokenStr.equals(":")) {
                        tokens.add(new Token(TipoToken.Delim, ":", lineNumber));
                    }else if (tokenStr.startsWith("\"") && tokenStr.endsWith("\"")) {
                        // Remove as aspas duplas do token e cria o token de string
                        String conteudoString = tokenStr.substring(1, tokenStr.length() - 1);
                        tokens.add(new Token(TipoToken.String, conteudoString, lineNumber));                  
                    } else if (tokenStr.equals("(")) {
                        tokens.add(new Token(TipoToken.AbrePar, "(", lineNumber));
                    } else if (tokenStr.equals(")")) {
                        tokens.add(new Token(TipoToken.FechaPar, ")", lineNumber));
                    } else if (OPERADORES_ARITMETICOS.contains(tokenStr)) {
                        tokens.add(new Token(mapearOperadorAritmetico(tokenStr), tokenStr, lineNumber));
                    } else if (OPERADORES_RELACIONAIS.contains(tokenStr)) {
                        tokens.add(new Token(mapearOperadorRelacional(tokenStr), tokenStr, lineNumber));
                    } else if (OPERADORES_BOOLEANOS.contains(tokenStr.toUpperCase())) {  // Correspondência insensível a maiúsculas
                        tokens.add(new Token(mapearOperadorBooleano(tokenStr.toUpperCase()), tokenStr, lineNumber));
                    } else if (Character.isLetter(tokenStr.charAt(0))) {
                        Token token = identificaPalavra(tokenStr);
                        if (token.getTipo() == TipoToken.Var && !isValidVariableName(tokenStr)) {
                            throw new LexicoException("Desconhecido", tokenStr, lineNumber);
                        } else {
                            tokens.add(token);
                        }
                    } else if (Character.isDigit(tokenStr.charAt(0))) {
                        tokens.add(identificaNumero(tokenStr));
                    } else {
                        throw new LexicoException("Desconhecido", tokenStr, lineNumber);
                    }
                } catch (LexicoException e) {
                    erros.add("Erro Léxico na linha " + e.getLineNumber() + ": " + e.getMessage() + " \"" + e.getToken() + "\"");
                } catch (Exception e) {
                    erros.add("Erro inesperado na linha " + lineNumber + ": \"" + tokenStr + "\"");
                }
            }
        }
        tokens.add(new Token(TipoToken.EOF, "EOF", lineNumber));
        return tokens;
    }

    private Token identificaPalavra(String palavra) {
        TipoToken tipo = switch (palavra.toUpperCase()) {  // Correspondência de palavras-chave
            case "DEC" -> TipoToken.PCDec;
            case "INT" -> TipoToken.PCInt;
            case "REAL" -> TipoToken.PCReal;
            case "PROG" -> TipoToken.PCProg;
            case "LER" -> TipoToken.PCLer;
            case "IMPRIMIR" -> TipoToken.PCImprimir;
            case "SE" -> TipoToken.PCSe;
            case "ENTAO" -> TipoToken.PCEntao;
            case "SENAO" -> TipoToken.PCSenao;
            case "ENQTO" -> TipoToken.PCEnqto;
            case "INI" -> TipoToken.PCIni;
            case "FIM" -> TipoToken.PCFim;
            default -> TipoToken.Var;
        };
        return new Token(tipo, palavra);  // Retorna o token correspondente
    }

private Token identificaNumero(String numero) throws LexicoException {
    try {
        if (numero.matches("\\d+\\.\\d+")) { // Reconhece números no formato 'n.n'
            Double.parseDouble(numero);  // Verifica se é um decimal válido
            return new Token(TipoToken.NumReal, numero);
        } else if (numero.matches("\\d+")) { // Reconhece números inteiros
            Integer.parseInt(numero);
            return new Token(TipoToken.NumInt, numero);
        } else {
            throw new LexicoException("Número inválido", numero, -1);
        }
    } catch (NumberFormatException e) {
        throw new LexicoException("Número inválido", numero, -1);
    }
}

    private TipoToken mapearOperadorAritmetico(String operador) {
        return switch (operador) {
            case "*" -> TipoToken.OpAritMult;
            case "/" -> TipoToken.OpAritDiv;
            case "+" -> TipoToken.OpAritSoma;
            case "-", "–" -> TipoToken.OpAritSub;
            default -> null;
        };
    }

    private TipoToken mapearOperadorRelacional(String operador) {
        return switch (operador) {
            case "<" -> TipoToken.OpRelMenor;
            case "<=" -> TipoToken.OpRelMenorIgual;
            case ">" -> TipoToken.OpRelMaior;
            case ">=" -> TipoToken.OpRelMaiorIgual;
            case "==" -> TipoToken.OpRelIgual;
            case "!=" -> TipoToken.OpRelDif;
            default -> null;
        };
    }

    private TipoToken mapearOperadorBooleano(String operador) {
        return switch (operador) {
            case "E" -> TipoToken.OpBoolE;
            case "OU" -> TipoToken.OpBoolOu;
            default -> null;
        };
    }

    private boolean isValidVariableName(String tokenStr) {
        return Character.isLetter(tokenStr.charAt(0));
    }

    public void imprimeTokens() {
        for (Token token : tokens) {
            System.out.print(token + " ");
        }
        System.out.println();
        for (String erro : erros) {
            System.out.println(erro);
        }
    }
}
