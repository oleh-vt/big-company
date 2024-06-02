package big.company.input.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import big.company.exception.BigCompanyApplicationException;
import big.company.input.EmployeesFileInputReader;
import big.company.organization.Employee;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class EmployeesTextFileInputReaderTest {

  private static final String EMPLOYEES_CSV = "employees.csv";
  private static final String HEADER = "id,firstName,lastName,salary,managerId" + System.lineSeparator();

  private EmployeesFileInputReader employeesFileInputReader;

  @TempDir
  private Path tempDir;

  private Path csvFile;

  @BeforeEach
  void setUp() {
    employeesFileInputReader = new EmployeesTextFileInputReader(new EmployeesCsvParser());
  }

  @DisplayName("When file exists, should read employees from the file")
  @Test
  void shouldReadEmployeesFromFile() {
    prepareFile("""
                        
        124 , Martin, Chekov, 45000, 123
        """);
    List<Employee> expectedEmployees = List.of(new Employee(124L, "Martin", "Chekov", 45000L, 123L));

    List<Employee> employees = employeesFileInputReader.read(csvFile);

    assertEquals(expectedEmployees, employees);
  }

  @DisplayName("When the file does not exist, should throw an exception")
  @Test
  void shouldThrowExceptionWhenFileNotExists() {
    Path nonExistingFile = tempDir.resolve("non_existing.csv");

    assertThrows(BigCompanyApplicationException.class, () -> employeesFileInputReader.read(nonExistingFile));
  }

  @DisplayName("When the path is a directory, should throw an exception")
  @Test
  void shouldThrowExceptionWhenThePathIsDirectory() {
    assertThrows(BigCompanyApplicationException.class, () -> employeesFileInputReader.read(tempDir));
  }

  private void prepareFile(String csv) {
    try {
      csvFile = Files.createFile(tempDir.resolve(EMPLOYEES_CSV));
      Files.write(csvFile, HEADER.concat(csv).getBytes(), StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}