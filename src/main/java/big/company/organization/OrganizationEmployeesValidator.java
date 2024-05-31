package big.company.organization;

import big.company.model.Employee;

import java.util.Collection;

public interface OrganizationEmployeesValidator {
    void validate(Collection<Employee> employees);
}
