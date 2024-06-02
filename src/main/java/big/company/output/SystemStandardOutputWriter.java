package big.company.output;

import big.company.report.ReportDto;

public class SystemStandardOutputWriter implements OutputWriter {

  private final ConsoleOutputFormatter consoleOutputFormatter;

  public SystemStandardOutputWriter(ConsoleOutputFormatter consoleOutputFormatter) {
    this.consoleOutputFormatter = consoleOutputFormatter;
  }

  @Override
  public void write(ReportDto report) {
    System.out.print(consoleOutputFormatter.format(report));
  }

}
