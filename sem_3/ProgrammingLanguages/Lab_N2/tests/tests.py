import subprocess 
import sys

PROGRAM_NAME = "./program"
successful_tests = 0
failed_tests = 0
errors = []

def test( test_number=0, test_input="", predict_stdout="", predict_stderr="", exit_code=0 ):
    global successful_tests
    global failed_tests
    
    result = subprocess.run( 
    [PROGRAM_NAME], 
    input=test_input+'\n', 
    capture_output=True, 
    text=True 
    )

    try:
        assert result.stdout.removeprefix("Input string: ").strip() == predict_stdout, f"Ожидался stdout '{predict_stdout}', получено: '{result.stdout}'" 
        assert result.stderr.removeprefix("Input string: ").strip() == predict_stderr, "Ожидался пустой stderr" if predict_stderr == '' else predict_stderr + f", но получено: '{result.stderr}'" 
        assert result.returncode == exit_code, f"Тест #{test_number} (exit code): ожидался {exit_code}, получен: {result.returncode}" 
    except AssertionError as e: 
        print(f"[-] Тест #{ test_number } ПРОВАЛЕН") 
        failed_tests += 1   
        errors.append([f"Тест #{test_number} Провален:", e])
        return

    print(f"[+] Тест #{test_number} успешно пройден!")
    successful_tests += 1

print("\nТесты:")
test(1, "first word", "Im 1st")
test(2, "last word", "Its the last word!!!!!")
test(3, "11111 9 % 7 * test strange symbols @@#", "23 345 -- -_ 345 56 35sdf rt3 !")
test(4, "unknown  key", "", "word was not found", 0)
test(5, "first", "", "word was not found", 0)
test(6, "first word  ", "", "word was not found", 0)
test(7, "", "", "word was not found", 0)
test(8, "\n", "", "word was not found", 0)
test(9, "a" * 255, "", "word was not found", 0)
test(10, "a" * 260, "", "error: buffer overflow", 1)

print(f"\n--------\n Тесты выполнены | Успешно: {successful_tests}, Неудачно: {failed_tests}\n")

for error in errors:
    print(error[0])
    print(error[1])

if failed_tests > 0:
    sys.exit(1)