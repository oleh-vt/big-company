package big.company.organization.impl;

import big.company.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        employees.add(new Employee(10, "John2", "Doe", 1000, 1L));
        for (int i = 0; i < numberOfCeos; i++) {
            employees.add(new Employee(i + 1, "John", "Doe", 1000, null));
        }
        assertThrows(IllegalStateException.class, () -> bigCompanyEmployeesValidator.validate(employees));
    }

    @DisplayName("When employee id is not unique, should throw an exception")
    @Test
    void shouldThrowExceptionWhenEmployeeIdIsNotUnique() {
        List<Employee> employees = List.of(
                new Employee(1, "John1", "Doe", 1000, null),
                new Employee(2, "John2", "Doe", 1000, 1L),
                new Employee(1, "John3", "Doe", 1000, 3L)
        );

        assertThrows(IllegalStateException.class, () -> bigCompanyEmployeesValidator.validate(employees));
    }

    @DisplayName("When employees present, only one CEO and employee ids are unique, should not throw exceptions")
    @Test
    void shouldNotThrowExceptionsWhenEmployeesPresentOneCeoAndEmployeeIdsUnique() {
        List<Employee> employees = List.of(
                new Employee(1, "John1", "Doe", 1000, null),
                new Employee(2, "John2", "Doe", 1000, 1L),
                new Employee(3, "John3", "Doe", 1000, 1L)
        );

        assertDoesNotThrow(() -> bigCompanyEmployeesValidator.validate(employees));
    }

    @DisplayName("When there is no an employee in the list with the given managerId, should throw an exception")
    @Test
    void shouldThrowExceptionWhenManagerIdDoesNotExist() {
        List<Employee> employees = List.of(
                new Employee(1, "John1", "Doe", 1000, null),
                new Employee(2, "John2", "Doe", 1000, 1L),
                new Employee(3, "John3", "Doe", 1000, 1000L)
        );

        assertThrows(IllegalStateException.class, () -> bigCompanyEmployeesValidator.validate(employees));
    }


}