# 501787 % 132 = 55 => YAML -> HCL, Дни: среда, суббота

with open('output_task0.txt', 'r') as f:
    data = f.read()

data = data.replace("\'", "'")
# print(data)

def skip_spaces(ind, data):
    while ind < len(data) and data[ind] == ' ':
        ind += 1
    return ind

def get_key(i, data):
    i += 1
    begin = i
    while i < len(data) and data[i] != "'":
        i += 1
    if i + 1 < len(data):
        i += 1
    else:
        print("OutOfRange in GETKEY")
    return i, data[begin : i-1]
def get_val(i, data):
    begin = i
    if data[i] == "'":
        i += 1
        while i < len(data) and data[i] != "'":
            i += 1
        if i + 1 < len(data):
            i += 1
        else:
            print("OutOfRange in GETVAL")
        return i, data[begin : i - 1]
    else:
        i, val = parse_data(data, i)
        return i, val

def parse_data( data, i=0 ):
    mydict = dict()
    i = skip_spaces(i, data)
    if data[i] == '{':
        i += 1
        i = skip_spaces(i, data)
        i, key = get_key(i, data)
        i = skip_spaces(i, data)
        if data[i] != ':':
            print("DictError")
        i += 1
        i = skip_spaces(i, data)

        i, val = parse_data(data, i)
        mydict[key] = val
        i = skip_spaces(i, data)
        while i < len(data) and data[i] == ',':
            i += 1
            i = skip_spaces(i, data)
            i, key = get_key(i, data)
            i = skip_spaces(i, data)
            if data[i] != ':':
                print("DictError")
            i += 1
            i = skip_spaces(i, data)

            i, val = parse_data(data, i)
            mydict[key] = val
        i = skip_spaces(i, data)
        if data[i] == '}':
            return i+1, mydict
        else:
            print("ERROR dict")

    elif data[i] == '[':
        mylist = []
        i += 1
        i = skip_spaces(i, data)
        while i < len(data) and data[i] != ']':
            if data[i] == ',':
                i += 1
            i, val = parse_data(data, i)
            skip_spaces(i, data)
            mylist.append(val)
        i += 1
        skip_spaces(i, data)
        return i, mylist
    elif data[i] == "'":
        return get_key(i, data)
    return i, mydict

TaskDict = parse_data(data)[1]
# print("Отпарсированный словарь:")
# print(TaskDict)


def check_key( key ):
    i = 0
    while i < len(key):
        if key[i] == '"':
            i += 1
            while i < len(key) and key[i] != '"':
                i += 1
        if key[i] == ':':
            return key[:i] + ' =' + key[i+1:]
        i += 1
    return key
def get_HCL( data, count_spaces=0 ):
    space = '  ' * count_spaces
    lines = []
    if isinstance(data, dict):
        for key, val in data.items():
            lines.append(f'{space}{check_key(key)} {{')
            lines.append(get_HCL(val, count_spaces + 1))
            lines.append(f'{space}}}')
    elif isinstance(data, list):
        for item in data:
            lines.append(f'{space}{{')
            lines.append(get_HCL(item, count_spaces + 1))
            lines.append(f'{space}}}')
    else:
        lines.append(f'{space}"{data}"')
    return '\n'.join(lines)

res = get_HCL(TaskDict)
# print(res)
with open("output_task1.txt", "w") as f:
	f.write(res)
