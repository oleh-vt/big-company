package big.company.organization.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import big.company.organization.Employee;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BigCompanyOrgStructureBuilderTest {

  private BigCompanyOrgStructureBuilder bigCompanyOrgStructureBuilder;

  @DisplayName("Should create organization from the employees list")
  @Test
  void shouldCreateOrganizationFromEmployeeList() {
    var e1 = new Employee(1L, "John1", "Doe", 1000L, null);
    var e2 = new Employee(2L, "John2", "Doe", 1000L, 1L);
    var e3 = new Employee(3L, "John3", "Doe", 1000L, 1L);
    var e4 = new Employee(4L, "John4", "Doe", 1000L, 2L);
    var e5 = new Employee(5L, "John5", "Doe", 1000L, 3L);
    var e6 = new Employee(6L, "John6", "Doe", 1000L, 3L);
    List<Employee> employees = List.of(e1, e2, e3, e4, e5, e6);

    Map<Long, Set<Long>> orgStructure = Map.of(
        1L, Set.of(2L, 3L),
        2L, Set.of(4L),
        3L, Set.of(5L, 6L)
    );
    Map<Long, Employee> employeesById = Map.of(
        e1.id(), e1,
        e2.id(), e2,
        e3.id(), e3,
        e4.id(), e4,
        e5.id(), e5,
        e6.id(), e6
    );
    bigCompanyOrgStructureBuilder = new BigCompanyOrgStructureBuilder(e -> {
    });

    var expectedOrganization = new BigCompanyOrganization(employeesById, orgStructure);

    BigCompanyOrganization bigCompanyOrganization = bigCompanyOrgStructureBuilder.build(employees);

    assertEquals(bigCompanyOrganization, expectedOrganization);
  }

  @DisplayName("Should create organization from the employees list")
  @Test
  void shouldInvokeEmployeeValidatorBefore() {
    final AtomicBoolean invoked = new AtomicBoolean(false);
    bigCompanyOrgStructureBuilder = new BigCompanyOrgStructureBuilder(e -> invoked.set(true));

    List<Employee> employees = List.of(
        new Employee(1L, "John1", "Doe", 1000L, null),
        new Employee(2L, "John2", "Doe", 1000L, 1L)
    );

    bigCompanyOrgStructureBuilder.build(employees);

    assertTrue(invoked.get());
  }

}