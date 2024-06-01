package big.company.organization;

import big.company.report.ReportingLineExcessLengthReport;
import big.company.report.SalaryThresholdVarianceReport;

public interface Organization {

  SalaryThresholdVarianceReport analyzeManagerSalaries();

  ReportingLineExcessLengthReport analyzeReportingLinesLength();
}
