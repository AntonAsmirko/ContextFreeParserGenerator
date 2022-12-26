#Лабораторная работа №4
####Необходимо написать некоторый упрощенный аналог генератора трансляторов. Рекомендуется брать за основу синтаксис ANTLR или Bison. Рекомендуется для чтения входного файла с грамматикой сгенерировать разборщик с помощью ANTLR или Bison.

Необходимо набрать в сумме хотя бы 35 баллов.

Обязательное требование: должен быть лексический анализатор, не должно быть ограничения, что токен это один символ.

Необходимо из каждого пункта выполнить хотя бы 1 вариант.

Выбор класса грамматик

####(10 баллов) LL(1)-грамматики, нисходящий разбор
####(15 баллов) SLR-грамматики, восходящий разбор
####(20 баллов) LALR-грамматики, восходящий разбор
Работа с атрибутами

####(10 баллов) поддержка синтезируемых атрибутов
####(10 баллов) поддержка наследуемых атрибутов
Тестирование получившегося генератора

####(обязательно) сгенерировать с помощью вашего генератора калькулятор
####(5 баллов) выполнить с помощью вашего генератора ваше задание второй лабораторной
####(10 баллов) выполнить с помощью вашего генератора ваше задание третьей лабораторной

# Ход работы

### Разрабодал свой формат грамматик с помощью ANTLR4
код g4 с её описанием:

    grammar MetaGrammar;
    
    rules : rule+ #rulesNode
    ;
    rule : NAME COLON rule_rhs (OR_SYMBOL rule_rhs)* SEMICOLON  #ruleNode
    ;
    rule_rhs : (TERMINAL|NAME|REGEX)+ CODE? #rhsRuleNode
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
    BRACKET_OPEN: '-{';
    BRACKET_CLOSE: '}-';
    QUOT : '\'';
    REGEX : '/'.*?'/';
    
    WHITESPACE: [ \t\r\n]+ -> skip;

пример инстанса моего формата:

    E : T X -{ children: List<String>, value: String -> val childrenD = children.map{ it.toDouble() }; (childrenD[0] + (if(childrenD.subList(1, childrenD.size).isNotEmpty()) childrenD.subList(1, childrenD.size).reduce{acc, i -> acc + i} else 0.0)).toString()}- ;
    X : '+' T X -{ children: List<String>, value: String -> val childrenD = children.map{ it.toDouble() }; (childrenD[0] + (if(childrenD.subList(1, childrenD.size).isNotEmpty()) childrenD.subList(1, childrenD.size).reduce{acc, i -> acc + i} else 0.0)).toString()}-
    | '-' T X -{ children: List<String>, value: String -> val childrenD = children.map{ it.toDouble() }; (-1.0 * childrenD[0] + (if(childrenD.subList(1, childrenD.size).isNotEmpty()) childrenD.subList(1, childrenD.size).reduce{acc, i -> acc + i} else 0.0)).toString()}-
    | ε
    ;
    T : F Y -{ children: List<String>, value: String -> val childrenD = children.map{ it.toDouble() }; (childrenD[0] * (if(childrenD.subList(1, childrenD.size).isNotEmpty()) childrenD.subList(1, childrenD.size).reduce{acc, i -> acc * i} else 1.0)).toString()}- ;
    Y : '*' F Y -{ children: List<String>, value: String -> val childrenD = children.map{ it.toDouble() }; (childrenD[0] * (if(childrenD.subList(1, childrenD.size).isNotEmpty()) childrenD.subList(1, childrenD.size).reduce{acc, i -> acc * i} else 1.0)).toString()}-
    | '/' F Y -{ children: List<String>, value: String -> val childrenD = children.map{ it.toDouble() }; (1.0 / childrenD[0] * (if(childrenD.subList(1, childrenD.size).isNotEmpty()) childrenD.subList(1, childrenD.size).reduce{acc, i -> acc * i} else 1.0)).toString()}-
    | ε
    ;
    F : /-?[1-9][0-9]*(\.[0-9]*)?/ -{ children: List<String>, value: String -> children[0] }-
    | '(' E ')' -{ children: List<String>, value: String -> children[0].toString()}-
    ;

это грамматика для арифметических выражений.

Для генерации парсера использовал LL(1) алгоритм