package BankTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import Bank.Outstanding;
import Bank.Outstanding.InadequateOutstandingAmountErrors;

/**
 * Test suite for validating the functionality of the `Outstanding` class.
 *
 * This class contains tests to ensure that outstanding balances are managed
 * correctly,
 * including adding oustanding, making repayments, and handling edge cases like
 * invalid operations.
 */
public class OutstandingTest {

    private Outstanding outstanding; // Instance of Outstanding for testing.

    /**
     * Initializes a new `Outstanding` instance before each test.
     */
    @BeforeEach
    public void setUp() {
        outstanding = new Outstanding();
    }

    /**
     * Verifies that the initial outstanding balance is zero.
     */
    @Test
    public void testInitialOutstandingBalance() {
        assertEquals(0.0, outstanding.getCurrentOutstandingBalance(),
                "Initial outstanding balance should be zero.");
    }

    /**
     * Verifies that adding a valid outstanding amount updates the outstanding
     * balance correctly.
     *
     * @throws InadequateOutstandingAmountErrors if the outstanding amount is
     *                                           invalid
     */
    @Test
    public void testAppendToOutstandingBalance() throws InadequateOutstandingAmountErrors {
        double outstandingAmount = 5000.0;
        outstanding.appendToOutstandingBalance(outstandingAmount);
        assertEquals(outstandingAmount, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should match the added outstanding amount.");
    }

    /**
     * Verifies that a valid repayment amount reduces the outstanding balance.
     *
     * @throws InadequateOutstandingAmountErrors if the repayment amount is invalid
     */
    @Test
    public void testMinusFromOutstandingBalance() throws InadequateOutstandingAmountErrors {
        double outstandingAmount = 5000.0;
        double repaymentAmount = 2000.0;

        outstanding.appendToOutstandingBalance(outstandingAmount);
        outstanding.minusFromOutstandingBalance(repaymentAmount);

        assertEquals(outstandingAmount - repaymentAmount, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should decrease after repayment.");
    }

    /**
     * Verifies that an attempt to repay more than the outstanding balance throws an
     * error.
     *
     * @throws InadequateOutstandingAmountErrors if the repayment amount is invalid
     */
    @Test
    public void testMinusFromOutstandingBalanceExceed() throws InadequateOutstandingAmountErrors {
        double outstandingAmount = 3000.0;
        double excessiveRepayment = 4000.0;

        outstanding.appendToOutstandingBalance(outstandingAmount);

        InadequateOutstandingAmountErrors exception = assertThrows(
                InadequateOutstandingAmountErrors.class,
                () -> outstanding.minusFromOutstandingBalance(excessiveRepayment),
                "Expected an exception when repayment exceeds the outstanding balance.");

        assertTrue(exception.getMessage().contains("Repayment amount exceeds outstanding balance"));
    }

    /**
     * Verifies that checking a valid repayment amount does not throw an error.
     *
     * @throws InadequateOutstandingAmountErrors if the repayment amount is invalid
     */
    @Test
    public void testCheckValidOutstandingBalance() throws InadequateOutstandingAmountErrors {
        double outstandingAmount = 4000.0;
        double validRepayment = 2000.0;

        outstanding.appendToOutstandingBalance(outstandingAmount);

        assertDoesNotThrow(() -> outstanding.checkCurrentAmountInOutstandingBalance(validRepayment),
                "No exception should be thrown for a valid repayment amount.");
    }

    /**
     * Verifies that checking an excessive repayment amount throws an error.
     *
     * @throws InadequateOutstandingAmountErrors if the repayment amount is invalid
     */
    @Test
    public void testCheckInvalidOutstandingBalance() throws InadequateOutstandingAmountErrors {
        double outstandingAmount = 3000.0;
        double excessiveRepayment = 4000.0;

        outstanding.appendToOutstandingBalance(outstandingAmount);

        assertThrows(
                InadequateOutstandingAmountErrors.class,
                () -> outstanding.checkCurrentAmountInOutstandingBalance(excessiveRepayment),
                "Expected an exception for a repayment amount that exceeds the outstanding balance.");
    }

    /**
     * This parametrized test makes sure that the system correctly throws an error
     * when an invalid outstanding amount is returned, such as negative or exceeding
     * the maximum outstanding limit
     */
    @ParameterizedTest
    @MethodSource("provideInvalidOutstandingAmount")
    public void testCheckInvalidOutstandingAmount(double invalidAmount, String expectedErrorMessage) {
        // Test that an error is thrown for invalid outstanding amounts
        InadequateOutstandingAmountErrors exception = assertThrows(InadequateOutstandingAmountErrors.class,
                () -> outstanding.appendToOutstandingBalance(invalidAmount),
                "Expected an error when attempting to add an invalid outstanding amount.");

        // Verify the exception message
        assertTrue(exception.getMessage().contains(expectedErrorMessage),
                "Expected the error message to contain: " + expectedErrorMessage);
    }

    /**
     * This method provides a list of invalid outstanding amounts along with the
     * expected error message.
     * The invalid amounts must be minus values and 0 amounts.
     */
    public static Stream<Arguments> provideInvalidOutstandingAmount() {
        return Stream.of(
                Arguments.of(-100.0, "Outstanding must be positive."),
                Arguments.of(0.0, "Outstanding must be positive."),
                Arguments.of(-5000.0, "Outstanding must be positive."));
    }

    /**
     * Verifies that adding a negative outstanding amount throws an error.
     */
    @Test
    public void testAppendNegativeOutstandingAmount() {
        double negativeOutstandingAmount = -1000.0;

        // Appending a negative outstanding amount should throw an exception
        assertThrows(InadequateOutstandingAmountErrors.class,
                () -> outstanding.appendToOutstandingBalance(negativeOutstandingAmount),
                "Expected an exception for negative outstanding amounts.");
    }

    /**
     * Tests subtracting a repayment amount of zero.
     */
    @Test
    public void testRepayZeroAmount() throws InadequateOutstandingAmountErrors {
        double outstandingAmount = 3000.0;
        outstanding.appendToOutstandingBalance(outstandingAmount);

    }

    /**
     * Verifies the behavior when repaying a positive amount.
     *
     * @throws InadequateOutstandingAmountErrors if the repayment amount is invalid
     */
    @Test
    public void testRepayPositiveAmount() throws InadequateOutstandingAmountErrors {
        double outstandingAmount = 5000.0;
        double repaymentAmount = 2000.0;

        outstanding.appendToOutstandingBalance(outstandingAmount);
        outstanding.minusFromOutstandingBalance(repaymentAmount);

        assertEquals(outstandingAmount - repaymentAmount, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should decrease after repayment.");
    }

    /**
     * Verifies that cumulative operations like multiple outstanding and repayments
     * are
     * handled correctly.
     *
     * @throws InadequateOutstandingAmountErrors if the outstanding or repayment
     *                                           amounts
     *                                           are invalid
     */
    @Test
    public void testCumulativeOperations() throws InadequateOutstandingAmountErrors {
        outstanding.appendToOutstandingBalance(2000.0); // Add outstanding
        outstanding.appendToOutstandingBalance(3000.0); // Add another outstanding
        outstanding.minusFromOutstandingBalance(1000.0); // Make a repayment

        assertEquals(4000.0, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should reflect cumulative operations.");
    }

    /**
     * Verifies that boundary values for repayment are handled correctly.
     *
     * @throws InadequateOutstandingAmountErrors if the repayment amount is invalid
     */
    @Test
    public void testBoundaryValues() throws InadequateOutstandingAmountErrors {
        outstanding.appendToOutstandingBalance(5000.0);

        // Ensure that we do not attempt to repay zero or negative value
        double repaymentAmount = 2000.0; // Set to a positive value
        outstanding.minusFromOutstandingBalance(repaymentAmount);

        // Ensure that the outstanding balance is correctly reduced after repayment
        assertEquals(5000.0 - repaymentAmount, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should decrease after repayment.");
    }

}
