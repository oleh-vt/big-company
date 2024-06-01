package big.company.organization.impl;

import big.company.organization.Employee;
import big.company.organization.OrganizationEmployeesValidator;

import java.util.*;
import java.util.stream.Collectors;

public class BigCompanyEmployeesValidator implements OrganizationEmployeesValidator {

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
      throw new IllegalStateException("Manager id does not reference any of the employees. Ids: " + nonExistentManagerIds);
    }
  }

  private void validateEmployeesNotEmpty(Collection<Employee> employees) {
    if (employees == null || employees.isEmpty()) {
      throw new IllegalStateException("There are no any employees in the organization");
    }
  }

  private void validateExactlyOneCeo(Collection<Employee> employees) {
    List<Employee> ceos = employees.stream()
        .filter(e -> Objects.isNull(e.managerId()))
        .toList();
    if (ceos.size() != 1) {
      throw new IllegalStateException("There should be exactly one CEO, but was " + ceos.size() + ". " + ceos);
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
      throw new IllegalStateException("Employee Id must be empty, but it was not. Ids: " + nonUniqueIds);
    }
  }


}
