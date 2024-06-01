package big.company.organization.impl;

import big.company.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigCompanyOrganizationTest {

    private BigCompanyOrganization bigCompanyOrganization;

    @DisplayName("Should find managers whose salary is lesser than 20% from the direct subordinates' average")
    @Test
    void analyzeManagerSalaries() {
        List<Employee> employees = createEmployees(new long[]{1100L, 1200L, 1000L, 800L});

        Map<Long, Set<Long>> orgStructure = Map.of(1L, Set.of(2L, 3L, 4L));
        Map<Long, Employee> employeesById = employees.stream()
                .collect(Collectors.toMap(Employee::id, Function.identity()));

        bigCompanyOrganization = new BigCompanyOrganization(employeesById, orgStructure);
        Set<BigCompanyOrganization.SalaryAnalysisResult> expectedResult = Set.of(
                new BigCompanyOrganization.SalaryAnalysisResult(1L, "John1", "Doe", -100)
        );

        List<BigCompanyOrganization.SalaryAnalysisResult> results = bigCompanyOrganization.analyzeManagerSalaries();

        assertEquals(expectedResult, Set.copyOf(results));
    }

    @DisplayName("Should find managers whose salary is greater than 50% from the direct subordinates' average")
    @Test
    void analyzeManagerSalaries2() {
        List<Employee> employees = createEmployees(new long[]{1800L, 1200L, 1000L, 800L});

        Map<Long, Set<Long>> orgStructure = Map.of(1L, Set.of(2L, 3L, 4L));
        Map<Long, Employee> employeesById = employees.stream()
                .collect(Collectors.toMap(Employee::id, Function.identity()));

        bigCompanyOrganization = new BigCompanyOrganization(employeesById, orgStructure);
        Set<BigCompanyOrganization.SalaryAnalysisResult> expectedResult = Set.of(
                new BigCompanyOrganization.SalaryAnalysisResult(1L, "John1", "Doe", 300)
        );

        List<BigCompanyOrganization.SalaryAnalysisResult> results = bigCompanyOrganization.analyzeManagerSalaries();

        assertEquals(expectedResult, Set.copyOf(results));
    }

    @DisplayName("When managers' salary is greater from the direct subordinates' average, but in inclusive range 20%-50%, should return empty report")
    @ParameterizedTest
    @ValueSource(longs = {1200L, 1350L, 1500L})
    void analyzeManagerSalaries3(long managerSalary) {
        List<Employee> employees = createEmployees(new long[]{managerSalary, 1200, 1000, 800});

        Map<Long, Set<Long>> orgStructure = Map.of(1L, Set.of(2L, 3L, 4L));
        Map<Long, Employee> employeesById = employees.stream()
                .collect(Collectors.toMap(Employee::id, Function.identity()));

        bigCompanyOrganization = new BigCompanyOrganization(employeesById, orgStructure);

        List<BigCompanyOrganization.SalaryAnalysisResult> results = bigCompanyOrganization.analyzeManagerSalaries();

        assertTrue(results.isEmpty());
    }

    private List<Employee> createEmployees(long[] employeeSalaries) {
        var firstName = "John";
        var lastName = "Doe";
        List<Employee> employees = new ArrayList<>(employeeSalaries.length);
        for (int i = 0, id = 1; i < employeeSalaries.length; i++, id++) {
            Long managerId = i == 0 ? null : 1L;
            employees.add(new Employee(id, firstName + id, lastName, employeeSalaries[i], managerId));
        }
        return employees;
    }

    @DisplayName("Should find all employees whose reporting line length is greater than 4")
    @Test
    void analyzeStructure() {
        Map<Long, Employee> employeesById = LongStream.rangeClosed(1, 10)
                .mapToObj(id -> new Employee(id, null, null, 0, null))
                .collect(Collectors.toMap(Employee::id, Function.identity()));
        Map<Long, Set<Long>> orgStructure = Map.of(
                1L, Set.of(2L, 3L),
                2L, Set.of(4L),
                3L, Set.of(5L),
                5L, Set.of(6L),
                6L, Set.of(7L, 10L),
                7L, Set.of(8L),
                8L, Set.of(9L)
        );
        bigCompanyOrganization = new BigCompanyOrganization(employeesById, orgStructure);

        List<BigCompanyOrganization.StructureAnalysisReport> reports = bigCompanyOrganization.analyzeStructure();

        assertTrue(reports.size() == 1);

    }
}