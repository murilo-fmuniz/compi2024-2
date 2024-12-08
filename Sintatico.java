import java.util.*;

public class Sintatico {
    private List<Token> tokens;
    private int posicaoAtual;
    private Set<String> tabelaSimbolos;  // Tabela de símbolos para variáveis declaradas

    public Sintatico(List<Token> tokens) {
        this.tokens = tokens;
        this.posicaoAtual = 0;
        this.tabelaSimbolos = new HashSet<>();
    }

    // Método principal
    public void analisar() {
        programa();
        consome(TipoToken.EOF);  // Verifica se o programa termina corretamente
        System.out.println("Análise sintática concluída com sucesso.");
    }

    // Gramática: Programa → ':' 'DEC' ListaDeclaracoes ':' 'PROG' ListaComandos;
    private void programa() {
        consome(TipoToken.Delim);   // ':'
        System.out.println("Leu : ");
        consome(TipoToken.PCDec);   // 'DEC'
        System.out.println("Leu DEC ");
        listaDeclaracoes();         // Declaração de variáveis
        System.out.println("Leu DECLARACOES ");
        consome(TipoToken.Delim);   // ':'
        System.out.println("Leu : ");
        consome(TipoToken.PCProg);  // 'PROG'
        System.out.println("Leu PROG ");
        listaComandos();            // Comandos
    }

    // === DECLARAÇÕES DE VARIÁVEIS ===
    private void listaDeclaracoes() {
        declaracao();
        listaDeclaracoes2();
    }

    private void listaDeclaracoes2() {
        if (verificaToken(TipoToken.Var)) {
            listaDeclaracoes();
        }
    }

    private void declaracao() {
        Token varToken = consome(TipoToken.Var);  // VARIAVEL
        consome(TipoToken.Delim);                 // ':'
        tipoVar();                                // INT ou REAL
        
        if (!tabelaSimbolos.add(varToken.getLexema())) {
            erro("Variável já declarada: " + varToken.getLexema());
        }
    }

    private void tipoVar() {
        if (verificaToken(TipoToken.PCInt)) {
            consome(TipoToken.PCInt);
        } else if (verificaToken(TipoToken.PCReal)) {
            consome(TipoToken.PCReal);
        } else {
            erro("Esperado 'INT' ou 'REAL'.");
        }
    }

    // === LISTA DE COMANDOS ===
    private void listaComandos() {
        comando();
        listaComandos2();
    }

    private void listaComandos2() {
        if (verificaComando()) {
            listaComandos();
        }
    }

    private boolean verificaComando() {
        return verificaToken(TipoToken.Var) ||         // ComandoAtribuicao: VARIAVEL ':=' ExpressaoAritmetica
               verificaToken(TipoToken.PCLer) ||      // ComandoEntrada: 'LER' VARIAVEL
               verificaToken(TipoToken.PCImprimir) || // ComandoSaida: 'IMPRIMIR' (VARIAVEL | CADEIA)
               verificaToken(TipoToken.PCSe) ||       // ComandoCondicao: 'SE' ExpressaoRelacional 'ENTAO' Comando
               verificaToken(TipoToken.PCEnqto) ||    // ComandoRepeticao: 'ENQTO' ExpressaoRelacional Comando
               verificaToken(TipoToken.PCIni);        // SubAlgoritmo: 'INI' ListaComandos 'FIM'
    }
    

    private void comando() {
        if (verificaToken(TipoToken.Var)) {
            System.out.println("Atribuindo\n");
            comandoAtribuicao();
        } else if (verificaToken(TipoToken.PCLer)) {
            System.out.println("Lendo\n");
            comandoEntrada();
        } else if (verificaToken(TipoToken.PCImprimir)) {
            System.out.println("Imprimindo\n");
            comandoSaida();
        } else if (verificaToken(TipoToken.PCSe)) {
            System.out.println("SENO\n");
            comandoCondicao();
        } else if (verificaToken(TipoToken.PCEnqto)) {
            System.out.println("COSENO\n");
            comandoRepeticao();
        } else if (verificaToken(TipoToken.PCIni)) {
            System.out.println("Inicial\n");
            subAlgoritmo();
        } else {
            erro("Comando inválido.");
        }
    }

    private void comandoAtribuicao() {
        Token varToken = consome(TipoToken.Var);  // VARIAVEL
        verificaVariavelDeclarada(varToken);
        consome(TipoToken.Atrib);                // ':='
        expressaoAritmetica();                   // ExpressaoAritmetica
    }

    private void comandoEntrada() {
        consome(TipoToken.PCLer);  // 'LER'
        Token varToken = consome(TipoToken.Var); // VARIAVEL
        verificaVariavelDeclarada(varToken);
    }

    private void comandoSaida() {
        consome(TipoToken.PCImprimir);  // 'IMPRIMIR'
        comandoSaida2();
    }

    private void comandoSaida2() {
        if (verificaToken(TipoToken.Var)) {
            Token varToken = consome(TipoToken.Var);
            verificaVariavelDeclarada(varToken);
        } else if (verificaToken(TipoToken.Cadeia)) {
            consome(TipoToken.Cadeia);
        } else {
            erro("Esperado variável ou cadeia.");
        }
    }

    private void comandoCondicao() {
        consome(TipoToken.PCSe);                    // 'SE'
        expressaoRelacional();                     // ExpressaoRelacional
        consome(TipoToken.PCEntao);                // 'ENTAO'
        comando();                                 // Comando
        comandoCondicao2();
    }

    private void comandoCondicao2() {
        if (verificaToken(TipoToken.PCSenao)) {
            consome(TipoToken.PCSenao);            // 'SENAO'
            comando();
        }
    }

    private void comandoRepeticao() {
        consome(TipoToken.PCEnqto);                // 'ENQTO'
        expressaoRelacional();                    // ExpressaoRelacional
        comando();
    }

    private void subAlgoritmo() {
        consome(TipoToken.PCIni);                  // 'INI'
        listaComandos();                          // ListaComandos
        consome(TipoToken.PCFim);                 // 'FIM'
    }

    // === EXPRESSÕES ===
    private void expressaoAritmetica() {
        termoAritmetico();
        expressaoAritmetica2();
    }

    private void expressaoAritmetica2() {
        if (verificaToken(TipoToken.OpAritSoma) || verificaToken(TipoToken.OpAritSub)) {
            consome(tokens.get(posicaoAtual).getTipo());
            expressaoAritmetica();
        }
    }

    private void termoAritmetico() {
        fatorAritmetico();
        termoAritmetico2();
    }

    private void termoAritmetico2() {
        if (verificaToken(TipoToken.OpAritMult) || verificaToken(TipoToken.OpAritDiv)) {
            consome(tokens.get(posicaoAtual).getTipo());
            termoAritmetico();
        }
    }

    private void fatorAritmetico() {
        if (verificaToken(TipoToken.NumInt) || verificaToken(TipoToken.NumReal) || verificaToken(TipoToken.Var)) {
            consome(tokens.get(posicaoAtual).getTipo());
        } else if (verificaToken(TipoToken.AbrePar)) {
            consome(TipoToken.AbrePar);    // '('
            expressaoAritmetica();
            consome(TipoToken.FechaPar);  // ')'
        } else {
            erro("Esperado número, variável ou parênteses.");
        }
    }

    private void expressaoRelacional() {
        termoRelacional();
        expressaoRelacional2();
    }

    private void expressaoRelacional2() {
        if (verificaToken(TipoToken.OpBoolE) || verificaToken(TipoToken.OpBoolOu)) {
            consome(tokens.get(posicaoAtual).getTipo());
            termoRelacional();
        }
    }

    private void termoRelacional() {
        expressaoAritmetica();
        if (verificaToken(TipoToken.OpRelMenor) || verificaToken(TipoToken.OpRelMaior) 
            || verificaToken(TipoToken.OpRelIgual) || verificaToken(TipoToken.OpRelDif)) {
            consome(tokens.get(posicaoAtual).getTipo());
            expressaoAritmetica();
        }
    }

    // === MÉTODOS AUXILIARES ===
    private Token consome(TipoToken tipoEsperado) {
        if (verificaToken(tipoEsperado)) {
            return tokens.get(posicaoAtual++);
        } else {
            erro("Token inesperado. Esperado: " + tipoEsperado);
            return null;
        }
    }

    private boolean verificaToken(TipoToken tipoEsperado) {
        return posicaoAtual < tokens.size() && tokens.get(posicaoAtual).getTipo().equals(tipoEsperado);
    }

    private void verificaVariavelDeclarada(Token token) {
        if (!tabelaSimbolos.contains(token.getLexema())) {
            erro("Variável não declarada: " + token.getLexema());
        }
    }

    private void erro(String mensagem) {
        Token tokenErro = posicaoAtual < tokens.size() ? tokens.get(posicaoAtual) : null;
        System.err.printf("Erro na linha %d: %s. Encontrado '%s'.%n",
            tokenErro.getLinha(), mensagem, tokenErro.getLexema());
        System.exit(1);
    }
}
