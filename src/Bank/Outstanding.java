package Bank;

import Bank.BankApp.InadequateBankOperatingFundsErrors;

public class Outstanding {

    private double outstandingBalance; // Tracks the current outstanding balance (Loan)

    /**
     * Constructor that initializes the outstanding balance to zero. (Loan)
     * This constructor is used when creating a new instance of the Outstanding
     * class.
     */
    public Outstanding() {
        this.outstandingBalance = 0.0; // Set initial outstanding balance to zero
    }

    /**
     * Retrieves the current outstanding balance for the account owner.
     * This method returns the amount the account owner currently owes.
     */
    public double getCurrentOutstandingBalance() {
        return outstandingBalance; // Return the outstanding balance
    }

    /**
     * This method allows for increasing the outstanding balance by the amount.
     * If the amount is invalid (zero or negative), it throws an error. (Loan)
     */
    public void appendToOutstandingBalance(double amount) throws InadequateOutstandingAmountErrors {
        if (amount <= 0) { // Check if the amount is positive
            throw new InadequateOutstandingAmountErrors(amount, "Outstanding must be positive.");
        }
        System.out.println("Outstanding amount of " + amount + " is valid.");

        outstandingBalance += amount; // Increase the outstanding balance by the amount
        System.out.println("Amount of " + amount + " added to outstanding balance.");
    }

    /**
     * This method allows the account owner to make a repayment on the outstanding
     * by reducing the outstanding balance.
     * Before subtracting, it checks if the repayment amount exceeds the current
     * outstanding balance. (Loan)
     */
    public void minusFromOutstandingBalance(double amount) throws InadequateOutstandingAmountErrors {
        checkCurrentAmountInOutstandingBalance(amount); // Ensure the repayment amount does not exceed the balance
        outstandingBalance -= amount; // Reduce the outstanding balance by the repayment amount
        // Log the updated outstanding balance
        System.out.println("New outstanding balance: " + outstandingBalance);
    }

    /**
     * This method verifies that the account owner is not trying to repay more than
     * the current outstanding balance. (Loan)
     */
    public void checkCurrentAmountInOutstandingBalance(double amount)
            throws InadequateOutstandingAmountErrors {
        if (amount > outstandingBalance) // Check if the repayment exceeds the outstanding balance
            throw new InadequateOutstandingAmountErrors(amount, "Repayment amount exceeds outstanding balance"); // Throw
                                                                                                                 // error
        // if the repayment is too large
        System.out.println("Validation passed: Repayment amount is within the outstanding balance.");
    }

    /**
     * This method checks if the bank has enough operating funds to grant the
     * outstanding requested.
     */
    public void confirmOutstandingAgainstBankOperatingFunds(double outstandingAmount, double bankOperatingFunds)
            throws InadequateBankOperatingFundsErrors {
        // Validate if the bank has adequate operating funds
        boolean doesAdequateFunds = validateAdequateFunds(outstandingAmount, bankOperatingFunds);

        if (!doesAdequateFunds) {
            // Log the error
            System.out.println("Validation failed: Requested outstanding amount " + outstandingAmount +
                    " exceeds available bank operating funds of " + bankOperatingFunds);

            // Throw exception for inadequate funds
            throw new InadequateBankOperatingFundsErrors(outstandingAmount, bankOperatingFunds);
        }
        // Log the success
        System.out.println("Validation passed: Outstanding amount " + outstandingAmount +
                " is within the available bank operating funds of " + bankOperatingFunds);
    }

    /**
     * Validate if the outstanding amount is within available bank
     * operating funds.
     *
     * @param outstandingAmount  requested outstanding amount
     * @param bankOperatingFunds available bank operating funds
     * @return true if funds are adequate, false if not
     */
    private boolean validateAdequateFunds(double outstandingAmount, double bankOperatingFunds) {
        return outstandingAmount <= bankOperatingFunds;
    }

    /**
     * Applies interest to the current outstanding balance.
     * This method applies the specified interest rate to the current outstanding
     * balance. It checks if the interest rate is within a valid range and ensures
     * that interest is not applied to a zero balance.
     *
     * @param interestRate the interest rate to apply (as a percentage).
     * @throws OutstandingInterestError if the interest rate is not within a valid
     *                                  range
     */
    public void applyInterest(double interestRate) throws OutstandingInterestError {
        // Check if the interest rate is within valid limits
        if (interestRate > 1000 || interestRate < -100) { // If the interest rate is out of bounds
            System.out.println("Invalid interest rate: " + interestRate);
            throw new OutstandingInterestError(interestRate, "Interest rate must be between -100% and 1000%"); // Throw
                                                                                                               // error
                                                                                                               // for
                                                                                                               // invalid
                                                                                                               // interest
                                                                                                               // rate
        }
        System.out.println("Interest rate " + interestRate + "% is valid.");

        // If the balance is zero, throw an exception with a custom message
        if (outstandingBalance == 0) {
            throw new OutstandingInterestError(interestRate, "Cannot apply interest to a zero balance");
        }
        // Apply the interest if everything is valid
        outstandingBalance *= (1 + interestRate / 100); // Calculate the new balance with interest
        System.out.println("Interest of " + interestRate + "% applied successfully.");

    }

    /*
     * Sets the outstanding balance to a specified value.
     * This method allows for directly setting the outstanding balance to a specific
     * value.
     */
    public void setBalance(double balance) {
        this.outstandingBalance = balance; // Set the outstanding balance to the specified value
    }

    // Exception class for handling errors related to invalid outstanding amounts
    public static class InadequateOutstandingAmountErrors extends Exception {
        /**
         * Constructor for InadequateOutstandingAmountErrors.
         * This error is thrown when a outstanding operation fails due to an invalid
         * amount.
         *
         * @param amount the outstanding amount that caused the error
         * @param info   additional information about the error
         */

        public InadequateOutstandingAmountErrors(double amount, String info) {
            // Call the error class constructor with a custom error message
            super("Invalid outstanding amount: " + amount + ". Information " + info);
            // Print the error message to the console for debugging purposes
            System.out.println(getMessage());
        }

    }

    // Error class for handling errors related to invalid interest rates
    public static class OutstandingInterestError extends Exception {

        private final double interestRate; // The invalid interest rate that caused the error

        /**
         * Constructor for OutstandingInterestError.
         * This error is thrown when an invalid interest rate is applied to an
         * outstanding balance. (Loan)
         *
         * @param interestRate the invalid interest rate
         * @param info         additional details about the error
         */
        public OutstandingInterestError(double interestRate, String info) {
            super("Invalid interest rate: " + interestRate + "%. Details: " + info);
            this.interestRate = interestRate; // Store the invalid interest rate

        }

        /**
         * Retrieves the invalid interest rate associated with this error.
         *
         * @return the invalid interest rate
         */
        public double getInterestRate() {
            return interestRate;
        }

    }
}
