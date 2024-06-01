package big.company.analyze.report;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class SalaryThresholdVarianceReport {

  private final List<Entry> entries = new LinkedList<>();

  public void addEntry(Long employeeId, String firstName, String lastName, double salaryThresholdVariance) {
    entries.add(new Entry(employeeId, firstName, lastName, salaryThresholdVariance));
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
