package big.company.report;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class SalaryThresholdVarianceReport {

  private static final String UNDERPAID = "Underpaid";
  private static final String OVERPAID = "Overpaid";
  private static final String NONE = "";

  private static final String TITLE = "MANAGERS WHICH EARN LESS OR MORE THAN THEY SHOULD";
  private static final String[] HEADER_ROW = {"ID", "FIRST NAME", "LAST NAME", "MISPAYMENT", "VARIANCE"};

  private final List<Entry> entries = new LinkedList<>();

  public void addEntry(Long employeeId, String firstName, String lastName, double salaryThresholdVariance) {
    entries.add(new Entry(employeeId, firstName, lastName, salaryThresholdVariance));
  }

  public ReportDto export() {
    return new ReportDto(TITLE, createDataTable());
  }


  private Object[][] createDataTable() {
    Object[][] dataTable = new Object[entries.size() + 1][];
    addHeaderRow(dataTable);
    for (int rowNumber = 1; rowNumber < dataTable.length; rowNumber++) {
      addRow(dataTable, rowNumber, createRow(entries.get(rowNumber - 1)));
    }
    return dataTable;
  }

  private void addHeaderRow(Object[][] dataTable) {
    dataTable[0] = Arrays.copyOf(HEADER_ROW, HEADER_ROW.length);
  }

  private void addRow(Object[][] dataTable, int rowNumber, Object[] row) {
    dataTable[rowNumber] = row;
  }

  private Object[] createRow(Entry entry) {
    return new Object[]{
        entry.employeeId(),
        entry.firstName(),
        entry.lastName(),
        getMispayment(entry.salaryThresholdVariance()),
        Math.abs(entry.salaryThresholdVariance())
    };
  }

  private String getMispayment(double variance) {
    int sign = BigDecimal.valueOf(variance).signum();
    if (sign == 0) {
      return NONE;
    }
    if (sign > 0) {
      return OVERPAID;
    } else {
      return UNDERPAID;
    }
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
