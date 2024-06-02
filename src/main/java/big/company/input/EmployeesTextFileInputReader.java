package big.company.input;

import big.company.organization.Employee;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class EmployeesTextFileInputReader implements EmployeesFileInputReader {

  private final EmployeesParser employeesParser;

  public EmployeesTextFileInputReader(EmployeesParser employeesParser) {
    this.employeesParser = employeesParser;
  }

  @Override
  public List<Employee> read(Path filePath) {
    validateFile(filePath);
    return employeesParser.parse(readAllLines(filePath));
  }

  private void validateFile(Path filePath) {
    if (Files.notExists(filePath)) {
      throw new IllegalArgumentException("File does not exist: " + filePath);
    }
    if (Files.isDirectory(filePath)) {
      throw new IllegalArgumentException("The specified path should be a file, but was a directory: " + filePath);
    }
  }

  private List<String> readAllLines(Path filePath) {
    try {
      return Files.readAllLines(filePath);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
