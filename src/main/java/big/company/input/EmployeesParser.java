package big.company.input;

import big.company.organization.Employee;
import java.util.List;

public interface EmployeesParser {

  List<Employee> parse(List<String> fileContent);

}
