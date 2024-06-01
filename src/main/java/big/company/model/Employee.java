package big.company.model;

public record Employee(
        long id,
        String firstName,
        String lastName,
        long salary,
        Long managerId
) {
}
