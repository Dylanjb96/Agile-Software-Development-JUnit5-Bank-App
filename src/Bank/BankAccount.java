package Bank;

import Bank.Outstanding.InadequateOutstandingAmountErrors;

/**
 * Bank account for a customer, with deposit,
 * withdraw, and outstanding (loan) balances features.
 */
public class BankAccount {

    private String accountOwner; // The name of the account owner
    private double currentBalance; // The current balance in the account
    private Outstanding outstanding; // Outstanding information for the account (Loan)

    /**
     * account owner's name with balance.
     * 
     * @param accountOwner   Account owner's name
     * @param currentBalance Balance in the owner's account
     */
    public BankAccount(String accountOwner, double currentBalance) {
        this.accountOwner = accountOwner;
        this.currentBalance = currentBalance;
        this.outstanding = new Outstanding(); // Initialize the outstanding balance information (Loan)
    }

    /**
     * Recieve the account owner's name.
     *
     */
    public String getAccountOwner() {
        return accountOwner;
    }

    /**
     * Recieve the account owner's balance
     */
    public double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Verifies if the owner of the account balance can be withdrawn
     * 
     * If the requested withdrawal amount is greater than the current balance, an
     * error is returned.
     */
    public void checkCurrentAmountInAccount(double amount)
            throws InadequateFundsErrors {
        if (currentBalance < amount)
            // error if the withdrawal amount is greater than the current balance.
            throw new InadequateFundsErrors(amount, currentBalance);
    }

    /**
     * Add funds to the account balance from the current account balance
     * 
     * This method increase the account owner's current balance by the amount they
     * put in.
     */
    public void deposit(double amount) {
        // Append the amount to the current balance.
        currentBalance += amount;
    }

    /**
     * Reduces the account balance from the current account balance.
     * 
     * Before requesting the withdrawal, it ensures the account owner has enough
     * funds by calling the 'checkCurrentAmountInAcount' method.
     * If the withdrawal amount is greater than the current balance, an error is
     * activated.
     */
    public void withdraw(double amount)
            throws InadequateFundsErrors {
        checkCurrentAmountInAccount(amount); // Verify that the account owner has enough funds.
        currentBalance -= amount; // Reduce the funds from the current balance.
    }

    /**
     * Return the total Outstanding balance. (Loan)
     * 
     * It call the 'Outstanding' class to retrieve the current outstanding balance
     * in connection to the account.
     */
    public double getCurrentOutstandingBalance() {
        return outstanding.getCurrentOutstandingBalance(); // Retrieve the current outstanding balance from the
                                                           // Outstanding class.
    }

    /**
     * Validates that repayment can be applied to the account.
     * 
     * It checks whether the amount can be reduced from the outstanding balance.
     * It hands over the validation to the 'Outstanding' class.
     * 
     * @throws InadequateOutstandingAmountErrors if the repayment is greater than
     *                                           the outstanding balance. (Loan)
     */
    public void checkCurrentAmountInOutstandingBalance(double amount)
            throws InadequateOutstandingAmountErrors {
        outstanding.checkCurrentAmountInOutstandingBalance(amount); // Hand over to the Outstanding class
    }

    /**
     * Increase the amount to the outstanding balance. (Loan)
     * 
     * This adds the amount to the outstanding balance. It then validates the
     * operation to ensure that the addition is possible and hand over the operation
     * to the 'Outstanding' class.
     * 
     * @param amount must be positive and if so add to the outstanding balance.
     * @throws InadequateOutstandingAmountErrors if the operation is not possible.
     */
    public void appendToOutstandingBalance(double amount) throws InadequateOutstandingAmountErrors {
        outstanding.appendToOutstandingBalance(amount); // Hand over to the Outstanding class
    }

    /**
     * Decrease the amount to the outstanding balance. (Loan)
     * 
     * This reduces the amount to the outstanding balance. It then validates the
     * operation to ensure that the deduction is possible and hand over the
     * operation to the 'Outstanding' class.
     * 
     * @param amount must be positive and less than or equal to the current
     *               outstanding balance, and if so, deducte from the outstanding
     *               balance.
     */
    public void minusFromOutstandingBalance(double amount)
            throws InadequateOutstandingAmountErrors {
        outstanding.minusFromOutstandingBalance(amount); // Hand over to the Outstanding class
    }

    /**
     * Access to the Outstanding class.
     * 
     * This allows other classes to access and interact with the 'Outstanding' class
     * directly.
     * 
     * @return the Outstanding instance associated with the account.
     */
    public Outstanding getOutstanding() {
        return outstanding; // Return the Outstanding class link to this account.
    }

    // Custom error class for when an account owner is not found in the system
    public static class AccountOwnerNotFoundErrors extends Exception {
        /**
         * Constructor for AccountOwnerNotFoundErrors.
         * This constructor creates an error with a message indicating that an
         * account with the specified account owner could not be found.
         *
         * @param accountOwner the name of the account owner that was not found
         */
        public AccountOwnerNotFoundErrors(String accountOwner) {
            // Call the error class constructor with a custom message
            super("No account found: " + accountOwner);
            // Print the error message to the console
            System.out.println(getMessage());
        }
    }

    public static class InadequateFundsErrors extends Exception {
        /**
         * Constructor for InadequateFundsErrors.
         * This constructor creates an error when a withdrawal is attempted with
         * inadequate funds in the account.
         *
         * @param withdrawAmount   amount the user wishes to withdraw
         * @param availableBalance current balance available in the account
         */
        public InadequateFundsErrors(double withdrawAmount, double availableBalance) {
            // Call the error class constructor with a custom error message
            super("Inadequate funds for withdrawal. Requested: " + withdrawAmount
                    + ", Available: " + availableBalance);
            // Print the error message to the console for debugging
            System.out.println(getMessage());
        }
    }

}
