package randoop.test;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** A set of checks, including at most one ExceptionCheck. */
public class RegressionChecks implements TestChecks<RegressionChecks> {

  private Set<Check> checks;
  private ExceptionCheck exceptionCheck;

  /** Create an empty set of regression checks. */
  public RegressionChecks() {
    this.checks = new LinkedHashSet<>();
    this.exceptionCheck = null;
  }

  @Override
  public int count() {
    int result = checks.size();
    if (exceptionCheck != null) {
      result++;
    }
    return result;
  }

  /**
   * Adds the given check to the sequence.
   *
   * @throws IllegalArgumentException if the argument's class is {@code ExceptionCheck} and this
   *     already contains such a check
   */
  @Override
  public void add(Check check) {
    if (check instanceof ExceptionCheck) {
      if (exceptionCheck != null) {
        throw new IllegalArgumentException(
            "Sequence already has a check"
                + " of type "
                + exceptionCheck.getClass().getCanonicalName());
      }
      exceptionCheck = (ExceptionCheck) check;
    } else {
      checks.add(check);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return map of non-exception checks in this object. each mapped to true, indicating it is
   *     passing
   */
  @Override
  public Set<Check> checks() {
    return checks;
  }

  /**
   * {@inheritDoc}
   *
   * @return true if there are regression checks or an expected exception, false otherwise
   */
  @Override
  public boolean hasChecks() {
    return (!checks.isEmpty() || exceptionCheck != null);
  }

  /**
   * {@inheritDoc}
   *
   * @return false, since all regression checks are passing
   */
  @Override
  public boolean hasErrorBehavior() {
    return false;
  }

  @Override
  public ExceptionCheck getExceptionCheck() {
    return exceptionCheck;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof RegressionChecks)) {
      return false;
    }
    RegressionChecks cks = (RegressionChecks) obj;
    if (!checks.equals(cks.checks)) {
      return false;
    }
    if (exceptionCheck == null) {
      return (cks.exceptionCheck == null);
    }
    return cks.exceptionCheck != null && exceptionCheck.equals(cks.exceptionCheck);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checks, exceptionCheck);
  }

  @Override
  public RegressionChecks commonChecks(RegressionChecks other) {
    RegressionChecks common = new RegressionChecks();
    for (Check ck : checks) {
      if (other.checks.contains(ck)) {
        common.add(ck);
      }
    }
    if (exceptionCheck.equals(other.exceptionCheck)) {
      common.add(exceptionCheck);
    }
    return common;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Returns false because regression checks are not invalid.
   *
   * @return false, always
   */
  @Override
  public boolean hasInvalidBehavior() {
    return false;
  }
}
