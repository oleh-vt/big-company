package big.company.organization;

import big.company.analyze.report.ReportingLineExcessLengthReport;
import big.company.analyze.report.SalaryThresholdVarianceReport;

public interface Organization {

  SalaryThresholdVarianceReport analyzeManagerSalaries();

  ReportingLineExcessLengthReport analyzeReportingLinesLength();
}
