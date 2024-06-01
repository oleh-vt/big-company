package big.company.output;

import big.company.report.Report;

public class SystemStandardOutputWriter implements OutputWriter {

  private final ConsoleFormatter consoleFormatter;

  public SystemStandardOutputWriter(ConsoleFormatter consoleFormatter) {
    this.consoleFormatter = consoleFormatter;
  }

  @Override
  public void write(Report report) {
    System.out.print(consoleFormatter.format(report));
  }

}
