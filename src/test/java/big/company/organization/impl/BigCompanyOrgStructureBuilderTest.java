package big.company.organization.impl;

import big.company.model.Employee;
import big.company.organization.OrganizationEmployeesValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigCompanyOrgStructureBuilderTest {

    private BigCompanyOrgStructureBuilder bigCompanyOrgStructureBuilder;

    private final OrganizationEmployeesValidator validatorStub = employees -> {
    };

    @BeforeEach
    void setUp() {
        bigCompanyOrgStructureBuilder = new BigCompanyOrgStructureBuilder(validatorStub);
    }

    @DisplayName("Should create organization from the employees list")
    @Test
    void shouldCreateOrganizationFromEmployeeList() {
        var e1 = new Employee(1, "John1", "Doe", 1000, null);
        var e2 = new Employee(2, "John2", "Doe", 1000, 1L);
        var e3 = new Employee(3, "John3", "Doe", 1000, 1L);
        var e4 = new Employee(4, "John4", "Doe", 1000, 2L);
        var e5 = new Employee(5, "John5", "Doe", 1000, 3L);
        var e6 = new Employee(6, "John6", "Doe", 1000, 3L);
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

        var expectedOrganization = new BigCompanyOrganization(employeesById, orgStructure);

        BigCompanyOrganization bigCompanyOrganization = bigCompanyOrgStructureBuilder.build(employees);

        assertEquals(bigCompanyOrganization, expectedOrganization);
    }


}