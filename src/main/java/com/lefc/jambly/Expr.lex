package compilatore;

import java_cup.runtime.*;
import java.io.*;

import sym;
import static sym.*;

%%

%class ExprLex

%unicode
%line
%column
%ignorecase

// %public
%final
// %abstract

%cupsym sym
%cup
%cupdebug


%{
   StringBuilder string = new StringBuilder();
 
   private Symbol sym(int type)
   {
     return sym(type, yytext());
   }

   private Symbol sym(int type, Object value)
   {
     return new Symbol(type, yyline, yycolumn, value);
   }

   public int getLine()
   {
     return yyline+1;
   }
   
   public int getCurrentPos()
   {
      return zzCurrentPos;
   }
   
   public int getColumn()
   {
     return yycolumn;
   }

   public char[] getBuffer()
   {
     return zzBuffer;
   }

   public PrintText text(int linea) throws IOException
   {
     PrintText pt=new PrintText();
     pt.textAndLine(linea,zzBuffer);
          
     return pt;
   }
     
   public PrintText text() throws IOException 
   {
     PrintText pt=new PrintText();
     pt.textAndLine(getCurrentPos(), getColumn(), getLine(),zzBuffer); //permette di ottenere il testo e il n di linea
         //1) posizione del buffer nel momento in cui viene sollevato l'errore, 2) colonna, 3) linea, 4) buffer 
     return pt;
   } 
 
public int countBrace2=0;  
public int pos_par_open=0;  //posizione dell'ultima parentesi graffa aperta
public int pos_par_close=0;  //posizione dell'ultima parentesi graffa chiusa
public int pos_vir=0;        //posizione dell'ultimo ";"
public int get_brace()
{
  return countBrace2;
}
public int get_open_par()
{
   return pos_par_open;
}
public int get_close_par()
{
    return pos_par_close;

}
public int getPos_vir()
{

  return pos_vir;
}  
 
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]       // tutti i caratteri fatta eccezione per \n o \r

WhiteSpace = {LineTerminator} | [ \t\f]

/* I 3 differenti tipi di commenti */
Commento = {TraditionalComment} | {EndOfLineComment} | 
          {DocumentationComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/*" "*"+ [^/*] ~"*/"

/* identificatore */
Identificatore = [:jletter:][:jletterdigit:]*


/* numeri interi */
DecIntegerLiteral = 0 | [1-9][0-9]*                   // abbiamo 0 o una cifra da 1 a 9 seguita o no da una o più cifre da 0 a 9 
DecLongLiteral    = {DecIntegerLiteral} [lL]          // per un long int facciamo seguire al numero intero l o L 

    
/* numeri floating point */        
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]     // casi (3.45e5F .34e-4f 4E+7f)
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?         // casi (3.45e5 .34e-4 4E+7)

FLit1    = [0-9]+ \. [0-9]*            // 1 o più cifre da 0 a 9 seguite da . e da 0 o più cifre da 0 a 9 (i.e.  0.34 3.45 7. 5.0)
FLit2    = \. [0-9]+                        // . seguito da 1 o più cifre da 0 a 9 (i.e. .34) 
FLit3    = [0-9]+                           // 1 o più cifre da 0 a 9  (i.e. 4 o 35)
Exponent = [eE] [+-]? [0-9]+                // (i.e. e5 E6 e-4 E+7)



/* string and character literals */
StringCharacter = [^\r\n\"\\]              //qualsiasi carattere escluso \r o \n o " o \
SingleCharacter = [^\r\n\'\\]              //qualsiasi carattere escluso \r o \n o ' o \

%state STRING, CHARLITERAL  


%%


<YYINITIAL> {

  /* parole chiave scelte in base ai costrutti scelti*/
 
  "boolean"                      { return sym(BOOLEAN); }
  
  
  "class"                        { return sym(CLASS); }
  
  "do"                           { return sym(DO); }
  "double"                       { return sym(DOUBLE); }
  "else"                         { return sym(ELSE); }
  "final"                        { return sym(FINAL); }
  
  
  "for"                          { return sym(FOR); }
  
  "import"                       { return sym(IMPORT); }
  "int"                          { return sym(INT); }
  
  "new"                          { return sym(NEW); }
  "if"                           { return sym(IF); }
  "public"                       { return sym(PUBLIC); }
  
  "package"                      { return sym(PACKAGE); }
  "private"                      { return sym(PRIVATE); }
  
  "void"                         { return sym(VOID); }
  "static"                       { return sym(STATIC); }
  "while"                        { return sym(WHILE); }
  
  
    
  
  /* valori booleani */
  "true"                         { return new Symbol(sym.BOOLEAN_LITERAL, true); }
  "false"                        { return new Symbol(sym.BOOLEAN_LITERAL, false); }
  
  /* valore null */
  "null"                         { return sym(NULL_LITERAL); }
  
  
  /* separatori */
  "("                            { return sym(TONDA_APERTA); }
  ")"                            { return sym(TONDA_CHIUSA); }
  "{"                            { countBrace2++;
                                  
                                  pos_par_open=yyline+1;
                                  
                                    return sym(LBRACE); }
  "}"                            { countBrace2--;  
                                                                     
                                     pos_par_close=yyline+1; 
                                    return sym(RBRACE); }
  "["                            { return sym(LBRACK); }
  "]"                            { return sym(RBRACK); }
  ";"                            { pos_vir=yyline+1; return sym(PUNTO_E_VIRGOLA); }
  ","                            { return sym(VIRGOLA); }
  "."                            { return sym(PUNTO); }
  
  /* operatori scelti quelli utili per i nostri costrutti*/
  "="                            { return sym(EQ); }
  ">"                            { return sym(GT); }
  "<"                            { return sym(LT); }
  "=="                           { return sym(EQEQ); }
  "<="                           { return sym(LTEQ); }
  ">="                           { return sym(GTEQ); }
  "!="                           { return sym(NOTEQ); }
  "&&"                           { return sym(ANDAND); }
  "||"                           { return sym(OROR); }
  "++"                           { return sym(PLUSPLUS); }
  "--"                           { return sym(MINUSMINUS); }
  "+"                            { return sym(PIU); }
  "-"                            { return sym(MENO); }
  "*"                            { return sym(PER); }
  "/"                            { return sym(DIVISO); }
    
  
 /* simbolo che identifica l'inizio di una stringa  */
  \"                             { yybegin(STRING); string.setLength(0); }       //si passa nello stato STRING

  /* simbolo che identifica l'inizio di un carattere */
  \'                             { yybegin(CHARLITERAL); }                      //si passa nello stato CHARLITERAL


  /* numeri */

  /* This is matched together with the minus, because the number is too big to 
     be represented by a positive integer. */
  "-2147483648"                  { return new Symbol(sym.INTEGER_LITERAL, new Integer(Integer.MIN_VALUE)); }
  
  {DecIntegerLiteral}            { return new Symbol(sym.INTEGER_LITERAL, new Integer(yytext())); }
  {DecLongLiteral}               { return new Symbol(sym.INTEGER_LITERAL, new Long(yytext().substring(0,yylength()-1))); } //substring che     
                                                                                                                   //elimina l o L
  
  {FloatLiteral}                 { return new Symbol(sym.FLOATING_POINT_LITERAL, new Float(yytext().substring(0,yylength()-1))); }
  {DoubleLiteral}                { return new Symbol(sym.FLOATING_POINT_LITERAL, new Double(yytext())); }
  {DoubleLiteral}[dD]            { return new Symbol(sym.FLOATING_POINT_LITERAL, new Double(yytext().substring(0,yylength()-1))); }
  
  /* commenti */
  {Commento}                      { }            //in caso di commento non viene eseguita alcuna azione

  /* whitespace */
  {WhiteSpace}                    { }            //in caso di spazio bianco non viene eseguita alcuna azione

  /* identifiers */ 
  {Identificatore}                   { return new Symbol(sym.IDENTIFICATORE, yytext()); }  //viene passato il il tipo di Token e il lessema letto

}

<STRING> {

  /* individuazione di " dopo aver individuato il primo " nello stato YYINITIAL  */
  \"                             { yybegin(YYINITIAL); return new Symbol(sym.STRING_LITERAL, string.toString()); }  //passaggio nuovamente nello 
                                                                                                            //stato YYINITIAL e stampa 
  
  
  /* i singoli caratteri vengono ripetuti 1 o più volte */ 
  {StringCharacter}+             { string.append( yytext() ); }   //stringa che si viene a creare è posta in append
  
  /* escape sequences presenti all'interno di una stringa e posti in append*/
  "\\b"                          { string.append( '\b' ); }
  "\\t"                          { string.append( '\t' ); }
  "\\n"                          { string.append( '\n' ); }
  "\\f"                          { string.append( '\f' ); }
  "\\r"                          { string.append( '\r' ); }
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }
 
  
  /* casi di errori nelle stringhe */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

<CHARLITERAL> {
  {SingleCharacter}\'            { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, yytext().charAt(0)); }
  
  /* escape sequences */
  "\\b"\'                        { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, '\b');}
  "\\t"\'                        { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, '\t');}
  "\\n"\'                        { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, '\n');}
  "\\f"\'                        { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, '\f');}
  "\\r"\'                        { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, '\r');}
  "\\\""\'                       { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, '\"');}
  "\\'"\'                        { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, '\'');}
  "\\\\"\'                       { yybegin(YYINITIAL); return new Symbol(sym.CHARACTER_LITERAL, '\\'); }
 

  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated character literal at end of line"); }
}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+yyline+", column "+yycolumn); }
<<EOF>>                          { return new Symbol(sym.EOF); }



