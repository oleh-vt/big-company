package big.company.model;

public record Employee(
        long id,
        String firstName,
        String lastName,
        double salary,
        Long managerId
) {
}
