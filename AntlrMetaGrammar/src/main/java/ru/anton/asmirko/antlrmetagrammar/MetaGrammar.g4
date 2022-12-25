grammar MetaGrammar;

rules : rule+ #rulesNode
      ;
rule : NAME COLON rule_rhs CODE? (OR_SYMBOL rule_rhs CODE?)* SEMICOLON  #ruleNode
     ;
rule_rhs : (TERMINAL|NAME|REGEX)+ #rhsRuleNode
         | EPS #epsRhs
         ;

NAME: [A-Z]+;
TERMINAL : STRING;
STRING : QUOT .*? QUOT;
CODE: BRACKET_OPEN .*? BRACKET_CLOSE;

EPS: 'ε';
OR_SYMBOL: '|';
COLON: ':';
SEMICOLON: ';' ;
BRACKET_OPEN: '{';
BRACKET_CLOSE: '}';
QUOT : '\'';
REGEX : '/'.*?'/';

WHITESPACE: [ \t\r\n]+ -> skip;