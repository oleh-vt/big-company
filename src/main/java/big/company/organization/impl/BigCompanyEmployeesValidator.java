package big.company.organization.impl;

import big.company.exception.BigCompanyApplicationException;
import big.company.organization.Employee;
import big.company.organization.OrganizationEmployeesValidator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BigCompanyEmployeesValidator implements OrganizationEmployeesValidator {

  private static final String INVALID_MANAGER_ID_REFERENCE_MESSAGE = "Manager id does not reference any of the employees. Ids: %s";
  private static final String NO_EMPLOYEES_MESSAGE = "There are no any employees in the organization";
  private static final String MORE_THAN_ONE_CEO_MESSAGE = "There should be exactly one CEO, but was %s. %s";
  private static final String NON_UNIQUE_IDS_MESSAGE = "Employee Id must be unique, but it was not. Ids: %s";

  public void validate(Collection<Employee> employees) {
    validateEmployeesNotEmpty(employees);
    validateExactlyOneCeo(employees);
    validateIdsUnique(employees);
    validateManagerIdsAreExistingEmployees(employees);
  }

  private void validateManagerIdsAreExistingEmployees(Collection<Employee> employees) {
    final Set<Long> employeeIds = employees.stream()
        .map(Employee::id)
        .collect(Collectors.toSet());
    List<Long> nonExistentManagerIds = employees.stream()
        .map(Employee::managerId)
        .filter(Objects::nonNull)
        .distinct()
        .filter(managerId -> !employeeIds.contains(managerId))
        .toList();
    if (!nonExistentManagerIds.isEmpty()) {
      throw new BigCompanyApplicationException(INVALID_MANAGER_ID_REFERENCE_MESSAGE.formatted(nonExistentManagerIds));
    }
  }

  private void validateEmployeesNotEmpty(Collection<Employee> employees) {
    if (employees == null || employees.isEmpty()) {
      throw new BigCompanyApplicationException(NO_EMPLOYEES_MESSAGE);
    }
  }

  private void validateExactlyOneCeo(Collection<Employee> employees) {
    List<Employee> ceos = employees.stream()
        .filter(e -> Objects.isNull(e.managerId()))
        .toList();
    if (ceos.size() != 1) {
      throw new BigCompanyApplicationException(MORE_THAN_ONE_CEO_MESSAGE.formatted(ceos.size(), ceos));
    }
  }

  private void validateIdsUnique(Collection<Employee> employees) {
    List<Long> nonUniqueIds = employees.stream()
        .collect(Collectors.groupingBy(Employee::id, Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .toList();
    if (!nonUniqueIds.isEmpty()) {
      throw new BigCompanyApplicationException(NON_UNIQUE_IDS_MESSAGE.formatted(nonUniqueIds));
    }
  }


}
