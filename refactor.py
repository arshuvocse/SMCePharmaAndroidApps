import re

path = r'd:\CSTL_Projects\SMC\ePharma\Apps\clickpharma\app\src\main\java\com\creatrix\salessolution\DBAdapter\SyncDb_Helper.java'

with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Pattern to find the Insert query and execSQL
# We will match:
# String insertQuery = "Insert into TableName(Col1,Col2,...)" + 
#    "values('" + obj.getCol1() + "','" + obj.getCol2() + "')";
# database.execSQL(insertQuery);

pattern = re.compile(
    r'String\s+(insertQuery|query|insertBrand)\s*=\s*"[Ii]nsert\s+into\s+([A-Za-z0-9_]+)\s*\(([^)]+)\)\s*"\s*\+?\s*[\r\n]*\s*"values\s*\(\'\s*"\s*\+\s*(.*?)\s*\+\s*"\s*\'\)"\s*;'
    r'\s*(?:databases?\.execSQL\(\1\);|//\s*database\.beginTransaction\(\);\s*databases?\.execSQL\(\1\);)',
    re.DOTALL
)

def replacer(match):
    var_name = match.group(1)
    table_name = match.group(2)
    cols_str = match.group(3)
    vals_str = match.group(4)
    
    cols = [c.strip() for c in cols_str.split(',')]
    # vals_str looks like: nInfo.getNSMEmpId() + "','" + nInfo.getNSMId()
    # Let's split by + "','" +
    # Note: Sometimes it has .replace("'", "''") which we don't strictly need with ContentValues, but we can leave it or remove it.
    
    # We can split vals_str by `\s*\+\s*"\'\,\'"\s*\+\s*` or similar.
    # Actually, the separator is exactly `+ "','" +` (with possible spaces)
    vals = re.split(r'\+\s*"\'\,\'"\s*\+', vals_str)
    
    if len(cols) != len(vals):
        # Fallback if parsing fails
        print(f"Mismatch in {table_name}: {len(cols)} cols, {len(vals)} vals")
        return match.group(0)
        
    lines = ["android.content.ContentValues values = new android.content.ContentValues();"]
    for c, v in zip(cols, vals):
        v = v.strip()
        lines.append(f'values.put("{c}", {v});')
    
    # Some use 'databases', some use 'database'
    lines.append(f'database.insert("{table_name}", null, values);')
    return "\n".join(lines)

new_content = pattern.sub(replacer, content)

# Next, we need to wrap `for` loops in SyncDb_Helper with beginTransaction.
# A simpler way: we just find `public boolean Insert` methods that don't have beginTransaction, 
# and inject it.

# Actually, the transaction logic can be applied globally to all `public boolean Insert` methods:
def inject_transaction(content):
    methods = re.split(r'(public boolean Insert[A-Za-z0-9_]+\(List<[^>]+>\s*[a-zA-Z0-9_]+\)\s*\{)', content)
    out = [methods[0]]
    for i in range(1, len(methods), 2):
        sig = methods[i]
        body = methods[i+1]
        
        # Check if it already has beginTransaction
        if 'database.beginTransaction()' not in body and 'SQLiteDatabase database' in body:
            # Inject beginTransaction right after getWritableDatabase()
            body = re.sub(r'(SQLiteDatabase\s+databases?\s*=\s*dbHelperMain\.getWritableDatabase\(\);)',
                          r'\1\n            database.beginTransaction();\n            try {', body, count=1)
            
            # Inject setTransactionSuccessful and endTransaction before database.close() or return
            # We look for `return isTrue;` or `return true;`
            # But wait, there's a try-catch block for the whole method!
            # It's better to find the end of the `for` loop.
            pass
            
        out.append(sig)
        out.append(body)
    return "".join(out)

with open(path, 'w', encoding='utf-8') as f:
    f.write(new_content)

print("Replacement complete. Let's inspect the diff.")
