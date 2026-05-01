import os
import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    if '@Data' not in content and '@Getter' not in content:
        return

    # Remove lombok imports
    content = re.sub(r'import lombok\..*?;\n', '', content)
    # Remove lombok annotations
    content = re.sub(r'@Data\n', '', content)
    content = re.sub(r'@NoArgsConstructor\n', '', content)
    content = re.sub(r'@AllArgsConstructor\n', '', content)

    # Find class name
    class_match = re.search(r'public class (\w+)', content)
    if not class_match: return
    class_name = class_match.group(1)

    # Find fields
    fields = re.findall(r'private\s+([\w<>,\s]+)\s+(\w+)(?:\s*=\s*[^;]+)?\s*;', content)
    
    methods = []
    
    # Add NoArgs constructor
    methods.append(f"    public {class_name}() {{}}")

    # Add AllArgs constructor (if there are fields, we skip for simplicity, or just generate standard getters/setters)
    
    for f_type, f_name in fields:
        f_type = f_type.strip()
        capitalized = f_name[0].upper() + f_name[1:]
        
        # Getter
        methods.append(f"    public {f_type} get{capitalized}() {{\n        return {f_name};\n    }}")
        # Setter
        methods.append(f"    public void set{capitalized}({f_type} {f_name}) {{\n        this.{f_name} = {f_name};\n    }}")

    methods_str = "\n\n".join(methods)
    
    # Insert before the last closing brace
    last_brace_idx = content.rfind('}')
    if last_brace_idx != -1:
        content = content[:last_brace_idx] + "\n" + methods_str + "\n}\n"
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Processed {filepath}")

dirs = [
    r"c:\Users\vmandge\Downloads\MediMoiz\MedBillBackend\src\main\java\com\medbill\backend\models",
    r"c:\Users\vmandge\Downloads\MediMoiz\MedBillBackend\src\main\java\com\medbill\backend\dto"
]

for d in dirs:
    if os.path.exists(d):
        for root, _, files in os.walk(d):
            for file in files:
                if file.endswith('.java'):
                    process_file(os.path.join(root, file))
