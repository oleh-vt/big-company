package big.company.organization.impl;

import java.math.BigDecimal;
import java.util.Optional;

record Range(BigDecimal lowerBound, BigDecimal upperBound) {

  Range {
    if (lowerBound.compareTo(upperBound) >= 0) {
      throw new IllegalArgumentException("Lower bound is greater or equal to upper bound");
    }
  }

  Optional<BigDecimal> calculateVariance(BigDecimal salary) {
    if (salary.compareTo(lowerBound) < 0) {
      return Optional.of(salary.subtract(lowerBound));
    }
    if (salary.compareTo(upperBound) > 0) {
      return Optional.of(salary.subtract(upperBound));
    }
    return Optional.empty();
  }

}
