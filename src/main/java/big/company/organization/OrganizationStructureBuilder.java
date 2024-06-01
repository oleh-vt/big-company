package big.company.organization;

import java.util.Collection;

public interface OrganizationStructureBuilder {

  Organization build(Collection<Employee> employees);
}
