public enum TipoToken { 
    // Palavras-chave (PC - Palavra-chave)
    PCDec,         // Palavra-chave para "DEC" (declaração)
    PCProg,        // Palavra-chave para "PROG" (início do programa)
    PCInt,         // Palavra-chave para "INT" (tipo inteiro)
    PCReal,        // Palavra-chave para "REAL" (tipo real/flutuante)
    PCLer,         // Palavra-chave para "LER" (operação de leitura)
    PCImprimir,    // Palavra-chave para "IMPRIMIR" (operação de impressão)
    PCSe,          // Palavra-chave para "SE" (condição if)
    PCEntao,       // Palavra-chave para "ENTAO" (então em instruções if)
    PCSenao,       // Palavra-chave para "SENAO" (senão em instruções if)
    PCEnqto,       // Palavra-chave para "ENQTO" (laço while)
    PCIni,         // Palavra-chave para "INI" (início de bloco)
    PCFim,         // Palavra-chave para "FIM" (fim de bloco)

    // Operadores Aritméticos (OpArit)
    OpAritMult,    // Operador de multiplicação "*"
    OpAritDiv,     // Operador de divisão "/"
    OpAritSoma,    // Operador de adição "+"
    OpAritSub,     // Operador de subtração "-"

    // Operadores Relacionais (OpRel)
    OpRelMenor,         // Operador de menor que "<"
    OpRelMenorIgual,    // Operador de menor ou igual "<="
    OpRelMaior,         // Operador de maior que ">"
    OpRelMaiorIgual,    // Operador de maior ou igual ">="
    OpRelIgual,         // Operador de igualdade "=="
    OpRelDif,           // Operador de desigualdade "!="

    // Operadores Booleanos
    OpBoolE,        // Operador lógico E "E"
    OpBoolOu,       // Operador lógico OU "OU"

    // Delimitador
    Delim,          // Delimitador ":"

    // Atribuição
    Atrib,          // Operador de atribuição ":="

    // Parênteses
    AbrePar,        // Parêntese aberto "("
    FechaPar,       // Parêntese fechado ")"
    
    //Aspas
    String,

    EXPRESSAO_RELACIONAL,

    // Identificadores e Números
    Var,            // Variável (identificador)
    NumInt,         // Número inteiro
    NumReal,        // Número real (ponto flutuante)

    // Cadeia (para literais de texto ou strings)
    Cadeia,         // Literal de string ou cadeia de caracteres

    tipoEsperado,

    // Fim de Arquivo
    EOF             // Marcador de fim de arquivo
}
