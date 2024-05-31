package big.company.organization.impl;

import big.company.model.Employee;
import big.company.organization.Organization;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BigCompanyOrganization implements Organization {

    private final Map<Long, Employee> employeeById;
    private final Map<Long, Set<Long>> subordinatesByManager;

    BigCompanyOrganization(Map<Long, Employee> employeeById, Map<Long, Set<Long>> subordinatesByManager) {
        this.employeeById = employeeById;
        this.subordinatesByManager = subordinatesByManager;
    }

    @Override
    public void analyzeManagerSalaries() {

    }

    @Override
    public void analyzeStructure() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigCompanyOrganization that = (BigCompanyOrganization) o;
        return employeeById.equals(that.employeeById) && subordinatesByManager.equals(that.subordinatesByManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeById, subordinatesByManager);
    }
}
