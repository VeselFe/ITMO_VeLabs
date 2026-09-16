# 501787 % 132 = 55 => YAML -> HCL, Дни: среда, суббота
def get_lines(text):
	return [line.rstrip() for line in text.split('\n') if line.strip() != '']

def count_indent( line ):
    return len(line) - len(line.lstrip(' '))

def parser(lines, ind_start, indent):
	items = []
	definition = {}
	i = ind_start
	while i < len(lines):
		this_line = lines[i]
		this_indent = count_indent(this_line)
		if this_indent < indent:
			break
		this_line = this_line.strip()
		if '#' in this_line:
			comment = this_line.index('#')
			this_line = this_line[:comment]
		if not this_line:
			i += 1
			continue
		if this_line.startswith('- '):
			value = this_line[2:].strip()
			if value.endswith(':'):
				key = value[:-1].strip()
				val, i_next = parser(lines, i + 1, this_indent + 2)
				items.append({key: val})
				i = i_next
			else:
				if i + 1 < len(lines):
					next_indent = count_indent(lines[i + 1])
					if next_indent > this_indent:
						val, next_i = parser(lines, i + 1, next_indent)
						items.append({value: val})
						i = next_i
					else:
						items.append(value)
						i += 1
				else:
					items.append(value)
					i += 1
		elif ':' in this_line:
			key, value = map(str.strip, this_line.split(':', 1))
			if value != '':
				definition[key] = value
				i += 1
			else:
				value, i_next = parser(lines, i + 1, this_indent + 2)
				definition[key] = value
				i = i_next
		else:
			print("FormatError in " + str(i + 1))
			i += 1
	if items:
		return items, i
	else:
		return definition, i

with open("input.txt", encoding="utf-8") as f:
	yaml_text = f.read()
result = parser(get_lines(yaml_text), 0, 0)[0]
# print(result)

with open("output_task0.txt", "w") as f:
	f.write(str(result))