package big.company.input;

import big.company.organization.Employee;
import java.nio.file.Path;
import java.util.List;

public interface EmployeesFileInputReader {

  List<Employee> read(Path path);
}
