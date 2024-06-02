package big.company.output;

import big.company.report.ReportDto;

public interface OutputWriter {

  void write(ReportDto report);

}
