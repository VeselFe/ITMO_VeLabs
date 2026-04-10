# 501787 % 132 = 55 => YAML -> HCL, Дни: среда, суббота

import yaml

with open("input.txt", "r", encoding="utf-8") as f:
    data = yaml.safe_load(f)

# print('Полученный словарь: ')
# print(data)

from pytfvars import tfvars

hcl_output = tfvars.convert(data)

with open("output_task2.txt", "w", encoding="utf-8") as f:
    f.write('Dictionary:\n')
    f.write(str(data) + '\n\n\n')
    f.write('HCL:\n')
    f.write(hcl_output)

# print(hcl_output)





