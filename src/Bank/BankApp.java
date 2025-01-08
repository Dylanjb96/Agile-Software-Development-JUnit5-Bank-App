package Bank;

import java.util.ArrayList;
import java.util.List;

import Bank.BankAccount.AccountOwnerNotFoundErrors;
import Bank.BankAccount.InadequateFundsErrors;
import Bank.Outstanding.InadequateOutstandingAmountErrors;

/**
 * Represents a bank with functionality to manage accounts, deposits,
 * withdrawals, outstanding, and bank operating funds.
 * Provides checks and constraints to ensure valid operations within defined
 * limits.
 */
public class BankApp {

    private double maximumWithdrawLimit; // Maximum withdrawal amount
    private double maximumDepositLimit; // Maximum deposit amount
    private double maximumOutstandingLimit; // Maximum oustanding amount (Loan)
    private double bankOperatingFund = 0.0; // Represents the total operating funds available in the bank. The value is
                                            // initialized to 0.0 and increases or decreases as deposits, withdrawals,
                                            // or outstanding are processed.

    // A list to store all the bank accounts.
    // Each account represents a owner's banking information, including balance and
    // outstanding. (Loan)
    private List<BankAccount> Bankaccounts = new ArrayList<>();

    /**
     * Constructs a Bank instance with specified operational limits.
     * These limits define the contraints for withdrawals, deposits and outstandings
     * in the bank.
     */
    public BankApp(double maximumWithdrawLimit, double maximumDepositLimit, double maximumOutstandingLimit) {
        this.maximumWithdrawLimit = maximumWithdrawLimit; // Initialize the maximum withdraw limit for the bank.
        this.maximumDepositLimit = maximumDepositLimit; // Initialize the maximum deposit limit for the bank.
        this.maximumOutstandingLimit = maximumOutstandingLimit; // Initialize the maximum outstanding limit for the
                                                                // bank. (Loan)
    }

    /**
     * Retrieves the list of accounts in the bank.
     * Each account in the list contains information such as the account owner's
     * name and current balance.
     */
    public List<BankAccount> getBankAccountOwners() {
        return Bankaccounts; // A list of bank accounts.
    }

    /**
     * Retrieves the maximum deposit limit allowed by the bank.
     * This limit ensures that the deposits do not exceed the bank's constraints.
     */
    public double getMaximunDepositLimit() {
        return maximumDepositLimit; // Deposits do not exceed the bank's constraints.
    }

    /**
     * Retrieves the maximum withdrawal limit set by the bank.
     * This limit ensures that the account owners can not exceed the bank's
     * constraints.
     */
    public double getMaximumWithdrawLimit() {
        return maximumWithdrawLimit; // Withdrawals do not exceed the bank's constraints.
    }

    /**
     * Retrieves the maximum oustanding limit set by the bank.
     */
    public double getMaximumOutstandingLimit() {
        return maximumOutstandingLimit; // Maximum allowable Oustanding limit. (Loan)
    }

    /**
     * Retrieves the current operating funds available in the bank.
     * This bank operating funds are used to support operations such as approving
     * outstanding.
     */
    public double getBankOperatingFunds() {
        return bankOperatingFund; // The current operating funds of the bank.
    }

    /**
     * This method allows the bank to set a limit on how much money a customer can
     * deposit.
     * The value passed here will override any previous limit set.
     * It ensures that the deposit operations do not exceed the threshold.
     */
    public void setMaxDeposit(double maximumDepositLimit) {
        this.maximumDepositLimit = maximumDepositLimit; // The new maximum deposit limit.
    }

    /**
     * The bank sets constraints to prevent unusually large withdrawals that may
     * affect liquidity.
     */
    public void setMaximumWithdrawLimit(double maximumWithdrawLimit) {
        this.maximumWithdrawLimit = maximumWithdrawLimit; // The new maximum withdraw limit.
    }

    /**
     * Sets the maximum oustanding limit in the bank.
     * This method determines the highest posssible amount of oustanding that can be
     * granted to a customer.
     * This prevent bank from lending excessively.
     */
    public void setMaximumOutstandingLimit(double maximumOutstandingLimit) {
        this.maximumOutstandingLimit = maximumOutstandingLimit; // The new maximum outstanding limit. (Loan)
    }

    /**
     * Before accepting a deposit, this method checks whether the amount is within
     * the allowed limits.
     * If the deposit exceeds the maximum allowable deposit or is non-positive, an
     * error is thrown.
     */
    public void checkAdequateDeposit(double amount) throws InadequateDepositAmountErrors {
        // Verifies if the deposit amount is valid (positive and within limits)
        // Check if the deposit amount is positive
        boolean mustPositive = amount > 0;
        // Check if the deposit amount exceeds the maximum deposit limit
        boolean reachLimit = amount > maximumDepositLimit;
        // If the deposit is not positive, throw an error with a message
        if (!mustPositive) {
            throw new InadequateDepositAmountErrors(amount, "Amount must be positive.");
        }
        // If the deposit exceeds the maximum limit, throw an error with a different
        // message
        if (reachLimit) {
            throw new InadequateDepositAmountErrors(amount, "Deposit amount exceeds the limit.");
        }
        // Otherwise, deposit is valid and no errors are thrown.
        System.out.println("Deposit of " + amount + " is valid.");
    }

    /**
     * This method checks that the requested withdrawal amount is valid, ensuring
     * that the amount is positive and does not exceed the maximum withdrawal limt.
     */
    public void checkAdequateWithdraw(double amount)
            throws InadequateWithdrawAmountErrors {
        boolean hasPositive = amount > 0;
        // Check if the deposit amount exceeds the maximum withdrawal limit
        boolean theLimit = amount > maximumWithdrawLimit;
        // If the withdraw is not positive, throw an error with a message
        if (!hasPositive) {
            throw new InadequateWithdrawAmountErrors(amount, "Amount must be positive.");
        }
        // If the withdraw exceeds the maximum limit, throw an error with a different
        // message
        if (theLimit) {
            throw new InadequateWithdrawAmountErrors(amount, "Withdrawal amount exceeds the limit.");
        }
        // Withdraw is valid and no errors are thrown.
        System.out.println("Withdraw of " + amount + " is valid.");
    }

    /**
     * Before granting outstanding (Loan), this method ensures that the requested
     * outstanding amount is within the allowable limits.
     * If the amount is negative or exceeds the maximum outstanding limit, it throws
     * an error to prevent invalid outstanding amount from being granted.
     */
    public void checkAdequateOutstanding(double amount)
            throws InadequateOutstandingAmountErrors {
        boolean shouldPositive = amount > 0;
        // Check if the outstanding amount exceeds the maximum outstanding limit
        boolean exceedsLimit = amount > maximumOutstandingLimit;
        // If the outstanding is not positive, throw an error with a message
        if (!shouldPositive) {
            throw new InadequateOutstandingAmountErrors(amount, "Amount must be positive.");
        }
        // If the outstanding exceeds the maximum limit, throw an error with a different
        // message
        if (exceedsLimit) {
            throw new InadequateOutstandingAmountErrors(amount, "Outstanding amount exceeds the limit.");
        }
        // Outstanding is valid and no errors are thrown.
        System.out.println("Outstanding of " + amount + " is valid.");
    }

    /**
     * This method decreases the bank operating funds by the given amount.
     * Before performing the operation, it verifies that the bank operating funds
     * are available.
     */
    public void minusFromBankOperatingFunds(double amount)
            throws InadequateBankOperatingFundsErrors {
        checkAdequateBankOperatingFunds(amount); // Verify that the bank operating funds are available
        bankOperatingFund -= amount; // Reduces the bank operating funds by the amount
    }

    /**
     * This method increases the bank operating funds by the given amount.
     * It is used when the bank recieves the additional amount, such as deposits,
     * that increase the amount of available bank operating funds.
     */
    public void appendToBankOperatingFunds(double amount) {
        bankOperatingFund += amount; // Increases the current bank operating funds by the amount.
    }

    /**
     * This method checks whether the current bank operating fund in the bank are
     * enough to cover the required operation. If it inadequate, an error is thrown
     * to prevent the operation from being performed.
     */
    public void checkAdequateBankOperatingFunds(double amount) throws InadequateBankOperatingFundsErrors {
        // Conditional checking, throwing an error if funds are inadequate.
        boolean hasAdequateFunds = amount <= bankOperatingFund;
        // If inadequate funds, throw the error.
        if (!hasAdequateFunds) {
            throw new InadequateBankOperatingFundsErrors(amount, bankOperatingFund);
        }
    }

    /**
     * This method allows for deposit into a particular account.
     * It validates the deposit amount and adds the deposit to both the account and
     * bank operating funds.
     */
    public void deposit(String accountOwner, double amount)
            throws InadequateDepositAmountErrors,
            AccountOwnerNotFoundErrors {
        // Log the deposit request
        System.out.println("Attempting to deposit " + amount + " into account: " + accountOwner);

        checkAdequateDeposit(amount); // Check if the deposit is valid
        System.out.println("Deposit amount is valid.");
        getOwnerAccount(accountOwner)
                .deposit(amount); // Add the deposit to the account
        System.out.println("Successfully deposited " + amount + " into " + accountOwner + "'s account.");
        appendToBankOperatingFunds(amount); // Add the deposit to the bank operating funds
        System.out.println("Deposited " + amount + " into the bank operating funds.");
    }

    /**
     * This method allows for withdraw from a particular account.
     * It checks if the account has enough funds and if the bank has enough
     * operating funds before proceding with the withdrawal.
     */
    public void withdraw(String accountOwner, double amount)
            throws InadequateFundsErrors,
            InadequateBankOperatingFundsErrors,
            AccountOwnerNotFoundErrors,
            InadequateWithdrawAmountErrors {
        // Log the withdrawal request
        System.out.println("Attempting to withdraw " + amount + " from account: " + accountOwner);

        checkAdequateWithdraw(amount); // Validate the withdrawal amount
        System.out.println("Withdrawal amount is valid.");
        checkAdequateBankOperatingFunds(amount); // Ensure the bank has enough operating funds
        System.out.println("Bank has enough operating funds for the withdrawal.");
        // Log the withdrawal from the account
        getOwnerAccount(accountOwner)
                .withdraw(amount); // Decrease the amount from the account balance
        System.out.println("Successfully withdrew " + amount + " from " + accountOwner + "'s account.");
        minusFromBankOperatingFunds(amount); // Decrease the amount from the bank operating funds
        System.out.println("Deducted " + amount + " from the bank operating funds.");
    }

    /**
     * Grant outstanding for an account. (Loan)
     * This method provides a outstanding to a particular account owner. it check if
     * the requested outstanding amount is valid, whether the account exists, and if
     * the bank has enough operating unds to grant the outstanding. If all
     * conditions are
     * met, the outstanding is added to the account's balance, and the bank
     * operating funds are reduced.
     */
    // Use Outstanding class to validate outstanding against reserves
    public void grantOutstanding(String accountOwner, double outstandingAmount)
            throws InadequateBankOperatingFundsErrors, // if there's not enough Operating Funds
            AccountOwnerNotFoundErrors, // if the account owner is not found
            InadequateOutstandingAmountErrors { // if there's not enough Outstanding amount
        // Log the request for granting an outstanding amount
        System.out.println(
                "Attempting to grant outstanding amount of " + outstandingAmount + " to account: " + accountOwner);

        checkAdequateOutstanding(outstandingAmount); // Validate the outstanding amount
        System.out.println("Outstanding amount is valid.");
        getOwnerAccount(accountOwner)
                .getOutstanding()
                .confirmOutstandingAgainstBankOperatingFunds(outstandingAmount, bankOperatingFund);
        System.out.println("Confirmed that the bank has enough funds to cover the outstanding amount.");

        checkAdequateBankOperatingFunds(outstandingAmount); // Ensure the bank has enough funds for the outstanding
        System.out.println("Bank has adequate operating funds to grant the outstanding.");

        getOwnerAccount(accountOwner)
                .appendToOutstandingBalance(outstandingAmount); // Add the outstanding amount to the account's
                                                                // outstanding balance
        System.out.println("Added outstanding amount of " + outstandingAmount + " to the account: " + accountOwner);

        minusFromBankOperatingFunds(outstandingAmount); // Deduct the outstanding amount from the bank's operating funds
        System.out.println("Deducted outstanding amount of " + outstandingAmount + " from the bank's operating funds.");
        // Log the successful granting of outstanding amount
        System.out.println(
                "Outstanding amount of " + outstandingAmount + " successfully granted to account: " + accountOwner);
    }

    /**
     * This method returns the current outstanding balance of the account, based on
     * the account owner's name. If the account is not found, it throws an error.
     * (Loan)
     */
    public double getOutstandingBalance(String accountOwner)
            throws AccountOwnerNotFoundErrors {
        return getOwnerAccount(accountOwner).getCurrentOutstandingBalance(); // Return the outstanding balance of the
                                                                             // account
    }

    /**
     * This method allows the account owner to repay part or all of their
     * outstanding balance. (Loan)
     * It checks if the repayment amount is valid, and if the account exists.
     * The repayment amount is then deducted from the outstanding balance and added
     * to the bank operating funds.
     */
    public void repayOutstanding(String accountOwner, double repaymentAmount)
            throws AccountOwnerNotFoundErrors,
            InadequateOutstandingAmountErrors,
            InadequateDepositAmountErrors {
        // Fetch the account owner and deduct repayment from the outstanding balance
        BankAccount account = getOwnerAccount(accountOwner); // Fetch account
        System.out.println("Fetched account for owner: " + accountOwner);

        account.getOutstanding().minusFromOutstandingBalance(repaymentAmount); // Deduct from loan balance
        System.out.println("Repayment of " + repaymentAmount + " deducted from outstanding balance.");

        // Add repayment amount to the bank's operating funds
        appendToBankOperatingFunds(repaymentAmount); // Increase bank reserves
        System.out.println("Added repayment of " + repaymentAmount + " to the bank's operating funds.");

        // log any additional logic after repayment
        System.out.println("Repayment of " + repaymentAmount + " successfully processed for account: " + accountOwner);
    }

    /**
     * This method searches for an account based on the account owner's name. If
     * there's no account with the name, it throws an error to indicate that the
     * account does not exist.
     */
    public BankAccount getOwnerAccount(String accountOwner)
            throws AccountOwnerNotFoundErrors {
        // Iterate over the list of accounts
        for (BankAccount account : Bankaccounts) {
            // Check if the account owner's name matches
            if (account.getAccountOwner().equals(accountOwner)) {
                return account; // Return the matching account
            }
        }
        // If no matching account is found, throw an exception
        throw new AccountOwnerNotFoundErrors(accountOwner);
    }

    /**
     * This method checks whether the account starting deposit is valid and if the
     * account owner already exists in the system.
     * If the account does not exist in the system, a new account is created and
     * added to the list of bank accounts, and the starting deposit is added to the
     * bank operating funds system.
     */
    public void appendOwnerAccount(String accountOwner, double startingDeposit)
            throws InadequateDepositAmountErrors, MultipleAccountErrors {
        // Validate the deposit amount using a helper method
        checkAdequateDeposit(startingDeposit);

        for (BankAccount account : Bankaccounts) {
            if (account.getAccountOwner().equals(accountOwner)) {
                // If an account with the same owner exists, throw an exception
                throw new MultipleAccountErrors(accountOwner);
            }
        }

        // Create a new BankAccount object and add it to the accounts list
        BankAccount newAccount = new BankAccount(accountOwner, startingDeposit);
        Bankaccounts.add(newAccount);

        // Update the bank's operating funds to include the starting deposit
        appendToBankOperatingFunds(startingDeposit);
    }

    /**
     * This method ensures that the account has no outstanding and that the bank
     * operating fund
     * are adequate to handle the closure. If the conditions are met, the account is
     * removed,
     * and the balance is decreased from the bank operating funds.
     */
    public void removeOwnerAccount(String accountOwner)
            throws AccountOwnerNotFoundErrors,
            InadequateOutstandingAmountErrors,
            InadequateBankOperatingFundsErrors {
        BankAccount account = getOwnerAccount(accountOwner); // Retrieve the account by owner name
        double outstandingBalance = account.getCurrentOutstandingBalance(); /// Get the outstanding balance
        if (outstandingBalance > 0)
            throw new InadequateOutstandingAmountErrors(outstandingBalance,
                    "Outstanding balance must be 0 to close account"); // Throw error if there's a outstanding balance
        Bankaccounts.removeIf(a -> a.getAccountOwner().equals(accountOwner)); // Remove the account if it exists
        minusFromBankOperatingFunds(account.getCurrentBalance()); // Decrease the account balance from the bank
                                                                  // operating funds
    }

    /**
     * This method fetches the current balance of the account based on the account
     * owner's name.
     * If the account is not found, it throws an error.
     */
    public double getOwnerAccountBalance(String accountOwner) throws AccountOwnerNotFoundErrors {
        // Attempt to find the account by iterating over the list of bank accounts.
        BankAccount account = Bankaccounts.stream()
                .filter(acc -> acc.getAccountOwner().equals(accountOwner)) // Filter accounts by the owner's name
                .findFirst() // Find the first matching account
                .orElseThrow(() -> new AccountOwnerNotFoundErrors(accountOwner)); // If not found, throw error

        // Once the account is found, return its current balance.
        return account.getCurrentBalance();
    }

    // Custom error class for handling cases where multiple accounts exist for the
    // same account owner
    public static class MultipleAccountErrors extends Exception {
        /**
         * Constructor for MultipleAccountErrors.
         * This constructor creates an error with a message indicating that an
         * account already exists for the specified account owner.
         *
         * @param accountOwner the name of the account owner that already has an
         *                     existing account
         */
        public MultipleAccountErrors(String accountOwner) {
            // Call the parent Exception class constructor with a custom error message
            super("This account already exist: " + accountOwner);
            // Print the exception message to the console for debugging purposes
            System.out.println(getMessage());
        }

    }

    // Define the class for handling errors related to inadequate bank operating
    // funds
    public static class InadequateBankOperatingFundsErrors extends Exception {
        /**
         * Constructor for InadequateBankOperatingFundsErrors.
         * This constructor creates an error when the requested amount exceeds the
         * available operating funds in the bank.
         *
         * @param requestedAmount         the amount requested
         * @param availableOperatingFunds the available operating funds in the bank
         */
        public InadequateBankOperatingFundsErrors(double requestedAmount, double availableOperatingFunds) {
            // Call the parent error class constructor with a custom error message
            super("Inadequate operating funds. Requested: " + requestedAmount + ", Available: "
                    + availableOperatingFunds);
            // Print the error message to the console for debugging
            System.out.println(getMessage());
        }
    }

    // Error class for handling errors related to invalid deposit amounts
    public static class InadequateDepositAmountErrors extends Exception {
        /**
         * Constructor for InadequateDepositAmountErrors.
         * This error is thrown when a deposit-related operation fails due to an invalid
         * amount.
         *
         * @param amount the deposit amount that caused the error
         * @param info   additional information about the error
         */
        public InadequateDepositAmountErrors(double amount, String info) {
            // Call the parent error class constructor with a custom error message
            super("Invalid deposit amount: " + amount + ". Information: " + info);
            // Print the error message to the console for debugging purposes
            System.out.println(getMessage());
        }
    }

    // Error class for handling errors related to invalid withdrawal amounts
    public static class InadequateWithdrawAmountErrors extends Exception {
        /**
         * Constructor for InadequateWithdrawalAmountErrors.
         * This error is thrown when a withdrawal-related operation fails due to an
         * invalid amount.
         *
         * @param amount the withdrawal amount that caused the error
         * @param info   additional information about the error
         */
        public InadequateWithdrawAmountErrors(double amount, String info) {
            // Call error class constructor with a custom error message
            super("Invalid withdrawal amount: " + amount + ". Information: " + info);
            // Print the error message to the console for debugging purposes
            System.out.println(getMessage());
        }
    }
}
