package big.company.organization.impl;

import big.company.exception.BigCompanyApplicationException;
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

  private static final double SALARY_LOWER_BOUND_MULTIPLIER = 1.2;
  private static final double SALARY_UPPER_BOUND_MULTIPLIER = 1.5;

  private static final int MAX_REPORTING_LINE_LENGTH = 4;

  private static final String CEO_NOT_FOUND_MESSAGE = "CEO is not found";
  private static final String UNABLE_CALCULATE_AVERAGE_SALARY = "Cannot calculate average salaries for employee ids: %s";

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

  @Override
  public ReportingLineExcessLengthReport analyzeReportingLinesLength() {
    var report = new ReportingLineExcessLengthReport();
    analyze(getCeoId(), 0, report);
    return report;
  }

  private Employee getEmployee(Long id) {
    return employeeById.get(id);
  }

  private Optional<BigDecimal> calculateSalaryThresholdVariance(long managerId, long salary) {
    BigDecimal average = calculateSubordinateAverageSalary(getSubordinateIds(managerId));
    Range acceptableRange = computeAcceptableSalaryRange(average);
    return acceptableRange.calculateVariance(BigDecimal.valueOf(salary));
  }

  private BigDecimal calculateSubordinateAverageSalary(Set<Long> subordinateIds) {
    return subordinateIds.stream()
        .map(employeeById::get)
        .mapToLong(Employee::salary)
        .average()
        .stream().mapToObj(BigDecimal::valueOf)
        .findFirst()
        .orElseThrow(() -> new BigCompanyApplicationException(UNABLE_CALCULATE_AVERAGE_SALARY.formatted(subordinateIds)));
  }

  private Range computeAcceptableSalaryRange(BigDecimal average) {
    BigDecimal lowerBound = average.multiply(BigDecimal.valueOf(SALARY_LOWER_BOUND_MULTIPLIER));
    BigDecimal upperBound = average.multiply(BigDecimal.valueOf(SALARY_UPPER_BOUND_MULTIPLIER));
    return new Range(lowerBound, upperBound);
  }


  private Set<Long> getSubordinateIds(Long managerId) {
    return subordinatesByManager.getOrDefault(managerId, Collections.emptySet());
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
        .orElseThrow(() -> new BigCompanyApplicationException(CEO_NOT_FOUND_MESSAGE));
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
