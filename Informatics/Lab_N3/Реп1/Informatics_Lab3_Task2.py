# Author = Veselov Fedor Evgenievich
# Group = P3132
# Date = 23.10.2025
# 501787 % 5 = 2

import re

print("Write your string: ")
new_str = str(input())

tests = ["10et1w$$t-r4t+7=2д17ушк3",
          "18ноября, длится7 недель, а лабораторных — \"3\"",
          "Порог сдачи: 10 баллов из 50 — это серьезно!",
          "12iu3ug5jhg*2&40???22@",
          "15 + 22 = 37"]

tests.append(new_str)
patern = r'-?\d+[.,]?\d*[eE][-+]?\d+|-?\d+[.,]?\d*'
def func( x ):
    n = x[0].replace(',', '.')
    return str(float(n) ** 3 * 5 - 13)
# for i in range(6):
#     print("Test #" + str(i+1) + ": ")
#     print(re.sub(patern, func, tests[i]))
print(re.sub(patern, func, tests[5]))