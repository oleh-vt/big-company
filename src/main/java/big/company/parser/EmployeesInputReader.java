package big.company.parser;

import big.company.model.EmployeeDto;

import java.util.List;

public interface EmployeesInputReader {
    List<EmployeeDto> read();
}
