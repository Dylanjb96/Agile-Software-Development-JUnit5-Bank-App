package BankTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Bank.BankAccount;
import Bank.BankAccount.InadequateFundsErrors;
import Bank.Outstanding.InadequateOutstandingAmountErrors;

public class AccountTest {

        private BankAccount account;

        /**
         * This method will run before each individual test to initialize a BankAccount
         * instance with the predefined starting balance and account holder name.
         */
        @BeforeEach
        public void settingUp() {
                String SAMPLE_ACCOUNT_OWNER = "Sample";
                double STARTING_BALANCE = 11000.0;
                // Initialize test data for the BankAccount instance
                String accountName = SAMPLE_ACCOUNT_OWNER; // Predefined account owner's name
                double startingBalance = STARTING_BALANCE; // Predefined starting balance
                // Create a new BankAccount instance with the test data
                account = new BankAccount(accountName, startingBalance);

                // Log setup details
                System.out.println("Test setup initialized. Account Owner: " + accountName +
                                ", Starting Balance: " + startingBalance);
        }

        /**
         * This test ensures that the account owner name is being correctly stored and
         * retrieved from the account.
         */
        @Test
        public void testGetAccountOwner() {
                String SAMPLE_ACCOUNT_OWNER = "Sample";
                // Define the expected account owner
                String expectOwner = SAMPLE_ACCOUNT_OWNER;
                // Retrieve the account owner's name from the account instance
                String actualOwner = account.getAccountOwner();

                // Verify that the retrieved account owner's name matches the expected name
                assertEquals(expectOwner, actualOwner,
                                "The account owner's name should match the expected owner.");
                System.out.println("Account owner validation successful. Owner: " + actualOwner);
        }

        /**
         * This test ensures that when an account is created, the starting balance
         * matches the expected value.
         */
        @Test
        public void testGetAccountOwnerBalance() {
                double STARTING_BALANCE = 11000.0;
                // Define the expected starting balance
                double expectBalance = STARTING_BALANCE;
                // Retrieve the current balance from the account
                double actualBalance = account.getCurrentBalance();

                // Verify that the retrieved balance matches the expected balance
                assertEquals(expectBalance, actualBalance,
                                "The starting account balance should match the expected balance.");
                System.out.println("Account balance validation successful. Balance: " + actualBalance);
        }

        /**
         * This test checks if withdrawing funds from the account works correctly and
         * that the balance is updated accordingly.
         */
        @Test
        public void testWithdraw() throws InadequateFundsErrors {
                double STARTING_BALANCE = 11000.0;
                // Define the withdrawal amount
                double withdrawalAmount = 3000.0;
                // Define the expected balance after withdrawal
                double expectBalance = STARTING_BALANCE - withdrawalAmount;
                // Log the initial balance before withdrawal for clarity
                System.out.println("[TEST] Initial balance: " + account.getCurrentBalance());

                // Perform the withdrawal operation
                account.withdraw(withdrawalAmount);
                // Check if the balance was updated correctly after the withdrawal
                double actualBalance = account.getCurrentBalance();
                // verify the updated balance
                assertEquals(expectBalance, actualBalance,
                                "After withdrawing " + withdrawalAmount
                                                + ", the balance should be reduced by the withdrawal amount.");

                // new balance after the withdrawal
                System.out.println("[TEST] New balance after withdrawal: " + actualBalance);
        }

        /**
         * This test ensures that an exception is thrown when attempting to withdraw
         * more funds than the current balance allows.
         */
        @Test
        public void testWithdrawInadequateFunds() {
                double STARTING_BALANCE = 11000.0;
                double withdrawAmount = STARTING_BALANCE + 2000.0; // Amount greater than the current balance
                InadequateFundsErrors exception = assertThrows(InadequateFundsErrors.class, // Expect an error to be
                                                                                            // thrown
                                () -> account.withdraw(withdrawAmount),
                                "Expected to throw InadeqauteFundsException when withdrawing more than balance.");

                assertTrue(exception.getMessage().contains("Inadequate funds"),
                                "The error message should indicate inadequate funds.");
        }

        /**
         * This test checks if the deposit functionality works as expected and that the
         * balance is correctly updated after a deposit.
         */
        @Test
        public void testDeposit() {
                double STARTING_BALANCE = 11000.0;
                // Define the deposit amount and expected balance after the deposit
                double depositAmount = 6000.0;
                double expectedBalance = STARTING_BALANCE + depositAmount;

                // Perform the deposit operation
                account.deposit(depositAmount);

                // Verify that the balance has been updated correctly
                assertEquals(expectedBalance, account.getCurrentBalance(),
                                "The account balance should increase by the deposited amount.");
                System.out.println("Deposit successful. New balance: " + account.getCurrentBalance());
        }

        /**
         * This test ensures that when an account is created, the initial outstanding
         * balance is set to zero.
         */
        @Test
        public void testStartingOutstandingBalance() {
                // Fetch the current outstanding balance
                double actualStartingOutstandingBalance = account.getCurrentOutstandingBalance();
                // Log the actual outstanding balance
                System.out.println("[TEST] Initial outstanding balance: " + actualStartingOutstandingBalance);

                // Validate that the outstanding balance is zero
                assertEquals(0.0, actualStartingOutstandingBalance, "Initial outstanding balance empty.");
                // Oconfirmation message for the test output
                System.out.println("[TEST] Test passed: Initial outstanding balance is correctly set to zero.");
        }

        /**
         * This test checks if the method to increase the outstanding balance works
         * correctly
         * and adds the specified amount to the outstanding balance.
         * 
         * @throws InadequateOutstandingAmountErrors if the outstanding amount is
         *                                           invalid
         */
        @Test
        public void testAppendToOutstandingBalance() throws InadequateOutstandingAmountErrors {
                // Amount to add to the outstanding
                double outstandingAmount = 4000.0;
                // outstanding amount before adding to it
                System.out.println("[TEST] Adding " + outstandingAmount + " to the outstanding balance.");
                // modifies the outstanding balance
                account.appendToOutstandingBalance(outstandingAmount);

                // Capture the actual outstanding balance after modification
                double actualOutstandingBalance = account.getCurrentOutstandingBalance();
                // Log the actual outstanding balance
                System.out.println("[TEST] Updated outstanding balance: " + actualOutstandingBalance);
                // Verify that the outstanding balance is correctly updated
                assertEquals(outstandingAmount, actualOutstandingBalance,
                                "Outstanding balance should increase after adding to it.");

                // confirm the test passed
                System.out.println("[TEST] Test passed: Outstanding balance updated correctly.");
        }

        /**
         * This test ensures that the method to reduce the outstanding balance works
         * correctly and the balance decreases after repayment.
         * 
         * @throws InadequateOutstandingAmountErrors if the repayment amount is invalid
         */
        @Test
        public void testMinusFromOutstandingBalance() throws InadequateOutstandingAmountErrors {
                double outstandingAmount = 5000.0; // Amount of outstanding
                double payoffAmount = 3000.0; // Repayment amount
                // initial outstanding balance and repayment amount
                System.out.println("[TEST] Initial outstanding balance: " + outstandingAmount);
                System.out.println("[TEST] Repayment amount: " + payoffAmount);

                account.appendToOutstandingBalance(outstandingAmount); // Add the initial outstanding amount
                System.out.println(
                                "[TEST] Outstanding balance after adding: " + account.getCurrentOutstandingBalance());
                account.minusFromOutstandingBalance(payoffAmount); // Repay part of the outstanding
                System.out.println("[TEST] Outstanding balance after repayment: "
                                + account.getCurrentOutstandingBalance());

                assertEquals(outstandingAmount - payoffAmount, account.getCurrentOutstandingBalance(), // Check if
                                                                                                       // the
                                                                                                       // outstanding
                                                                                                       // balance
                                                                                                       // decreased
                                                                                                       // correctly
                                "Outstanding balance should decrease after repayment.");
                // success if the test passes
                System.out.println("[TEST] Test passed: Outstanding balance decreased correctly.");
        }

        /**
         * This test ensures that an exception is thrown when the repayment amount
         * reach over the outstanding balance.
         * 
         * @throws InadequateOutstandingAmountErrors if the repayment amount exceeds the
         *                                           outstanding balance
         */
        @Test
        public void testMinusFromOutstandingBalanceLimit() throws InadequateOutstandingAmountErrors {
                double outstandingAmount = 6000.0; // Initial oustanding balance
                double exceedingRepayment = 7000.0; // amount exceeds the outstanding balance
                // initial outstanding balance and repayment amount
                System.out.println("[TEST] Initial outstanding balance: " + outstandingAmount);
                System.out.println("[TEST] Excessive repayment amount: " + exceedingRepayment);

                account.appendToOutstandingBalance(outstandingAmount);
                System.out.println(
                                "[TEST] Outstanding balance after adding: " + account.getCurrentOutstandingBalance());

                InadequateOutstandingAmountErrors exception = assertThrows( // Expect an exception when repayment
                                                                            // exceeds
                                                                            // balance
                                InadequateOutstandingAmountErrors.class,
                                () -> account.minusFromOutstandingBalance(exceedingRepayment),
                                "Expected to throw InvalidOutstandingAmountException when repayment exceeds outstanding balance.");

                assertTrue(exception.getMessage().contains("Repayment amount exceeds outstanding balance")); // Validate
                                                                                                             // the
                                                                                                             // exception
                // message
                // Log the exception message for successful error handling
                System.out.println("[TEST] Exception caught: " + exception.getMessage());
        }

        /**
         * This test checks if the appropriate error is thrown when trying to access
         * more funds than available in the account.
         */
        @Test
        public void testCheckInadequateFunds() {
                double STARTING_BALANCE = 11000.0;
                double checkAmount = STARTING_BALANCE + 1000.0; // Amount greater than the current balance
                System.out.println(
                                "[TEST] Checking amount: " + checkAmount + " which exceeds the balance: "
                                                + STARTING_BALANCE);
                InadequateFundsErrors exception = assertThrows( // Expect an exception to be thrown
                                InadequateFundsErrors.class,
                                () -> account.checkCurrentAmountInAccount(checkAmount),
                                "Expected to throw InadequateFundsException for insufficient funds.");

                assertTrue(exception.getMessage().contains("Inadequate funds"),
                                "The error message should show 'Inadequate funds' ");
        }

        /**
         * This test ensures that no exception is thrown when the requested amount is
         * less than or equal to the current balance.
         */
        @Test
        public void testCheckAdequateFunds() {
                double STARTING_BALANCE = 11000.0;
                double validCheckAmount = STARTING_BALANCE / 2;
                System.out.println("[TEST] Checking amount: " + validCheckAmount
                                + " which is less than or equal to the balance: " + STARTING_BALANCE);

                // Error when the amount is less than or equal to the balance
                assertDoesNotThrow(() -> account.checkCurrentAmountInAccount(validCheckAmount),
                                "No error should be thrown for sufficient funds.");

                // Success of the test
                System.out.println("[TEST] No exception was thrown as expected for valid withdrawal amount.");
        }

        /**
         * This test ensures that an error is thrown when the repayment exceeds the
         * outstanding balance.
         * 
         * @throws InadequateOutstandingAmountErrors if the repayment amount exceeds the
         *                                           outstanding balance
         */
        @Test
        public void testCheckInvalidAmountInOutstandingBalance() throws InadequateOutstandingAmountErrors {
                double outstandingAmount = 5000.0; // Initial outstanding balance
                double exceedRepayment = 6000.0; // Repayment amount exceeds the outstanding balance
                System.out.println("[TEST] Adding an outstanding amount of " + outstandingAmount +
                                " and trying to repay " + exceedRepayment
                                + " which exceeds the outstanding balance.");

                account.appendToOutstandingBalance(outstandingAmount); // Add the outstanding amount

                assertThrows( // Expect an error to be thrown when the repayment exceeds balance
                                InadequateOutstandingAmountErrors.class,
                                () -> account.checkCurrentAmountInOutstandingBalance(exceedRepayment),
                                "Expected to throw InvalidOutstandingAmountException for excessive repayment.");
        }

        /**
         * This test ensures that no error is thrown when the repayment amount is
         * valid and does not exceed the outstanding balance.
         * 
         * @throws InadequateOutstandingAmountErrors if the repayment amount is invalid
         */
        @Test
        public void testCheckValidAmountInOutstandingBalance() throws InadequateOutstandingAmountErrors {
                double outstandingAmount = 5000.0; // Initial outstanding balance
                double correctRepay = 3000.0; // Valid repayment amount
                System.out.println("[TEST] Adding an outstanding amount of " + outstandingAmount +
                                " and attempting to repay " + correctRepay + ", which is a valid repayment.");

                account.appendToOutstandingBalance(outstandingAmount); // Add the outstanding amount

                assertDoesNotThrow(() -> account.checkCurrentAmountInOutstandingBalance(correctRepay),
                                "No exception should be thrown for valid repayment amount.");
                System.out.println("[TEST] Repayment of " + correctRepay + " is valid and no exception was thrown.");
        }

}
