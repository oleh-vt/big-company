package big.company.output;

import big.company.report.ReportDto;

public class SystemStandardOutputReportWriter implements ReportOutputWriter {

  private final ConsoleOutputFormatter consoleOutputFormatter;
  private final ConsoleOutputWriter consoleOutputWriter;

  public SystemStandardOutputReportWriter(ConsoleOutputFormatter consoleOutputFormatter,
      ConsoleOutputWriter consoleOutputWriter) {
    this.consoleOutputFormatter = consoleOutputFormatter;
    this.consoleOutputWriter = consoleOutputWriter;
  }

  @Override
  public void write(ReportDto report) {
    consoleOutputWriter.write(consoleOutputFormatter.format(report));
  }

}
