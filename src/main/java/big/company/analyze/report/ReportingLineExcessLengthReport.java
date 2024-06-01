package big.company.analyze.report;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class ReportingLineExcessLengthReport {

  private final List<Entry> entries = new LinkedList<>();

  public void addEntry(Long employeeId, String firstName, String lastName, int reportingLineExcessLength) {
    entries.add(new Entry(employeeId, firstName, lastName, reportingLineExcessLength));
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
