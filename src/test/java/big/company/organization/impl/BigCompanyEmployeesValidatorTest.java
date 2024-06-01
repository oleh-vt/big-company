package big.company.organization.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import big.company.model.Employee;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class BigCompanyEmployeesValidatorTest {

  private final BigCompanyEmployeesValidator bigCompanyEmployeesValidator = new BigCompanyEmployeesValidator();

  @DisplayName("When no employees in the list, should throw an exception")
  @ParameterizedTest(name = "Employees == {arguments}")
  @NullAndEmptySource
  void shouldThrowExceptionWhenNoEmployees(List<Employee> employees) {
    assertThrows(IllegalStateException.class, () -> bigCompanyEmployeesValidator.validate(employees));
  }

  @DisplayName("When CEO is not exactly one, should throw an exception")
  @ParameterizedTest(name = "CEOs == {arguments}")
  @ValueSource(ints = {0, 2})
  void shouldThrowExceptionWhenNoEmployees(int numberOfCeos) {
    List<Employee> employees = new ArrayList<>();
    employees.add(new Employee(10L, "John2", "Doe", 1000L, 1L));
    for (int i = 0; i < numberOfCeos; i++) {
      employees.add(new Employee(i + 1L, "John", "Doe", 1000L, null));
    }
    assertThrows(IllegalStateException.class, () -> bigCompanyEmployeesValidator.validate(employees));
  }

  @DisplayName("When employee id is not unique, should throw an exception")
  @Test
  void shouldThrowExceptionWhenEmployeeIdIsNotUnique() {
    List<Employee> employees = List.of(
        new Employee(1L, "John1", "Doe", 1000L, null),
        new Employee(2L, "John2", "Doe", 1000L, 1L),
        new Employee(1L, "John3", "Doe", 1000L, 3L)
    );

    assertThrows(IllegalStateException.class, () -> bigCompanyEmployeesValidator.validate(employees));
  }

  @DisplayName("When employees present, only one CEO and employee ids are unique, should not throw exceptions")
  @Test
  void shouldNotThrowExceptionsWhenEmployeesPresentOneCeoAndEmployeeIdsUnique() {
    List<Employee> employees = List.of(
        new Employee(1L, "John1", "Doe", 1000L, null),
        new Employee(2L, "John2", "Doe", 1000L, 1L),
        new Employee(3L, "John3", "Doe", 1000L, 1L)
    );

    assertDoesNotThrow(() -> bigCompanyEmployeesValidator.validate(employees));
  }

  @DisplayName("When there is no an employee in the list with the given managerId, should throw an exception")
  @Test
  void shouldThrowExceptionWhenManagerIdDoesNotExist() {
    List<Employee> employees = List.of(
        new Employee(1L, "John1", "Doe", 1000L, null),
        new Employee(2L, "John2", "Doe", 1000L, 1L),
        new Employee(3L, "John3", "Doe", 1000L, 1000L)
    );

    assertThrows(IllegalStateException.class, () -> bigCompanyEmployeesValidator.validate(employees));
  }


}