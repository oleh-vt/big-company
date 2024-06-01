package big.company.report;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class ReportingLineExcessLengthReport {

  private static final String TITLE = "EMPLOYEES WITH TOO LONG REPORTING LINE";
  private static final String[] HEADER_ROW = {"ID", "FIRST NAME", "LAST NAME", "REPORTING LINE EXCESS"};

  private final List<Entry> entries = new LinkedList<>();

  public void addEntry(Long employeeId, String firstName, String lastName, int reportingLineExcessLength) {
    entries.add(new Entry(employeeId, firstName, lastName, reportingLineExcessLength));
  }

  public Report export() {
    return new Report(TITLE, createDataTable());
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
        entry.reportingLineExcessLength()
    };
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
