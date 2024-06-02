package big.company.output;

import big.company.report.ReportDto;

public interface ReportOutputWriter {

  void write(ReportDto report);

}
