package big.company.application;

import big.company.input.EmployeesCsvParser;
import big.company.input.EmployeesTextFileInputReader;
import big.company.organization.impl.BigCompanyEmployeesValidator;
import big.company.organization.impl.BigCompanyOrgStructureBuilder;
import big.company.output.ConsoleTableOutputFormatter;
import big.company.output.SystemStandardOutputWriter;

public class Initializer {

  private Initializer() {
  }

  public static Application initApplication() {
    var employeesInputReader = new EmployeesTextFileInputReader(new EmployeesCsvParser());
    var orgStructureBuilder = new BigCompanyOrgStructureBuilder(new BigCompanyEmployeesValidator());
    var outputWriter = new SystemStandardOutputWriter(new ConsoleTableOutputFormatter());

    return new Application(employeesInputReader, orgStructureBuilder, outputWriter);
  }
}
