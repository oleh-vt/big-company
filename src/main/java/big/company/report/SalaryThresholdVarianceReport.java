package big.company.report;

import big.company.output.OutputWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class SalaryThresholdVarianceReport implements Printable {

  private static final List<String> HEADER_ROW = List.of(
      "Manager Id", "First Name", "Last Name", "Salary threshold variance"
  );

  private final List<Entry> entries = new LinkedList<>();

  public void addEntry(Long employeeId, String firstName, String lastName, double salaryThresholdVariance) {
    entries.add(new Entry(employeeId, firstName, lastName, salaryThresholdVariance));
  }

  @Override
  public void print(OutputWriter writer) {
    List<List<?>> rows = new ArrayList<>();
    rows.add(HEADER_ROW);
    entries.forEach(e -> {
      List<Object> row = new ArrayList<>();
      row.add(e.employeeId());
      row.add(e.firstName());
      row.add(e.lastName());
      row.add(e.salaryThresholdVariance());
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
    SalaryThresholdVarianceReport that = (SalaryThresholdVarianceReport) o;
    return entries.equals(that.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries);
  }

  @Override
  public String toString() {
    return "SalaryThresholdVarianceReport{" +
        "entries=" + entries +
        '}';
  }

  private record Entry(Long employeeId, String firstName, String lastName, double salaryThresholdVariance) {

  }
}
