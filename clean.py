import re

def clean_verilog(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    cleaned_lines = []
    for line in lines:
        # 去除整行注释
        if not line.strip().startswith('//'):
            # 去除行末注释
            cleaned_line = re.sub(r'//.*', '', line)
            cleaned_lines.append(cleaned_line)

    cleaned_content = ''.join(cleaned_lines)

    with open(file_path, 'w') as file:
        file.write(cleaned_content)
if __name__ == "__main__":
    clean_verilog('/home/wuxy/chisel/MyChisel/generated/top.v')
