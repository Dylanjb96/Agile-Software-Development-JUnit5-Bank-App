package BankTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite for the Banking Application.
 * 
 * Annotations Used:
 * {@link Suite}: Indicates that this class serves as a test suite, grouping
 * related tests.
 * {@link SelectClasses}: Specifies which test classes are included in this
 * suite.
 * {@link DisplayName}: Provides a user-friendly name for the suite, visible in
 * test reports or tools.
 */
@Suite // Declares this class as a JUnit test suite.
@SelectClasses({ AccountTest.class, OutstandingTest.class, BankAppTest.class, OutstandingInterestTest.class }) // Includes
                                                                                                               // tests
                                                                                                               // for
                                                                                                               // individual
                                                                                                               // account
// functionality, oustanding balances,
// related operations, overall bank
// application operations and limits.
@DisplayName("Banking App Test Suite") // Gives the suite a name for display in reports.
public class BankTestSuite {
    /**
     * This class does not contain any test logic.
     * Its purpose is to group and run the selected test classes as a suite.
     */
}
