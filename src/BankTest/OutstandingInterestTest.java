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
import Bank.Outstanding.OutstandingInterestError;

/**
 * Test suite for validating interest application logic on outstanding balances.
 * 
 * This test class ensures the `Outstanding` class behaves as expected under
 * various conditions when applying interest rates to an outstanding balance.
 * 
 */
public class OutstandingInterestTest {

    private Outstanding outstanding; // Instance of the Outstanding class for testing.

    /**
     * Sets up a new `Outstanding` instance before each test is executed.
     */
    @BeforeEach
    public void setingUp() {
        outstanding = new Outstanding();
    }

    /**
     * Verifies that applying a valid interest rate successfully updates the
     * outstanding balance.
     *
     * @throws InadequateOutstandingAmountErrors if the balance is invalid
     * @throws OutstandingInterestError          if the interest rate is invalid
     */
    @Test
    public void testApplyInterest() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        // Set initial balance
        double initialOutstanding = 10_000.0; // Initial balance of 10,000
        double interestRate = 5.0; // 5% interest rate

        // Add the initial balance to the outstanding account
        outstanding.appendToOutstandingBalance(initialOutstanding); // Apply balance

        // Apply interest
        outstanding.applyInterest(interestRate);

        // Calculate the expected balance after interest is applied
        double expectedBalance = initialOutstanding * (1 + interestRate / 100);

        // Assert that the balance after applying interest is as expected
        assertEquals(expectedBalance, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should reflect 5% interest.");
    }

    /**
     * Tests that interest payable correctly on an outstanding balance.
     */
    @Test
    public void testInterestPayable() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        double initialOutstanding = 10000.0;
        double interestRate = 5.0; // 5% interest

        outstanding.appendToOutstandingBalance(initialOutstanding);
        outstanding.applyInterest(interestRate);

        double expectedBalance = initialOutstanding * (1 + interestRate / 100);
        assertEquals(expectedBalance, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should include accrued interest.");
    }

    /**
     * Verifies that applying a 0% interest rate leaves the outstanding balance
     * unchanged.
     */
    @Test
    public void testZeroInterestRates() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        double initialOutstanding = 10000.0;

        outstanding.appendToOutstandingBalance(initialOutstanding);
        outstanding.applyInterest(0.0);

        assertEquals(initialOutstanding, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should remain unchanged with 0% interest.");
    }

    /**
     * Verifies that applying a negative interest rate reduces the outstanding
     * balance.
     */
    @Test
    public void testNegativeInterestRates() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        double initialOutstanding = 10000.0;
        double interestRate = -5.0; // Negative 5% interest

        outstanding.appendToOutstandingBalance(initialOutstanding);
        outstanding.applyInterest(interestRate);

        double expectedBalance = initialOutstanding * (1 + interestRate / 100);
        assertEquals(expectedBalance, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should decrease with negative interest.");
    }

    /**
     * Verifies that applying interest multiple times compounds the outstanding
     * balance.
     */
    @Test
    public void testRepeatedInterest() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        double initialOutstanding = 10000.0;
        double interestRate = 5.0;

        outstanding.appendToOutstandingBalance(initialOutstanding);
        outstanding.applyInterest(interestRate); // First application of interest
        outstanding.applyInterest(interestRate); // Second application of interest

        double firstPayable = initialOutstanding * (1 + interestRate / 100);
        double secondAccrual = firstPayable * (1 + interestRate / 100);

        assertEquals(secondAccrual, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should reflect compound interest.");
    }

    /**
     * Verifies that applying interest to a zero balance throws an error.
     */
    @Test
    public void testInterestRateOnZeroBalance() {
        outstanding.setBalance(0); // Set balance to zero

        // Expect the error to be thrown when applying interest on a zero balance
        OutstandingInterestError exception = assertThrows(
                OutstandingInterestError.class,
                () -> outstanding.applyInterest(5.0),
                "Applying interest to a zero balance should throw an exception.");

        assertTrue(exception.getMessage().contains("Cannot apply interest to a zero balance"));
    }

    /**
     * Verifies that an extremely high valid interest rate is processed correctly.
     */
    @Test
    public void testHighInterestRates() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        double initialOutstanding = 1000.0;
        double interestRate = 100.0; // 100% interest

        outstanding.appendToOutstandingBalance(initialOutstanding);
        outstanding.applyInterest(interestRate);

        double expectedBalance = initialOutstanding * (1 + interestRate / 100);
        assertEquals(expectedBalance, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should reflect an extremely high interest rate.");
    }

    /**
     * Verifies that an invalid high interest rate throws an exception.
     */
    @Test
    public void testInvalidInterestRates() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        outstanding.appendToOutstandingBalance(10000.0); // Add balance of 10,000
        System.out.println("Before applying interest");

        // Test for invalid interest rate (too high)
        OutstandingInterestError exception = assertThrows(
                OutstandingInterestError.class,
                () -> outstanding.applyInterest(1500.0), // Invalid interest rate
                "Expected an exception for invalid interest rate.");

        System.out.println("After applying interest");

        // Assert that the exception message contains the expected details
        assertTrue(exception.getMessage().contains("Interest rate must be between -100% and 1000%"));

        // Assert that the exception contains the correct interest rate
        assertEquals(1500.0, exception.getInterestRate()); // Ensure the interest rate in the exception is correct
    }

    @ParameterizedTest
    @MethodSource("provideValidInterestRate")
    public void testValidInterestRates(double initialOutstanding, double interestRate, double expectedBalance)
            throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        // Set up initial balance
        outstanding.appendToOutstandingBalance(initialOutstanding);
        // apply interest rate
        outstanding.applyInterest(interestRate);

        // Assert the updated balance matches the expected balance
        assertEquals(expectedBalance, outstanding.getCurrentOutstandingBalance(),
                "Outstanding balance should match the expected balance for a valid interest rate.");
    }

    /**
     * Produce valid test data for interest rate.
     */
    public static Stream<Arguments> provideValidInterestRate() {
        return Stream.of(
                // Valid interest rates
                Arguments.of(10000.0, 5.0, 10500.0), // 5% interest rate
                Arguments.of(10000.0, 0.0, 10000.0), // 0%
                Arguments.of(10000.0, -5.0, 9500.0) // -5%
        );
    }

    /**
     * Tests applying an invalid high interest rate.
     */
    @Test
    public void testInvalidHighInterestRate() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        outstanding.appendToOutstandingBalance(10000.0); // Add balance

        OutstandingInterestError exception = assertThrows(
                OutstandingInterestError.class,
                () -> outstanding.applyInterest(1500.0), // Invaild interest rate
                "Expected an exception for invalid high interest rate.");

        assertTrue(exception.getMessage().contains("Interest rate must be between -100% and 1000%"));
        assertEquals(1500.0, exception.getInterestRate());
    }

    /**
     * Verifies that an invalid low interest rate throws an exception.
     */
    @Test
    public void testInvalidLowInterestRate() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        outstanding.appendToOutstandingBalance(10000.0); // Add balance

        OutstandingInterestError exception = assertThrows(
                OutstandingInterestError.class,
                () -> outstanding.applyInterest(-150.0), // Invalid interest rate
                "Expected an exception for invalid low interest rate.");

        assertTrue(exception.getMessage().contains("Interest rate must be between -100% and 1000%"));
        assertEquals(-150.0, exception.getInterestRate());
    }

    /**
     * Tests applying interest to a zero balance.
     */
    @Test
    public void testApplyInterestToZeroBalance() throws InadequateOutstandingAmountErrors, OutstandingInterestError {
        outstanding.setBalance(0.0); // Set balance to zero
        OutstandingInterestError exception = assertThrows(
                OutstandingInterestError.class,
                () -> outstanding.applyInterest(5.0), // Apply interest
                "Expected an exception for applying interest to a zero balance.");

        assertTrue(exception.getMessage().contains("Cannot apply interest to a zero balance"));
        assertEquals(5.0, exception.getInterestRate());
    }
}
