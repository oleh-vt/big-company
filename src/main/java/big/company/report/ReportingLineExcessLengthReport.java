package big.company.report;

import big.company.output.OutputWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class ReportingLineExcessLengthReport implements Printable {

  private static final List<String> HEADER_ROW = List.of(
      "Manager Id", "First Name", "Last Name", "Reporting Line Excess"
  );

  private final List<Entry> entries = new LinkedList<>();

  public void addEntry(Long employeeId, String firstName, String lastName, int reportingLineExcessLength) {
    entries.add(new Entry(employeeId, firstName, lastName, reportingLineExcessLength));
  }

  @Override
  public void print(OutputWriter writer) {
    List<List<?>> rows = new ArrayList<>();
    rows.add(HEADER_ROW);
    entries.forEach(e -> {
      List<String> row = new ArrayList<>();
      row.add(String.valueOf(e.employeeId()));
      row.add(e.firstName());
      row.add(e.lastName());
      row.add(String.valueOf(e.reportingLineExcessLength()));
      rows.add(row);
    });
    writer.write(rows);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportingLineExcessLengthReport that = (ReportingLineExcessLengthReport) o;
    return entries.equals(that.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries);
  }

  @Override
  public String toString() {
    return "ReportingLineExcessLengthReport{" +
        "entries=" + entries +
        '}';
  }

  private record Entry(Long employeeId, String firstName, String lastName, int reportingLineExcessLength) {

  }
}
