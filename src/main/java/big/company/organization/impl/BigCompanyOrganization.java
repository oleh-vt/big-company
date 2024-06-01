package big.company.organization.impl;

import big.company.report.ReportingLineExcessLengthReport;
import big.company.report.SalaryThresholdVarianceReport;
import big.company.organization.Employee;
import big.company.organization.Organization;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class BigCompanyOrganization implements Organization {

  private static final double SALARY_LOWER_THRESHOLD = 1.2;
  private static final double SALARY_HIGHER_THRESHOLD = 1.5;

  private static final int MAX_REPORTING_LINE_LENGTH = 4;

  private final Map<Long, Employee> employeeById;
  private final Map<Long, Set<Long>> subordinatesByManager;

  BigCompanyOrganization(Map<Long, Employee> employeeById,
      Map<Long, Set<Long>> subordinatesByManager) {
    this.employeeById = employeeById;
    this.subordinatesByManager = subordinatesByManager;
  }

  @Override
  public SalaryThresholdVarianceReport analyzeManagerSalaries() {
    var report = new SalaryThresholdVarianceReport();
    for (Long managerId : subordinatesByManager.keySet()) {
      Employee manager = getEmployee(managerId);
      calculateSalaryThresholdVariance(managerId, manager.salary())
          .ifPresent(variance -> report.addEntry(manager.id(), manager.firstName(), manager.lastName(), variance.doubleValue()));
    }
    return report;
  }

  private Optional<BigDecimal> calculateSalaryThresholdVariance(long managerId, long salary) {
    return calculateVariance(salary,
        calculateSubordinateAverageSalary(getSubordinateIds(managerId)));
  }

  private Employee getEmployee(Long id) {
    return employeeById.get(id);
  }

  // consider implementing this using range class that returns variance from the range
  private Optional<BigDecimal> calculateVariance(long salary, double average) {
    BigDecimal actualSalary = BigDecimal.valueOf(salary);
    BigDecimal avg = BigDecimal.valueOf(average);
    BigDecimal lowerBound = avg.multiply(BigDecimal.valueOf(SALARY_LOWER_THRESHOLD));
    if (actualSalary.compareTo(lowerBound) < 0) {
      return Optional.of(actualSalary.subtract(lowerBound));
    }
    BigDecimal upperBound = avg.multiply(BigDecimal.valueOf(SALARY_HIGHER_THRESHOLD));
    if (actualSalary.compareTo(upperBound) > 0) {
      return Optional.of(actualSalary.subtract(upperBound));
    }
    return Optional.empty();
  }

  private Set<Long> getSubordinateIds(Long managerId) {
    return subordinatesByManager.getOrDefault(managerId, Collections.emptySet());
  }

  @Override
  public ReportingLineExcessLengthReport analyzeReportingLinesLength() {
    var report = new ReportingLineExcessLengthReport();
    analyze(getCeoId(), 0, report);
    return report;
  }

  private void analyze(long id, int depth, ReportingLineExcessLengthReport report) {
    int currentLength = depth - 1;
    if (currentLength > MAX_REPORTING_LINE_LENGTH) {
      Employee manager = getEmployee(id);
      report.addEntry(manager.id(), manager.firstName(), manager.lastName(), currentLength - MAX_REPORTING_LINE_LENGTH);
    }
    getSubordinateIds(id).forEach(subordinateId -> analyze(subordinateId, depth + 1, report));
  }


  private long getCeoId() {
    return employeeById.values().stream()
        .filter(e -> Objects.isNull(e.managerId()))
        .findFirst()
        .map(Employee::id)
        .orElseThrow(() -> new IllegalStateException("CEO is not found"));
  }


  private double calculateSubordinateAverageSalary(Set<Long> subordinateIds) {
    return subordinateIds.stream()
        .map(employeeById::get)
        .mapToLong(Employee::salary)
        .average()
        .orElseThrow(() -> new IllegalStateException(
            "Cannot calculate average salaries for employee ids: " + subordinateIds));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BigCompanyOrganization that = (BigCompanyOrganization) o;
    return employeeById.equals(that.employeeById) && subordinatesByManager.equals(
        that.subordinatesByManager);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeById, subordinatesByManager);
  }

  @Override
  public String toString() {
    return "BigCompanyOrganization{\n" +
        "employeeById=" + employeeById +
        "\nsubordinatesByManager=" + subordinatesByManager +
        "\n}";
  }
}
