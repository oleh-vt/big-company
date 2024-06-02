package big.company.output;

import big.company.report.ReportDto;

public interface ConsoleOutputFormatter {

  String format(ReportDto report);

}
