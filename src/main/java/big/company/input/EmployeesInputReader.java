package big.company.input;

import big.company.model.Employee;
import java.nio.file.Path;
import java.util.List;

public interface EmployeesInputReader {

  List<Employee> read(Path path);
}
