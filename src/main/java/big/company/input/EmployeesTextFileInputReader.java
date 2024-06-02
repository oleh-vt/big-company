package big.company.input;

import big.company.exception.BigCompanyApplicationException;
import big.company.organization.Employee;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class EmployeesTextFileInputReader implements EmployeesFileInputReader {

  public static final String FILE_NOT_EXIST_MESSAGE = "File does not exist: %s";
  public static final String PATH_IS_DIRECTORY_MESSAGE = "The specified path should be a file, but was a directory: %s";
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
      throw new BigCompanyApplicationException(FILE_NOT_EXIST_MESSAGE.formatted(filePath));
    }
    if (Files.isDirectory(filePath)) {
      throw new BigCompanyApplicationException(PATH_IS_DIRECTORY_MESSAGE.formatted(filePath));
    }
  }

  private List<String> readAllLines(Path filePath) {
    try {
      return Files.readAllLines(filePath);
    } catch (IOException e) {
      throw new BigCompanyApplicationException(e.getMessage());
    }
  }

}
