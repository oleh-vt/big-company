package big.company.organization;

public record Employee(
    Long id,
    String firstName,
    String lastName,
    Long salary,
    Long managerId
) {

}
