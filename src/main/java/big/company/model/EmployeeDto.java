package big.company.model;

public record EmployeeDto(
        long id,
        String firstName,
        String lastName,
        double salary,
        Long managerId
) {
}
