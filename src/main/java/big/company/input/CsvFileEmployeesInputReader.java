package big.company.input;

import big.company.model.Employee;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

//TODO: split the file load and parsing

public class CsvFileEmployeesInputReader implements EmployeesInputReader {

  private static final int COLUMNS_NUMBER = 5;
  private static final String COMMA = ",";

  @Override
  public List<Employee> read(Path filePath) {
    validateFile(filePath);
    try (Stream<String> lines = Files.lines(filePath)) {
      return lines
          .filter(line -> !line.isBlank())
          .skip(1)
          .map(this::parseEmployeeDto)
          .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void validateFile(Path filePath) {
    if (Files.notExists(filePath)) {
      throw new IllegalArgumentException("File does not exist: " + filePath);
    }
    if (Files.isDirectory(filePath)) {
      throw new IllegalArgumentException("The specified path should be a file, but was a directory: " + filePath);
    }
  }

  private Employee parseEmployeeDto(String csvRow) {
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

  private Employee parse(String[] row) {
    return new Employee(parseId(row), parseFirstName(row), parseLastName(row), parseSalary(row), parseManagerId(row));
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

  private static final class ColumnIndex {

    private static final int ID = 0;
    private static final int FIRST_NAME = 1;
    private static final int LAST_NAME = 2;
    private static final int SALARY = 3;
    private static final int MANAGER_ID = 4;
  }


}
