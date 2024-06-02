package big.company.input;

import big.company.organization.Employee;
import java.util.List;

public class EmployeesCsvParser implements EmployeesParser {

  private static final int COLUMNS_NUMBER = 5;
  private static final String COMMA = ",";
  private static final int HEADER_LINES = 1;

  private static final class ColumnIndex {

    private static final int ID = 0;
    private static final int FIRST_NAME = 1;
    private static final int LAST_NAME = 2;
    private static final int SALARY = 3;
    private static final int MANAGER_ID = 4;

  }

  @Override
  public List<Employee> parse(List<String> lines) {
    return lines.stream()
        .filter(line -> !line.isBlank())
        .skip(HEADER_LINES)
        .map(this::parseEmployee)
        .toList();
  }

  private Employee parse(String[] row) {
    return new Employee(parseId(row), parseFirstName(row), parseLastName(row), parseSalary(row), parseManagerId(row));
  }

  private Employee parseEmployee(String csvRow) {
    String[] row = csvRow.split(COMMA, -1);
    validate(row);
    return parse(row);
  }

  private void validate(String[] row) {
    validateRowComplete(row);
    validateRequiredFieldsSet(row);
  }

  private void validateRowComplete(String[] row) {
    if (row.length < COLUMNS_NUMBER) {
      throw new IllegalStateException("Row is invalid. Expected %d columns, but was %d".formatted(COLUMNS_NUMBER, row.length));
    }
  }

  private void validateRequiredFieldsSet(String[] row) {
    for (int i = 0; i < row.length; i++) {
      if (i != ColumnIndex.MANAGER_ID && row[i].isBlank()) {
        throw new IllegalStateException("The value of column %d is missing".formatted(i + 1));
      }
    }
  }

  private long parseId(String[] row) {
    return Long.parseLong(row[ColumnIndex.ID].strip());
  }

  private String parseFirstName(String[] row) {
    return row[ColumnIndex.FIRST_NAME].strip();
  }

  private String parseLastName(String[] row) {
    return row[ColumnIndex.LAST_NAME].strip();
  }

  private long parseSalary(String[] row) {
    return Long.parseLong(row[ColumnIndex.SALARY].strip());
  }

  private Long parseManagerId(String[] row) {
    String managerIdRaw = row[ColumnIndex.MANAGER_ID];
    if (managerIdRaw.isBlank()) {
      return null;
    }
    return Long.valueOf(managerIdRaw.strip());
  }

}
