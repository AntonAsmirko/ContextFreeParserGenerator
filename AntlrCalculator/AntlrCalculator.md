# Лабараторная работа №3
## Вариант 2. Арифметические выражения
Вычисление арифметических выражений с заведением переменных.
В результате трансляции должно вычисляться значение выражений, в выражении допускается присваивание значений переменных. Исполь- зуйте целочисленные переменные. 
<br>
Пример:

    a = 2;
    b = a + 2;
    c = a + b * (b - 3);
    a = 3;
    c = a + b * (b - 3);

Вывод:

    a=2
    b=4
    c=6
    a=3
    c=7

## Ход работы
Для генерации парсера использовал ANTLR4, к парсеру генерирую Visitor, 
в котором перевожу дерево из ANTLR4 в самописное дерево. Расширил функционал классов самописного дерева, теперь оно
может хранить функции, которые вызываются, когда мы выходим из вершины (АТГ),
они используются для генерации вывода.