import csv

def validate_csv(filepath):
    with open(filepath, newline='', encoding='utf-8') as csvfile:
        reader = csv.reader(csvfile)
        try:
            header = next(reader)
        except StopIteration:
            return False, "The CSV file is empty."

        if not header or header[0] != "input_text":
            return False, "The first column name is not 'input_text'."

        expected_columns = len(header)
        for i, row in enumerate(reader, start=2):
            if len(row) != expected_columns:
                return False, f"Row {i} has {len(row)} columns, expected {expected_columns}."

    return True, "CSV file is valid."