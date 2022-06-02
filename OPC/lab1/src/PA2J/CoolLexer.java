package src.PA2J;
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int ERROR = 3;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int STRING_LITERAL = 2;
	private final int yy_state_dtrans[] = {
		0,
		27,
		15,
		18
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NOT_ACCEPT,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NOT_ACCEPT,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"24:9,25,26,24,25,1,24:18,25,24,35,24:5,27,29,28,34,30,23,30,34,19:10,30:2,3" +
"3,31,32,24,30,22:26,24:4,21,24,4,20,2,15,6,7,20,10,8,20:2,3,20,9,14,16,20,1" +
"1,5,12,18,13,17,20:3,30,24,30,34,24,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,67,
"0,1,2,3,4,1,5,6,7,1,8,1:4,9,1:2,10,11,12,13,1:2,14,15,16,17,18,19,20,21,22," +
"23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47," +
"48,49,50,8,51,52,53,54,55")[0];

	private int yy_nxt[][] = unpackFromString(56,36,
"1,2,3,41,61:2,62,21,28,43,61:2,63,20,31,61,64,65,61,4,61,5,6,7,5,2:2,8,22,2" +
"3:2,29,5,30,22,9,-1:37,2,-1:11,2,-1:11,2:2,-1:11,61,66,44,61:18,-1:32,4,-1:" +
"18,6:21,-1:36,19,-1:40,11,-1:9,61:21,-1:13,1,25:25,16,25:8,17,1,26:35,-1:2," +
"19:24,12,19:9,-1,2,61:11,20,61:9,-1:2,2:2,-1:11,61:2,47,61:3,10,61:14,-1:42" +
",14,-1:7,25:25,-1,25:8,-1:2,26:35,1,-1,13:26,24,13:7,-1:2,61:3,48,61,10,61," +
"42,61:13,-1:45,23,-1:26,23,-1:7,23,-1:6,61:5,10,61:15,-1:15,61:10,10,61:10," +
"-1:15,61:15,10,61:5,-1:15,61:4,10,61:16,-1:15,61:14,10,61:6,-1:15,10,61:20," +
"-1:15,61:7,10,61:13,-1:15,61,10,61:19,-1:15,61:3,10,61:17,-1:15,61:13,10,61" +
":7,-1:15,61:4,32,61:7,45,61:8,-1:15,61:8,55,61:12,-1:15,61:4,33,61:7,32,61:" +
"8,-1:15,61:3,34,61:17,-1:15,61:12,35,61:8,-1:15,61:2,36,61:18,-1:15,61,44,6" +
"1:19,-1:15,61:11,54,61:9,-1:15,61:4,37,61:16,-1:15,61:16,34,61:4,-1:15,61:1" +
"2,38,61:8,-1:15,61:6,56,61:14,-1:15,61:3,39,61:17,-1:15,61:12,57,61:8,-1:15" +
",61:4,58,61:16,-1:15,61,34,61:19,-1:15,61:6,40,61:14,-1:15,61:9,59,61:11,-1" +
":15,61:6,60,61:14,-1:15,61:10,39,61:10,-1:15,61,44,61,46,61:17,-1:15,61:8,4" +
"9,50,61:11,-1:15,61:12,51,61:8,-1:15,61:8,52,61:12,-1:15,61:2,53,61:18,-1:1" +
"3");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ }
					case -3:
						break;
					case 3:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -4:
						break;
					case 4:
						{
	return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}
					case -5:
						break;
					case 5:
						{
    yybegin(ERROR);
    return new Symbol(TokenConstants.ERROR, "Unexpected char '" + yytext() + "'");
}
					case -6:
						break;
					case 6:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -7:
						break;
					case 7:
						{
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
					case -8:
						break;
					case 8:
						{
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
					case -9:
						break;
					case 9:
						{
    string_buf.setLength(0);
    yybegin(STRING_LITERAL);
}
					case -10:
						break;
					case 10:
						{
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
					case -11:
						break;
					case 11:
						{
    yybegin(COMMENT);
}
					case -12:
						break;
					case 12:
						{ }
					case -13:
						break;
					case 13:
						{}
					case -14:
						break;
					case 14:
						{
    yybegin(YYINITIAL);
}
					case -15:
						break;
					case 15:
						{
    String match = yytext();
	if (match.contains("\\0")) {
		yybegin(ERROR);
		return new Symbol(TokenConstants.ERROR, "String contains null character");
	}
	string_buf.append(unescapeJavaString(match));
}
					case -16:
						break;
					case 16:
						{
    if (string_buf.charAt(string_buf.length() - 1) != '\\') {
        yybegin(ERROR);
        return new Symbol(TokenConstants.ERROR, "Unexpected EOL");
    }
    string_buf.deleteCharAt(string_buf.length() - 1);
    string_buf.append(yytext());
}
					case -17:
						break;
					case 17:
						{
    yybegin(YYINITIAL);
    if (string_buf.length() > MAX_STR_CONST) {
        yybegin(ERROR);
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
    return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
}
					case -18:
						break;
					case 18:
						{ }
					case -19:
						break;
					case 20:
						{ }
					case -20:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -21:
						break;
					case 22:
						{
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
					case -22:
						break;
					case 23:
						{
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
					case -23:
						break;
					case 24:
						{}
					case -24:
						break;
					case 25:
						{
    String match = yytext();
	if (match.contains("\\0")) {
		yybegin(ERROR);
		return new Symbol(TokenConstants.ERROR, "String contains null character");
	}
	string_buf.append(unescapeJavaString(match));
}
					case -25:
						break;
					case 26:
						{ }
					case -26:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -27:
						break;
					case 29:
						{
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
					case -28:
						break;
					case 30:
						{
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
					case -29:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -30:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -31:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -32:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -33:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -34:
						break;
					case 36:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -35:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -36:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -37:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -38:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -39:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -40:
						break;
					case 42:
						{
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
					case -41:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -42:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -43:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -44:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -45:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -46:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -47:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -48:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -49:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -50:
						break;
					case 52:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -51:
						break;
					case 53:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -52:
						break;
					case 54:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -53:
						break;
					case 55:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -54:
						break;
					case 56:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -55:
						break;
					case 57:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -56:
						break;
					case 58:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -57:
						break;
					case 59:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -58:
						break;
					case 60:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -59:
						break;
					case 61:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -60:
						break;
					case 62:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -61:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -62:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -63:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -64:
						break;
					case 66:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -65:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
