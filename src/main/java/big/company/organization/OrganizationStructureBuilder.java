package big.company.organization;

import big.company.model.Employee;
import java.util.Collection;

public interface OrganizationStructureBuilder {

  Organization build(Collection<Employee> employees);
}
