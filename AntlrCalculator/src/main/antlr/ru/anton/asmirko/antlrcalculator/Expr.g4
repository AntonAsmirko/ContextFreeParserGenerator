grammar Expr;

prog: stat+
    ;

stat: ID '=' expr LINE_END #assign
    ;

expr: expr op=(MUL|DIV) expr #MullDiv
    | expr op=(ADD|SUB) expr #AddSub
    | INT                 #int
    | ID                  #id
    | '(' expr ')'        #parens
    ;

ID : [a-z]+ ;
INT : '-'?[1-9][0-9]* ;
WS : [ \t\n\r]+ -> skip ;

MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
LINE_END : ';' ;
