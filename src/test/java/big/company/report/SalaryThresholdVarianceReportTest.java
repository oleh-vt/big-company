package big.company.report;

import static org.junit.jupiter.api.Assertions.*;

import big.company.organization.Employee;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SalaryThresholdVarianceReportTest {

  private static final String EXPECTED_TITLE = "MANAGERS WHICH EARN LESS OR MORE THAN THEY SHOULD";
  private static final String[] EXPECTED_HEADER_ROW = {"ID", "FIRST NAME", "LAST NAME", "MISPAYMENT", "VARIANCE"};

  private static final String UNDERPAID = "Underpaid";
  private static final String OVERPAID = "Overpaid";

  @DisplayName("Should export the report data for output")
  @Test
  void shouldExportReportDataForOutput() {
    SalaryThresholdVarianceReport report = new SalaryThresholdVarianceReport();
    var e1 = new Employee(1L, "John1", "Doe", null, null);
    var e2 = new Employee(2L, "John2", "Doe", null, null);
    double negativeVariance = -50;
    double positiveVariance = 100;
    report.addEntry(e1.id(), e1.firstName(), e1.lastName(), negativeVariance);
    report.addEntry(e2.id(), e2.firstName(), e2.lastName(), positiveVariance);
    Object[][] expectedDataTable = {
        EXPECTED_HEADER_ROW,
        new Object[]{e1.id(), e1.firstName(), e1.lastName(), UNDERPAID, Math.abs(negativeVariance)},
        new Object[]{e2.id(), e2.firstName(), e2.lastName(), OVERPAID, positiveVariance}
    };

    ReportDto reportDto = report.export();

    assertEquals(EXPECTED_TITLE, reportDto.title());
    assertTrue(Arrays.deepEquals(expectedDataTable, reportDto.dataTable()));
  }
}