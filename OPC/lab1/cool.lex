package src.PA2J;
import java_cup.runtime.Symbol;

%%

%{
    static int MAX_STR_CONST = 1024;
    StringBuffer string_buf = new StringBuffer();

    int get_curr_lineno() {
        return yyline + 1;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
        filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
        return filename;
    }

	public String unescapeJavaString(String st) {
		StringBuilder sb = new StringBuilder(st.length());
		for (int i = 0; i < st.length(); i++) {
			char ch = st.charAt(i);
			if (ch == '\\') {
				char nextChar = (i == st.length() - 1) ? '\\' : st.charAt(i + 1);

				switch (nextChar) {
					case '\\':
						ch = '\\';
						break;
					case 'b':
						ch = '\b';
						break;
					case 'f':
						ch = '\f';
						break;
					case 'n':
						ch = '\n';
						break;
					case 'r':
						ch = '\r';
						break;
					case 't':
						ch = '\t';
						break;
					case '\"':
						ch = '\"';
						break;
					case '\'':
						ch = '\'';
						break;
					default:
						ch = nextChar;
				}
				i++;
			}
			sb.append(ch);
		}
		return sb.toString();
	}
%}

%eofval{
    switch(yy_lexical_state) {
        case YYINITIAL:
            break;
        case COMMENT:
            yybegin(ERROR);
            return new Symbol(TokenConstants.ERROR, "EOF inside comment");
        case STRING_LITERAL:
            yybegin(ERROR);
            return new Symbol(TokenConstants.ERROR, "EOF inside string literal");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%type java_cup.runtime.Symbol
%cup
%line
%state COMMENT
%state STRING_LITERAL
%state ERROR

WHITESPACE = [ \n\f\r\t\v]+
EOL = [\n]
NUMBER = [0-9]+

KEYWORDS = "class"|"else"|"false"|"fi"|"if"|"in"|"inherits"|"isvoid"|"let"|"loop"|"pool"|"then"|"while"|"case"|"esac"|"new"|"of"|"not"|"true"

OBJECT_ID = [a-z][a-zA-Z0-9_]*
TYPE_ID = [A-Z][a-zA-Z0-9_]*

OPERANDS = ("+"|"-"|"*"|"/"|"="|"~")
SYMBOLS = ("{"|"}"|"@"|":"|";"|"("|")"|"=>"|"<="|"<-"|"."|","|"<")

%%

<YYINITIAL> {WHITESPACE} { }

<YYINITIAL> {KEYWORDS} {
    String upperCased = yytext().toUpperCase();
    switch(upperCased) {
        case "CLASS":
            return new Symbol(TokenConstants.CLASS);
        case "LET":
            return new Symbol(TokenConstants.LET);
        case "NEW":
            return new Symbol(TokenConstants.NEW);
        case "IF":
            return new Symbol(TokenConstants.IF);
        case "THEN":
            return new Symbol(TokenConstants.THEN);
        case "ELSE":
            return new Symbol(TokenConstants.ELSE);
        case "CASE":
            return new Symbol(TokenConstants.CASE);
        case "WHILE":
            return new Symbol(TokenConstants.WHILE);
        case "LOOP":
            return new Symbol(TokenConstants.LOOP);
        case "POOL":
            return new Symbol(TokenConstants.POOL);
        case "IN":
            return new Symbol(TokenConstants.IN);
        case "OF":
            return new Symbol(TokenConstants.OF);
        case "NOT":
            return new Symbol(TokenConstants.NOT);
        case "TRUE":
            if (yytext().charAt(0) == 'T') return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
            return new Symbol(TokenConstants.BOOL_CONST, "true");
        case "FALSE":
            if (yytext().charAt(0) == 'F') return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
            return new Symbol(TokenConstants.BOOL_CONST, "false");
        case "INHERITS":
            return new Symbol(TokenConstants.INHERITS);
        case "ISVOID":
            return new Symbol(TokenConstants.ISVOID);
        case "ESAC":
            return new Symbol(TokenConstants.ESAC);
        case "FI":
            return new Symbol(TokenConstants.FI);
    }
}

<YYINITIAL> {NUMBER} {
	return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}

<YYINITIAL> {OBJECT_ID} {
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}

<YYINITIAL> {TYPE_ID} {
    return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}

<YYINITIAL> "--".*{EOL} { }

<YYINITIAL> "(*" {
    yybegin(COMMENT);
}

<COMMENT> "*)" {
    yybegin(YYINITIAL);
}

<COMMENT> .|{EOL} {}

<YYINITIAL> {SYMBOLS} {
    String symbol = yytext();
    switch(symbol) {
        case "{":
            return new Symbol(TokenConstants.LBRACE);
        case "}":
            return new Symbol(TokenConstants.RBRACE);
        case "@":
            return new Symbol(TokenConstants.AT);
        case ":":
            return new Symbol(TokenConstants.COLON);
        case ";":
            return new Symbol(TokenConstants.SEMI);
        case "(":
            return new Symbol(TokenConstants.LPAREN);
        case ")":
            return new Symbol(TokenConstants.RPAREN);
        case "=>":
            return new Symbol(TokenConstants.DARROW);
        case "<=":
            return new Symbol(TokenConstants.LE);
        case "<-":
            return new Symbol(TokenConstants.ASSIGN);
        case ".":
            return new Symbol(TokenConstants.DOT);
        case ",":
            return new Symbol(TokenConstants.COMMA);
        case "<":
            return new Symbol(TokenConstants.LT);
    }
}

<YYINITIAL> {OPERANDS} {
    String symbol = yytext();
    switch(symbol) {
        case "+":
            return new Symbol(TokenConstants.PLUS);
        case "-":
            return new Symbol(TokenConstants.MINUS);
        case "*":
            return new Symbol(TokenConstants.MULT);
        case "/":
            return new Symbol(TokenConstants.DIV);
        case "=":
            return new Symbol(TokenConstants.EQ);
        case "~":
            return new Symbol(TokenConstants.NEG);
    }
}

<YYINITIAL> \" {
    string_buf.setLength(0);
    yybegin(STRING_LITERAL);
}

<STRING_LITERAL> \" {
    yybegin(YYINITIAL);
    if (string_buf.length() > MAX_STR_CONST) {
        yybegin(ERROR);
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
    return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
}
    
<STRING_LITERAL> {EOL} {
    if (string_buf.charAt(string_buf.length() - 1) != '\\') {
        yybegin(ERROR);
        return new Symbol(TokenConstants.ERROR, "Unexpected EOL");
    }
    string_buf.deleteCharAt(string_buf.length() - 1);
    string_buf.append(yytext());
}

<STRING_LITERAL> [^\"\n]* {
    String match = yytext();
	if (match.contains("\\0")) {
		yybegin(ERROR);
		return new Symbol(TokenConstants.ERROR, "String contains null character");
	}
	string_buf.append(unescapeJavaString(match));
}

<YYINITIAL> . {
    yybegin(ERROR);
    return new Symbol(TokenConstants.ERROR, "Unexpected char '" + yytext() + "'");
}

<ERROR> [^]* { }
