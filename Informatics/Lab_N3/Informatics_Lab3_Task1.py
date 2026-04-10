# Author = Veselov Fedor Evgenievich
# Group = P3132
# Date = 05.10.2025
# 501787 % 7 = 6

import requests # узнать про данную библиотеку
import re

# url = 'https://isu.itmo.ru'
# response = requests.get(url)
# html = response.text

with open('isu_itmo.txt', 'r', encoding='utf-8') as file:
    html = file.read()

# print(html)
patern = r'id="([^"]*)"'
print("isu: ")
print(re.findall(patern, html))

for i in range(1,5):
    print("Test #" + str(i) + ": ")
    with open('Tests_for_1/' + str(i) + '.txt', 'r', encoding='utf-8') as file:
        html = file.read()
    print(re.findall(patern, html))