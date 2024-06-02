package big.company.application;

import big.company.exception.BigCompanyApplicationException;
import big.company.input.EmployeesFileInputReader;
import big.company.organization.Organization;
import big.company.organization.OrganizationStructureBuilder;
import big.company.output.ConsoleOutputWriter;
import big.company.output.ReportOutputWriter;
import big.company.report.ReportingLineExcessLengthReport;
import big.company.report.SalaryThresholdVarianceReport;
import java.nio.file.Path;

public class Application {

  private static final String ERROR_MESSAGE_TEMPLATE = "Error: %s";

  private final EmployeesFileInputReader employeesFileInputReader;
  private final OrganizationStructureBuilder orgStructureBuilder;
  private final ReportOutputWriter reportOutputWriter;
  private final ConsoleOutputWriter consoleOutputWriter;

  public Application(
      EmployeesFileInputReader employeesFileInputReader,
      OrganizationStructureBuilder orgStructureBuilder,
      ReportOutputWriter reportOutputWriter,
      ConsoleOutputWriter consoleOutputWriter
  ) {
    this.employeesFileInputReader = employeesFileInputReader;
    this.orgStructureBuilder = orgStructureBuilder;
    this.reportOutputWriter = reportOutputWriter;
    this.consoleOutputWriter = consoleOutputWriter;
  }

  public void runAnalysis(String filePath) {
    try {
      run(Path.of(filePath));
    } catch (BigCompanyApplicationException e) {
      consoleOutputWriter.write(ERROR_MESSAGE_TEMPLATE.formatted(e.getMessage()));
    }
  }

  private void run(Path filePath) {
    Organization organization = orgStructureBuilder.build(employeesFileInputReader.read(filePath));

    SalaryThresholdVarianceReport managerSalaryVarianceReport = organization.analyzeManagerSalaries();
    ReportingLineExcessLengthReport reportingLineLengthExcessReport = organization.analyzeReportingLinesLength();

    reportOutputWriter.write(managerSalaryVarianceReport.export());
    reportOutputWriter.write(reportingLineLengthExcessReport.export());
  }

}
