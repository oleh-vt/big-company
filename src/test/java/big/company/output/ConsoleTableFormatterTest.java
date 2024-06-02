package big.company.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import big.company.report.ReportDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConsoleTableFormatterTest {

  private static final String CR =  System.lineSeparator();

  private final ConsoleOutputFormatter consoleOutputFormatter = new ConsoleTableOutputFormatter();

  @DisplayName("Should create a well-formatted table with the title")
  @Test
  void format() {
    Object[][] data = new Object[3][];
    data[0] = new String[]{"COLUMN1", "COLUMN2", "COLUMN3"};
    data[1] = new String[]{"1", "very long value in this cell", "yes"};
    data[2] = new String[]{"2", "a bit shorter value", "maybe"};
    String title = "TABLE TITLE";

    var report = new ReportDto(title, data);

    var expectedTable = title + CR
        + "  COLUMN1    COLUMN2                         COLUMN3  " + CR
        + "  1          very long value in this cell    yes      " + CR
        + "  2          a bit shorter value             maybe    " + CR;

    String table = consoleOutputFormatter.format(report);

    assertEquals(expectedTable, table);

  }
}