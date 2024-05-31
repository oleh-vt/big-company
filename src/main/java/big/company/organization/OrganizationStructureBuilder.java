package big.company.organization;

import big.company.model.Employee;
import big.company.organization.impl.BigCompanyOrganization;

import java.util.Collection;

public interface OrganizationStructureBuilder {
    BigCompanyOrganization build(Collection<Employee> employees);
}
