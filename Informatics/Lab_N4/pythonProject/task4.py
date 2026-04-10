# 501787 % 132 = 55 => YAML -> HCL, Дни: среда, суббота

import os
import time

programs = ["task0.py", "task1.py", "task2.py", "task3.py"]

for prog in programs:
	start_time = time.time()
	for i in range(100):
		exit_code = os.system(f"venv\Scripts\python {prog}")
	end_time = time.time()

	elapsed = end_time - start_time

	if exit_code == 0:
		print(f"{prog} выполнена 100 раз за {elapsed:.2f} секунд")
	else:
		print(f"{prog} завершилась с ошибкой, код выхода: {exit_code}, время: {elapsed:.2f} секунд")
