# Author = Veselov Fedor Evgenievich
# Group = P3132
# Date = 23.10.2025
# 501787 % 3 = 1

import re

print("Write your text: ")
print("---------------------------")
print(" 1)Number of case         |")
print(" 2)Text:                  |")
print("      < Your text >       |")
print(" to finish input write *  |")
print("---------------------------")

with open('Tests_for_2/input_6.txt', 'w', encoding='utf-8') as file:
    new_input = str(input())
    while(new_input != '*'):
        file.write(new_input + '\n')
        new_input = str(input())


for test_num in range(1,7):
    print("\n\n --------- Test №" + str(test_num) + " --------- ")
    with open('Tests_for_2/input_' + str(test_num) +'.txt', 'r', encoding='utf-8') as file:
        case = file.readline()
        text = file.read()

    print(text)
    words = text.split()
    first_str = re.findall(r'\d', case)
    if first_str == [] or len(first_str) == 1:
        print("wrong input of number of case")
        break
    word_numb = int(first_str[1])
    print("Номер шаблона падежа: " + str(word_numb))

    patern = r'\w(\w+)(ый|ий|ой|ая|яя|ое|ее|ые|ие|ого|его|ой|ей|ому|ему|ую|юю|ым|им|ыми|ими|ом|ем|ых|их)'
    words = re.findall(patern, text)
    words = [(word.lower(), ending.lower()) for word, ending in words]
    roots = [it[0] for it in words]

    changes = []
    for i in words:
        root_i = i[0]
        if roots.count(root_i) > 2:
            changes.append(i)

    if changes == []:
        print(text)
        break
    if word_numb <= 0:
        print("wrong input number of case")
        break
    form = changes[word_numb - 1][1]
    unic = []
    for i in changes:
        if not i[0] in unic:
            unic.append(i[0])
    def repl( x ):
        return x[1] + form + '(!)'

    for ch_root in unic:
        patern = r'(' + ch_root + ')(ый|ий|ой|ая|яя|ое|ее|ые|ие|ого|его|ой|ей|ому|ему|ую|юю|ым|им|ыми|ими|ом|ем|ых|их)'
        text = re.sub(patern, repl, text, flags=re.IGNORECASE)
    print("Измененный текст (Измененные окончания обозначены !):")
    print(text)
