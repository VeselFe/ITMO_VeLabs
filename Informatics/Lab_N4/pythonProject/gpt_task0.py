def count_indent(line):
    return len(line) - len(line.lstrip(' '))

def parse_block(lines, start, indent):
    items = []
    definition = {}
    i = start
    while i < len(lines):
        line = lines[i]
        this_indent = count_indent(line)
        if this_indent < indent:
            break
        line = line.strip()
        if not line:
            i += 1
            continue
        if line.startswith('- '):  # список
            value = line[2:].strip()
            if value.endswith(':'):  # ключ со вложенным словарем
                key = value[:-1].strip()
                val, next_i = parse_block(lines, i + 1, this_indent + 2)
                items.append({key: val})
                i = next_i
            else:
                # Если последний элемент в списке - значение, возможно словарь
                # Смотрим, есть ли вложенная структура на следующей строке
                if i + 1 < len(lines):
                    next_indent = count_indent(lines[i + 1])
                    if next_indent > this_indent:
                        # Парсим вложенный блок как словарь
                        val, next_i = parse_block(lines, i + 1, next_indent)
                        items.append({value: val})
                        i = next_i
                    else:
                        items.append(value)
                        i += 1
                else:
                    items.append(value)
                    i += 1
        elif ':' in line:  # словарь
            key, val = map(str.strip, line.split(':', 1))
            if val == '':
                # вложенный словарь
                val, next_i = parse_block(lines, i + 1, this_indent + 2)
                definition[key] = val
                i = next_i
            else:
                definition[key] = val
                i += 1
        else:
            i += 1

    if items:
        return items, i
    else:
        return definition, i


def parse_yaml(text: str):
    lines = [line.rstrip() for line in text.strip().split('\n') if line.strip() != '']
    parsed, _ = parse_block(lines, 0, 0)
    return parsed

# Пример использования
if __name__ == "__main__":
    with open("input.txt", encoding="utf-8") as f:
        example_yaml = f.read()
    result = parse_yaml(example_yaml)
    print(result)
