# Вариант 11 — transpose [Java]
Утилита командной строки "transpose". 
Транспонирует текст, то есть столбцы слов превращает в строки, и наоборот. 

**Доступные параметры:**
+ `inputFile` задаёт имя входного файла. При отсутствии параметра текст считывается с консоли.
+ `-o outputFile` задаёт имя выходного файла. При отсутствии параметра текст выводится на консоль.
+ `-a num` задаёт ширину "поля" для каждого слова в выходном файле. 
Если длина слова меньше `num`, оставшееся место заполняется пробелами.
+ `-t` обрезает слова, если их длина больше, чем `num` символов (см. `-a num`).
Если флаг `-a` остутствует, считается, что установлен флаг `-a 10`.
+ `-r` выравнивает слова по правому краю в пределах "поля" (см. `-a num`).
Если флаг `-a` остутствует, считается, что установлен флаг `-a 10`.
Если флаг `-r` не указан, слова выравниваются по левому краю.

Пустые строки не обрабатываются.

**Использование:** `transpose [-a num] [-t] [-r] [-o outputFile] [inputFile]`