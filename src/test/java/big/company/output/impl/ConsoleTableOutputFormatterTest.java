package big.company.output.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import big.company.output.ConsoleOutputFormatter;
import big.company.report.ReportDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConsoleTableOutputFormatterTest {

  public static final String TITLE = "TABLE TITLE";
  private static final String CR = System.lineSeparator();

  private final ConsoleOutputFormatter consoleOutputFormatter = new ConsoleTableOutputFormatter();

  @DisplayName("Should create a well-formatted table with the title")
  @Test
  void shouldCreateWellFormattedTableWithTitle() {
    Object[][] data = new Object[][]{
        new String[]{"COLUMN1", "COLUMN2", "COLUMN3"},
        new String[]{"1", "very long value in this cell", "yes"},
        new String[]{"2", "a bit shorter value", "maybe"}
    };
    var report = new ReportDto(TITLE, data);

    var expectedTable = TITLE + CR
        + "  COLUMN1    COLUMN2                         COLUMN3  " + CR
        + "  1          very long value in this cell    yes      " + CR
        + "  2          a bit shorter value             maybe    " + CR;

    String table = consoleOutputFormatter.format(report);

    assertEquals(expectedTable, table);
  }

  @DisplayName("When no data to display, should show title and 'no content'")
  @Test
  void shouldShowNoContentWhenNoDataToDisplay() {
    Object[][] data = new Object[][]{
        new String[]{"COLUMN1", "COLUMN2", "COLUMN3"}
    };
    var report = new ReportDto(TITLE, data);

    var expectedTable = TITLE + CR
        + "  COLUMN1    COLUMN2    COLUMN3  " + CR
        + "  <no content>" + CR;

    String table = consoleOutputFormatter.format(report);

    assertEquals(expectedTable, table);
  }
}