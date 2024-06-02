package big.company.organization.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class RangeTest {

  private static final int LOWER_BOUND_VALUE = 100;
  private static final int UPPER_BOUND_VALUE = 200;

  private Range range;

  @BeforeEach
  void setUp() {
    range = new Range(new BigDecimal(LOWER_BOUND_VALUE), new BigDecimal(UPPER_BOUND_VALUE));
  }

  @DisplayName("When the upper bound is greater than lower bound, should create the range object")
  @Test
  void shouldCreateRangeObject() {
    var lowerBound = BigDecimal.valueOf(LOWER_BOUND_VALUE);
    var upperBound = BigDecimal.valueOf(UPPER_BOUND_VALUE);

    Range actualRange = assertDoesNotThrow(() -> new Range(lowerBound, upperBound));

    assertEquals(range, actualRange);
  }

  @DisplayName("When the lower bound is greater or equal to the upper bound, should throw an exception")
  @ParameterizedTest
  @CsvSource({
      "200, 100",
      "200, 200"
  })
  void shouldThrowExceptionWhenLowerBoundIsGreaterOrEqualToUpperBound(long lowerBound, long upperBound) {
    assertThrows(IllegalArgumentException.class, () -> new Range(new BigDecimal(lowerBound), new BigDecimal(upperBound)));

  }

  @DisplayName("When the value is below lower bound, should return difference between the value and the lower bound")
  @Test
  void shouldCalculateVarianceWhenValueIsBelowLowerBound() {
    int expectedVariance = -50;
    BigDecimal value = new BigDecimal(LOWER_BOUND_VALUE - Math.abs(expectedVariance));

    Optional<BigDecimal> variance = range.calculateVariance(value);

    assertTrue(variance.isPresent());
    assertEquals(new BigDecimal(expectedVariance), variance.get());
  }

  @DisplayName("When the value is above upper bound, should return difference between the value and the upper bound")
  @Test
  void shouldCalculateVarianceWhenValueIsAboveUpperBound() {
    int expectedVariance = 50;
    BigDecimal value = new BigDecimal(UPPER_BOUND_VALUE + expectedVariance);

    Optional<BigDecimal> variance = range.calculateVariance(value);

    assertTrue(variance.isPresent());
    assertEquals(new BigDecimal(expectedVariance), variance.get());
  }

  @DisplayName("When the value withing bounds or at the bound, should return empty optional")
  @ParameterizedTest
  @ValueSource(ints = {LOWER_BOUND_VALUE, 150, UPPER_BOUND_VALUE})
  void testCalculateVarianceWithinBounds(int value) {
    BigDecimal salary = new BigDecimal(value);

    Optional<BigDecimal> variance = range.calculateVariance(salary);

    assertTrue(variance.isEmpty());
  }

}