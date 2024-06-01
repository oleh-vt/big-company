package big.company.output;

import big.company.report.PrintableReport;

public class SystemStandardOutputWriter implements OutputWriter {

  private final ConsoleOutputFormatter consoleOutputFormatter;

  public SystemStandardOutputWriter(ConsoleOutputFormatter consoleOutputFormatter) {
    this.consoleOutputFormatter = consoleOutputFormatter;
  }

  @Override
  public void write(PrintableReport report) {
    System.out.print(consoleOutputFormatter.format(report));
  }

}
