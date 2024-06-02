package big.company.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import big.company.exception.BigCompanyApplicationException;
import big.company.organization.Employee;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmployeesCsvParserTest {

  private static final String HEADER = "id,firstName,lastName,salary,managerId";
  private static final String EMPTY = "";
  private static final String BLANK = "  ";

  private final EmployeesCsvParser employeesCsvParser = new EmployeesCsvParser();

  @DisplayName("Should parse employee when all fields are present")
  @Test
  void shouldReadEmployeeWithAllFieldsSet() {
    List<String> lines = prepareCsvFileContent("124 , Martin, Chekov, 45000, 123");
    var expectedEmployee = new Employee(124L, "Martin", "Chekov", 45000L, 123L);

    List<Employee> employees = employeesCsvParser.parse(lines);

    assertEquals(1, employees.size());
    assertEquals(expectedEmployee, employees.get(0));
  }

  @DisplayName("Should parse employee when managerId field is not set")
  @Test
  void shouldReadEmployeeWhenManagerIdFieldIsNotSet() {
    List<String> lines = prepareCsvFileContent("123, Joe, Doe, 60000, ");
    var expectedEmployee = new Employee(123L, "Joe", "Doe", 60000L, null);

    List<Employee> employees = employeesCsvParser.parse(lines);

    assertEquals(1, employees.size());
    assertEquals(expectedEmployee, employees.get(0));
  }

  @DisplayName("Should skip blank lines")
  @Test
  void shouldSkipBlankLinesInFile() {
    List<String> lines = prepareCsvFileContent(EMPTY, BLANK, "124 , Martin, Chekov, 45000, 123");
    var expectedEmployee = new Employee(124L, "Martin", "Chekov", 45000L, 123L);

    List<Employee> employees = employeesCsvParser.parse(lines);

    assertEquals(1, employees.size());
    assertEquals(expectedEmployee, employees.get(0));
  }

  @DisplayName("When any of the required fields is blank, should throw an exception")
  @ParameterizedTest
  @ValueSource(strings = {"id", "firstName", "lastName", "salary"})
  void shouldThrowExceptionWhenAnyOfTheRequiredFieldsBlank(String requiredField) {
    String csv = "id,firstName,lastName,salary".replace(requiredField, EMPTY);
    List<String> lines = prepareCsvFileContent(csv);

    assertThrows(BigCompanyApplicationException.class, () -> employeesCsvParser.parse(lines));
  }

  @DisplayName("When a row is incomplete, should throw an exception")
  @Test
  void shouldThrowExceptionWhenRowIsIncomplete() {
    List<String> lines = prepareCsvFileContent("123, Joe, Doe");
    assertThrows(BigCompanyApplicationException.class, () -> employeesCsvParser.parse(lines));
  }

  private List<String> prepareCsvFileContent(String... csvRows) {
    return Stream.concat(Stream.of(HEADER), Arrays.stream(csvRows)).toList();
  }

}