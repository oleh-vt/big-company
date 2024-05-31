package big.company.organization.impl;

import big.company.model.Employee;
import big.company.organization.OrganizationStructureBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BigCompanyOrgStructureBuilder implements OrganizationStructureBuilder {

    @Override
    public BigCompanyOrganization build(Collection<Employee> employees) {
        return new BigCompanyOrganization(collectEmployeesById(employees), collectSubordinatesByManager(employees));
    }

    private Map<Long, Set<Long>> collectSubordinatesByManager(Collection<Employee> employees) {
        return employees.stream()
                .filter(e -> Objects.nonNull(e.managerId()))
                .collect(
                        Collectors.groupingBy(
                                Employee::managerId,
                                Collectors.mapping(Employee::id, Collectors.toSet())
                        ));
    }

    private Map<Long, Employee> collectEmployeesById(Collection<Employee> employees) {
        return employees.stream()
                .collect(Collectors.toMap(Employee::id, Function.identity()));
    }
}
