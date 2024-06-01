package big.company.output;

import big.company.report.Report;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConsoleTableFormatter implements ConsoleFormatter {

  private static final String ALIGN_LEFT = "%-";
  private static final String STR = "s";
  private static final String NEW_LINE = "%n";
  private static final String BLANK_SPACE = "  ";

  @Override
  public String format(Report report) {
    return report.title()
        + System.lineSeparator()
        + buildTablePresentation(report.dataTable());
  }

  private String buildTablePresentation(Object[][] table) {
    String rowFormat = createRowFormat(calculateColumnWidths(table));
    return Arrays.stream(table)
        .map(rowFormat::formatted)
        .collect(Collectors.joining());
  }

  private int[] calculateColumnWidths(Object[][] table) {
    int columns = table[0].length;
    int[] columnWidths = new int[columns];
    for (Object[] row : table) {
      for (int i = 0; i < columns; i++) {
        columnWidths[i] = Math.max(columnWidths[i], String.valueOf(row[i]).length());
      }
    }
    return columnWidths;
  }

  private String createRowFormat(int[] columnWidths) {
    StringBuilder format = new StringBuilder();
    for (int width : columnWidths) {
      format.append(BLANK_SPACE)
          .append(ALIGN_LEFT)
          .append(width)
          .append(STR)
          .append(BLANK_SPACE);
    }
    return format.append(NEW_LINE).toString();
  }

}
