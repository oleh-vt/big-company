package big.company.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReportingLineExcessLengthReportTest {

  private static final String EXPECTED_TITLE = "EMPLOYEES WITH TOO LONG REPORTING LINE";
  private static final String[] EXPECTED_HEADER_ROW = {"ID", "FIRST NAME", "LAST NAME", "REPORTING LINE EXCESS"};

  @DisplayName("Should export the report data for output")
  @Test
  void shouldExportReportDataForOutput() {
    ReportingLineExcessLengthReport report = new ReportingLineExcessLengthReport();
    long employeeId = 1;
    String firstName = "John";
    String lastName = "Doe";
    int lineExcessLength = 2;
    report.addEntry(employeeId, firstName, lastName, lineExcessLength);
    Object[][] expectedDataTable = {
        EXPECTED_HEADER_ROW,
        new Object[]{employeeId, firstName, lastName, lineExcessLength}
    };

    ReportDto reportDto = report.export();

    assertEquals(EXPECTED_TITLE, reportDto.title());
    assertTrue(Arrays.deepEquals(expectedDataTable, reportDto.dataTable()));
  }
}