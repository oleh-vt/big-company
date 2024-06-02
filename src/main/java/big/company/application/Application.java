package big.company.application;

import big.company.input.EmployeesFileInputReader;
import big.company.organization.Organization;
import big.company.organization.OrganizationStructureBuilder;
import big.company.output.SystemStandardOutputWriter;
import big.company.report.ReportingLineExcessLengthReport;
import big.company.report.SalaryThresholdVarianceReport;
import java.nio.file.Path;

public class Application {

  private final EmployeesFileInputReader employeesFileInputReader;
  private final OrganizationStructureBuilder orgStructureBuilder;
  private final SystemStandardOutputWriter outputWriter;

  public Application(
      EmployeesFileInputReader employeesFileInputReader,
      OrganizationStructureBuilder orgStructureBuilder,
      SystemStandardOutputWriter outputWriter
  ) {
    this.employeesFileInputReader = employeesFileInputReader;
    this.orgStructureBuilder = orgStructureBuilder;
    this.outputWriter = outputWriter;
  }

  public void runAnalysis(String filePath) {
    Organization organization = orgStructureBuilder.build(employeesFileInputReader.read(Path.of(filePath)));

    SalaryThresholdVarianceReport managerSalaryVarianceReport = organization.analyzeManagerSalaries();
    ReportingLineExcessLengthReport reportingLineLengthExcessReport = organization.analyzeReportingLinesLength();

    outputWriter.write(managerSalaryVarianceReport.export());
    outputWriter.write(reportingLineLengthExcessReport.export());
  }

}
