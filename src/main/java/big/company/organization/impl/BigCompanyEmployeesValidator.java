package big.company.organization.impl;

import big.company.model.Employee;
import big.company.organization.OrganizationEmployeesValidator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BigCompanyEmployeesValidator implements OrganizationEmployeesValidator {

    public void validate(Collection<Employee> employees) {
        validateEmployeesNotEmpty(employees);
        validateExactlyOneCeo(employees);
        validateIdsUnique(employees);
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
