package big.company.organization.impl;

import big.company.model.Employee;
import big.company.organization.Organization;

import java.math.BigDecimal;
import java.util.*;

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
  public List<SalaryAnalysisResult> analyzeManagerSalaries() {
    List<SalaryAnalysisResult> results = new ArrayList<>();
    for (Long managerId : subordinatesByManager.keySet()) {
      Employee manager = getManager(managerId);
      calculateSalaryThresholdVariance(managerId, manager.salary())
          .map(v -> new SalaryAnalysisResult(manager.id(), manager.firstName(), manager.lastName(), v.doubleValue()))
          .ifPresent(results::add);
    }
    return List.copyOf(results);
  }

  private Optional<BigDecimal> calculateSalaryThresholdVariance(long managerId, long salary) {
    return calculateVariance(salary,
        calculateSubordinateAverageSalary(getSubordinateIds(managerId)));
  }

  private Employee getManager(Long managerId) {
    return employeeById.get(managerId);
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
  public List<StructureAnalysisReport> analyzeStructure() {
    List<StructureAnalysisReport> reports = new ArrayList<>();
    analyzeOrgStructure(getCeoId(), 0, reports);
    return reports;
  }

  private void analyzeOrgStructure(long id, int depth, List<StructureAnalysisReport> reports) {
    int currentLength = depth - 1;
    if (currentLength > MAX_REPORTING_LINE_LENGTH) {
      reports.add(new StructureAnalysisReport(getManager(id), currentLength));
    }
    if (hasSubordinates(id)) {
      getSubordinateIds(id)
          .forEach(subordinateId -> analyzeOrgStructure(subordinateId, depth + 1, reports));
    }

  }

  private boolean hasSubordinates(long id) {
    return subordinatesByManager.containsKey(id);
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

  public record SalaryAnalysisResult(
      long employeeId,
      String firstName,
      String lastName,
      double salaryDelta
  ) {

  }

  public record StructureAnalysisReport(Employee employee, int distance) {

  }

}
