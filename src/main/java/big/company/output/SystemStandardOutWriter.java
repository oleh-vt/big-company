package big.company.output;

import java.util.List;
import java.util.stream.Collectors;

public class SystemStandardOutWriter implements OutputWriter {

  private static final String ALIGN_LEFT = "%-";
  private static final String STR = "s";
  private static final String NEW_LINE = "%n";
  private static final String BLANK_SPACE = "  ";

  @Override
  public void write(List<List<?>> table) {
    System.out.print(buildReportPresentation(table));
  }

  private String buildReportPresentation(List<List<?>> table) {
    String rowFormat = createRowFormat(calculateColumnWidths(table));
    return table.stream()
        .map(row -> createPrintableLine(row, rowFormat))
        .collect(Collectors.joining());
  }

  private int[] calculateColumnWidths(List<List<?>> table) {
    int columns = table.get(0).size();
    int[] columnWidths = new int[columns];
    for (List<?> row : table) {
      for (int i = 0; i < columns; i++) {
        columnWidths[i] = Math.max(columnWidths[i], String.valueOf(row.get(i)).length());
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

  private String createPrintableLine(List<?> row, String rowFormat) {
    return rowFormat.formatted(row.toArray());
  }

}
