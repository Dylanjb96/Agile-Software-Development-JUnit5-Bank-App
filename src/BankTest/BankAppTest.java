package BankTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import Bank.BankAccount;
import Bank.BankAccount.AccountOwnerNotFoundErrors;
import Bank.BankAccount.InadequateFundsErrors;
import Bank.BankApp;
import Bank.BankApp.InadequateBankOperatingFundsErrors;
import Bank.BankApp.InadequateDepositAmountErrors;
import Bank.BankApp.InadequateWithdrawAmountErrors;
import Bank.BankApp.MultipleAccountErrors;
import Bank.Outstanding.InadequateOutstandingAmountErrors;

public class BankAppTest {

        private static final double MAXIMUM_OUTSTANDING = 20000.0; // Maximum outstanding outstanding limit for bank
                                                                   // (Loan)
        private static final double MAXIMUM_WITHDRAW = 6000.0; // Maximum withdrawal limit for bank
        private static final double MAXIMUM_DEPOSIT = 10000.0; // Maximum deposit limit for bank

        private static final List<String> ACCOUNT_OWNERS = List.of("Bob", "Alice"); // List
                                                                                    // of
                                                                                    // test
                                                                                    // account
                                                                                    // owners

        private static final BankApp bank = new BankApp(MAXIMUM_DEPOSIT, MAXIMUM_WITHDRAW, MAXIMUM_OUTSTANDING); // Bank
                                                                                                                 // instance
                                                                                                                 // for
                                                                                                                 // testing

        /**
         * This method is executed once before all test methods run, ensuring that the
         * bank has the required starting operating funds.
         */
        @BeforeAll
        public static void setBeforeClass() {
                double startingBankOperatingFunds = 100000.0;
                bank.appendToBankOperatingFunds(startingBankOperatingFunds); // Add the initial operating funds to the
                                                                             // bank
                System.out.println("Initial operating funds set to: " + startingBankOperatingFunds);
        }

        /**
         * This method is executed once after all tests to remove the operating funds
         * from the bank. It ensures that the reserves are properly cleaned up and no
         * leftover funds remain.
         */
        @AfterAll
        public static void tearDownClass()
                        throws InadequateBankOperatingFundsErrors {
                double startingBankOperatingFunds = 100000.0;
                bank.minusFromBankOperatingFunds(startingBankOperatingFunds); // Subtract the initial operating funds
                                                                              // from
                                                                              // the bank
                System.out.println("Bank operating funds reset after testing.");

        }

        /**
         * This method runs before each individual test to create the test accounts with
         * the starting deposit.
         */
        @BeforeEach
        public void setUp()
                        throws InadequateDepositAmountErrors,
                        MultipleAccountErrors {
                double startingDeposit = 5000.0;
                bank.appendOwnerAccount(ACCOUNT_OWNERS.get(0), startingDeposit); // Add new account with initial deposit
                bank.appendOwnerAccount(ACCOUNT_OWNERS.get(1), startingDeposit); // Add new account with initial deposit
                System.out.println("Test setup: Account Owner: " + ACCOUNT_OWNERS.get(0) + ", Initial Deposit: "
                                + startingDeposit);
                System.out.println("Test setup: Account Owner: " + ACCOUNT_OWNERS.get(1) + ", Initial Deposit: "
                                + startingDeposit);
        }

        /**
         * This method runs after each individual test to clean up by removing the test
         * accounts.
         */
        @AfterEach
        public void tearClassDown()
                        throws AccountOwnerNotFoundErrors,
                        InadequateOutstandingAmountErrors,
                        InadequateBankOperatingFundsErrors {
                bank.removeOwnerAccount(ACCOUNT_OWNERS.get(0)); // Remove the account after each test
                System.out.println("Test teardown: Account Owner " + ACCOUNT_OWNERS.get(0) + " removed.");
                bank.removeOwnerAccount(ACCOUNT_OWNERS.get(1)); // Remove the account after each test
                System.out.println("Test teardown: Account Owner " + ACCOUNT_OWNERS.get(1) + " removed.");
        }

        /**
         * This test ensures that the accounts are added properly during setup and that
         * the initial balance and reserves are correctly calculated.
         */
        @Test
        public void testAddAccountsSuccess() throws AccountOwnerNotFoundErrors {
                double startingBankOperatingFunds = 100000.0;
                double startingDeposit = 5000.0;
                // initial number of accounts in the bank
                int expectAccountCount = ACCOUNT_OWNERS.size();
                int actualAccountCount = bank.getBankAccountOwners().size();
                assertEquals(expectAccountCount, actualAccountCount,
                                "The number of accounts in the bank should match the initial number of accounts.");

                // account balances for each owner match the starting deposit
                for (String accountOwner : ACCOUNT_OWNERS) {
                        double actualBalance = bank.getOwnerAccountBalance(accountOwner);
                        assertEquals(startingDeposit, actualBalance,
                                        "The balance for account owner " + accountOwner
                                                        + " should match the starting deposit.");
                }

                // bank's operating funds are correctly updated
                double expectedOperatingFunds = startingBankOperatingFunds
                                + (ACCOUNT_OWNERS.size() * startingDeposit);
                double actualOperatingFunds = bank.getBankOperatingFunds();
                assertEquals(expectedOperatingFunds, actualOperatingFunds,
                                "The bank's operating funds should be updated correctly after adding accounts.");

                // successful completion of account setup validation
                System.out.println("Account setup validation successful.");
        }

        /**
         * This test ensures that if a deposit amount is negative or invalid,
         * the correct exception is thrown to handle such cases.
         */
        @Test
        public void testAddAccountInadequateDeposit() {
                // inadequate deposit amount
                double inadequateDeposit = -1000.0;
                String accountOwner = ACCOUNT_OWNERS.getFirst(); // Retrieve the account owner

                // Log the expected failure
                System.out.println(
                                "Testing invalid deposit of " + inadequateDeposit + " for account owner: "
                                                + accountOwner);

                // error is thrown when trying to add an account with an invalid deposit amount
                assertThrows(InadequateDepositAmountErrors.class,
                                () -> bank.appendOwnerAccount(accountOwner, inadequateDeposit),
                                "Expected an exception when attempting to add an account with an invalid deposit.");
                System.out.println("Test passed: Exception correctly thrown for invalid deposit.");
        }

        /**
         * This test ensures that the bank will reject any deposit that exceeds the
         * maximum deposit limit.
         */
        @Test
        public void testDepositLimit()
                        throws InadequateDepositAmountErrors {
                // excessive deposit amount that exceeds the max limit
                double excessiveDepositAmount = 25000.0;
                String accountOwner = ACCOUNT_OWNERS.getFirst(); // Get the account owner for the test

                // print out the test
                System.out.println("Testing deposit exceeding the maximum limit of " + excessiveDepositAmount
                                + " for account owner: " + accountOwner);

                // error is thrown when trying to deposit an amount that exceeds the maximum
                // limit
                assertThrows(InadequateDepositAmountErrors.class,
                                () -> bank.deposit(accountOwner, excessiveDepositAmount),
                                "Expected an exception when trying to deposit an amount that exceeds the maximum limit.");
        }

        /**
         * This test ensures that deposits are processed accurately and that the bank's
         * reserves and account balance reflect the change within the specified time.
         */
        @Test
        public void testDeposit() throws AccountOwnerNotFoundErrors, InadequateDepositAmountErrors {
                // Retrieve the account
                BankAccount account = bank.getOwnerAccount(ACCOUNT_OWNERS.getFirst());
                // the deposit amount
                double depositAmount = 2000.0;
                // log the pre-deposit details
                System.out.println("Performing deposit of " + depositAmount + " for account owner: "
                                + account.getAccountOwner());
                System.out.println("Initial operating funds: " + bank.getBankOperatingFunds());
                System.out.println("Initial balance for account: " + account.getCurrentBalance());

                // state before the deposit
                double outstandingBeforeDeposit = bank.getBankOperatingFunds();
                double amountBeforeDeposit = account.getCurrentBalance();
                // Perform the deposit
                bank.deposit(account.getAccountOwner(), depositAmount);
                // state after the deposit
                double outstandingAfterDeposit = bank.getBankOperatingFunds();
                double amountAfterDeposit = account.getCurrentBalance();
                // outstanding and account balance are updated correctly
                assertEquals(outstandingBeforeDeposit + depositAmount, outstandingAfterDeposit,
                                "Operating funds should increase by deposit amount.");
                assertEquals(amountBeforeDeposit + depositAmount, amountAfterDeposit,
                                "Account amount should increase by deposit amount.");

                // post-deposit state for verification
                System.out.println("Post-deposit operating funds: " + outstandingAfterDeposit);
                System.out.println("Post-deposit account amount: " + amountAfterDeposit);
        }

        /**
         * This test ensures that an exception is thrown when attempting to withdraw
         * more than the available operating funds in the bank.
         */
        @Test
        public void testWithdrawExceedsBankOperatingFunds() {
                double startingBankOperatingFunds = 100000.0;
                double startingDeposit = 5000.0;
                // withdrawal amount that exceeds available operating funds
                double excessiveAmount = startingBankOperatingFunds
                                + (ACCOUNT_OWNERS.size() + 1) * startingDeposit;
                // the amount that will be attempted for withdrawal
                System.out.println("Attempting to withdraw an amount exceeding the bank's operating funds: "
                                + excessiveAmount);

                // the expected error is thrown when attempting to withdraw more than available
                // operating funds
                assertThrows(InadequateBankOperatingFundsErrors.class,
                                () -> bank.minusFromBankOperatingFunds(excessiveAmount));
        }

        /**
         * This test checks if the system prevents granting outstanding that exceed the
         * bank's
         * available operating funds.
         */
        @Test
        @Timeout(2000) // Set a timeout of 2 seconds to ensure the outstanding operation completes
                       // within the time limit
        public void testApproveOutstandingExceedsBankReserves() throws AccountOwnerNotFoundErrors,
                        InadequateOutstandingAmountErrors,
                        InadequateDepositAmountErrors,
                        InadequateBankOperatingFundsErrors,
                        InadequateFundsErrors,
                        InadequateWithdrawAmountErrors,
                        MultipleAccountErrors {
                double startingBankOperatingFunds = 100000.0;

                // Create an account for "Harry Mason"
                String accountOwner = "Harry Mason";
                double initialDeposit = 1.0;
                bank.appendOwnerAccount(accountOwner, initialDeposit);

                // limits for the bank regarding outstanding and deposit amounts
                bank.setMaximumOutstandingLimit(startingBankOperatingFunds);
                bank.setMaxDeposit(startingBankOperatingFunds);

                try {
                        // grant an outstanding that should succeed
                        bank.grantOutstanding(accountOwner, startingBankOperatingFunds);

                        // grant a second outstanding that exceeds the bank reserves
                        assertThrows(InadequateBankOperatingFundsErrors.class, () -> {
                                bank.grantOutstanding(accountOwner, startingBankOperatingFunds);
                        });

                } finally {
                        // repay the outstanding, withdraw the minimal amount, and remove the account
                        cleanUp(accountOwner);
                }

                // Reset the bank's deposit and outstanding limit
                resetBankLimits();
        }

        // clean up after the test
        private void cleanUp(String accountOwner)
                        throws AccountOwnerNotFoundErrors, InadequateBankOperatingFundsErrors,
                        InadequateOutstandingAmountErrors, InadequateDepositAmountErrors, InadequateFundsErrors,
                        InadequateWithdrawAmountErrors {
                double startingBankOperatingFunds = 100000.0;
                bank.repayOutstanding(accountOwner, startingBankOperatingFunds);
                bank.withdraw(accountOwner, 1.0);
                bank.removeOwnerAccount(accountOwner);
        }

        // reset bank limits after test execution
        private void resetBankLimits() {
                bank.setMaxDeposit(MAXIMUM_DEPOSIT);
                bank.setMaximumOutstandingLimit(MAXIMUM_OUTSTANDING);
        }

        /**
         * This test ensures that when a repayment is made, the outstanding outstanding
         * balance
         * is updated correctly.
         */
        @Test
        public void testRepayOutstandingSuccess()
                        throws InadequateDepositAmountErrors,
                        InadequateBankOperatingFundsErrors,
                        AccountOwnerNotFoundErrors,
                        InadequateOutstandingAmountErrors {
                double startingBankOperatingFunds = 100000.0;
                double startingDeposit = 5000.0;
                // grant an outstanding balance to the account owner
                bank.grantOutstanding(ACCOUNT_OWNERS.getFirst(), 5000.0);

                // repay a portion of the outstanding balance
                double repaymentAmount = 3000.0;
                bank.repayOutstanding(ACCOUNT_OWNERS.getFirst(), repaymentAmount);

                // the outstanding balance has been updated correctly
                assertEquals(2000.0,
                                bank.getOutstandingBalance(ACCOUNT_OWNERS.getFirst()),
                                "Outstanding balance should decrease correctly after repayment.");

                // bank's operating funds have been updated correctly after the repayment
                double expectedOperatingFunds = startingBankOperatingFunds
                                + startingDeposit * ACCOUNT_OWNERS.size() - 5000.0 + repaymentAmount;
                assertEquals(expectedOperatingFunds,
                                bank.getBankOperatingFunds(),
                                "Bank operating funds should reflect the repayment.");

                // Repay the remaining balance and ensure the final account state is correct
                bank.repayOutstanding(ACCOUNT_OWNERS.getFirst(), 2000.0); // Final repayment
                assertEquals(0.0,
                                bank.getOutstandingBalance(ACCOUNT_OWNERS.getFirst()),
                                "Outstanding balance should be zero after full repayment.");
        }

        /**
         * This test checks if the system correctly raises an error when an attempt
         * is made to access an account that does not exist in the bank's records.
         */
        @Test
        public void testFindAccountOwnerNotFound() {
                // Attempt to get an account that doesn't exist, expecting an
                // AccountOwnerNotFoundErrors error
                // Define the invalid account owner's name that doesn't exist in the system
                String nonExistentAccountOwner = "Does not exist in the system";

                // assertThrows to check that an AccountOwnerNotFoundErrors exception is thrown
                assertThrows(AccountOwnerNotFoundErrors.class,
                                () -> bank.getOwnerAccount(nonExistentAccountOwner),
                                "Expected AccountOwnerNotFoundErrors to be thrown when account doesn't exist in the system.");
        }

        /**
         * This test simulates a sequence of operations: adding a new account,
         * depositing money into the account,
         * and then making a withdrawal. It checks if the bank's operating funds are
         * updated correctly after each operation.
         * Finally, it cleans up by removing the account created during the test.
         */
        @Test
        public void testOutstandingActions()
                        throws InadequateDepositAmountErrors,
                        AccountOwnerNotFoundErrors,
                        InadequateFundsErrors,
                        InadequateBankOperatingFundsErrors,
                        InadequateWithdrawAmountErrors,
                        InadequateOutstandingAmountErrors,
                        MultipleAccountErrors {
                double startingBankOperatingFunds = 100000.0;
                double startingDeposit = 5000.0;
                // new account with an initial deposit
                String accountOwner = "Harry Mason";
                double initialBalance = 10000.0;
                bank.appendOwnerAccount(accountOwner, initialBalance);

                // deposit into the account
                double depositAmount = 5000.0;
                bank.deposit(accountOwner, depositAmount);

                // withdrawal from the account
                double withdrawalAmount = 3000.0;
                bank.withdraw(accountOwner, withdrawalAmount);

                // the expected bank operating funds after these operations
                double expectedOperatingFunds = startingBankOperatingFunds
                                + (startingDeposit * ACCOUNT_OWNERS.size())
                                + initialBalance + depositAmount - withdrawalAmount;

                // verify that the bank's operating funds match the expected amount
                assertEquals(expectedOperatingFunds, bank.getBankOperatingFunds(),
                                "Bank operating funds should be updated correctly after the operations.");

                // Clean up: Remove the test account after the test
                bank.removeOwnerAccount(accountOwner);
        }

        /**
         * This test checks if the system raises an error when an attempt is made to
         * add an account with the same owner name that already exists in the bank's
         * records.
         */
        @Test
        public void testAddMultipleAccount()
                        throws AccountOwnerNotFoundErrors,
                        InadequateOutstandingAmountErrors,
                        InadequateBankOperatingFundsErrors,
                        InadequateDepositAmountErrors, MultipleAccountErrors {
                // new account with the name "Jen Doyle" and a starting deposit of 1000
                String accountOwner = "Jen Doyle";
                double initialDeposit = 3900.0;
                bank.appendOwnerAccount(accountOwner, initialDeposit);

                // add the same account again, expecting an error
                assertThrows(MultipleAccountErrors.class, () -> {
                        // trying to add the same account again should throw an error
                        bank.appendOwnerAccount(accountOwner, initialDeposit);
                }, "Expected MultipleAccountErrors when attempting to add a duplicate account.");

                // clean up by removing the test account
                bank.removeOwnerAccount(accountOwner);
        }

        /**
         * This test ensures that the system enforces the bank's withdrawal limits by
         * raising an exception if the withdrawal amount exceeds limit.
         */
        @Test
        public void testInadequateWithdrawReachLimit() {
                // new maximum withdrawal limit for testing
                final double testMaximumWithdrawLimit = 2300.0;
                final double withdrawAmountLimit = testMaximumWithdrawLimit + 10.0;

                // new maximum withdrawal limit for the test
                bank.setMaximumWithdrawLimit(testMaximumWithdrawLimit);

                // check that an InadequateWithdrawalAmountErrors error is thrown
                assertAll("Test multiple account owners",
                                () -> assertThrows(InadequateWithdrawAmountErrors.class,
                                                () -> bank.withdraw(ACCOUNT_OWNERS.get(0), withdrawAmountLimit),
                                                "Expected error when withdrawal exceeds the max limit for account Bob"),
                                () -> assertThrows(InadequateWithdrawAmountErrors.class,
                                                () -> bank.withdraw(ACCOUNT_OWNERS.get(1), withdrawAmountLimit),
                                                "Expected error when withdrawal exceeds the max limit for account Alice"));

                // reset the maximum withdrawal limit back to the original value
                bank.setMaximumWithdrawLimit(MAXIMUM_WITHDRAW);
        }

        @Test
        public void testGetDepositLimit() {
                // the expected maximum deposit limit for the test
                final double expectedMaxDeposit = MAXIMUM_DEPOSIT;
                // the actual maximum deposit limit from the bank
                double actualMaxDeposit = bank.getMaximunDepositLimit();

                // Verify if the expected value matches the actual value
                assertEquals(expectedMaxDeposit, actualMaxDeposit,
                                "The maximum deposit amount returned by the system should be the same as the anticipated maximum.");
        }

        /**
         * Verifies that the system correctly returns the maximum withdrawal limit.
         * Ensures the limit matches the value set during the creation of the BankApp
         * instance.
         */
        @Test
        public void testGetMaximumWithdrawal() {
                // expected maximum withdrawal limit for the test
                final double expectedMaxWithdrawal = MAXIMUM_WITHDRAW;
                // maximum withdrawal limit before performing the assertion
                bank.setMaximumWithdrawLimit(expectedMaxWithdrawal);
                // actual maximum withdrawal limit from the bank
                double actualMaxWithdrawal = bank.getMaximumWithdrawLimit();

                // Verify if the expected value matches the actual value
                assertEquals(expectedMaxWithdrawal, actualMaxWithdrawal,
                                "The maximum withdrawal amount returned by the system should be the same as the anticipated maximum.");
        }

        /**
         * Verifies that the system correctly returns the maximum outstanding limit.
         * Ensures the limit matches the value set during the creation of the
         * BankApp instance.
         */
        @Test
        public void testGetMaximumOutstanding() {
                // expected maximum outstanding amount for the test
                final double expectMaxOutstanding = MAXIMUM_OUTSTANDING;
                // actual maximum outstanding amount from the bank system
                double legitMaxOutstanding = bank.getMaximumOutstandingLimit();

                // Verify that the actual value matches the expected value
                assertEquals(expectMaxOutstanding, legitMaxOutstanding,
                                "The maximum outstanding amount returned by the system should be the same as the anticipated maximum.");
        }

        /**
         * This test ensures that if the account does not have enough balance to cover
         * the withdrawal, an error is thrown.
         */
        @Test
        public void testWithdrawInadequateFunds() {
                // withdrawal amount, which exceeds the account balance
                double excessiveAmount = MAXIMUM_WITHDRAW;
                // expect an InadequateFundsErrors exception to be thrown
                assertAll("Check withdrawal for multiple account owners",
                                () -> assertThrows(InadequateFundsErrors.class,
                                                () -> bank.withdraw(ACCOUNT_OWNERS.get(0), excessiveAmount),
                                                "Expected InadequateFundsErrors when attempting to withdraw more than the account balance for account Bob."),
                                () -> assertThrows(InadequateFundsErrors.class,
                                                () -> bank.withdraw(ACCOUNT_OWNERS.get(1), excessiveAmount),
                                                "Expected InadequateFundsErrors when attempting to withdraw more than the account balance for account Alice."));
        }

}
