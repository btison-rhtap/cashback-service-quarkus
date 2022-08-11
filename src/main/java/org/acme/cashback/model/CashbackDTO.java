package org.acme.cashback.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CashbackDTO {

    private BigDecimal expenseAmount;

    private BigDecimal cashbackAmount;

    private BigDecimal expenseEarnedAmount;

    private String customerStatus;

    private CashbackDTO(){}

    public void setExpenseEarnedAmount(BigDecimal incomingExpenseEarnedAmount) {
        expenseEarnedAmount = incomingExpenseEarnedAmount;
    }

    public void subtractFromCashbackAmount(BigDecimal incomingExpenseEarnedAmount) {
        cashbackAmount = cashbackAmount.subtract(incomingExpenseEarnedAmount);
    }

    public void addToCashbackAmount(BigDecimal incomingExpenseEarnedAmount) {
        if(cashbackAmount == null){
            cashbackAmount = new BigDecimal(BigInteger.ZERO);
        }
        cashbackAmount = cashbackAmount.add(incomingExpenseEarnedAmount);
    }

    @Override
    public String toString() {
        return "CashbackDTO{" +
                ", expenseAmount=" + expenseAmount +
                ", cashbackAmount=" + cashbackAmount +
                ", expenseEarnedAmount=" + expenseEarnedAmount +
                ", customerStatus='" + customerStatus + '\'' +
                '}';
    }

    /**
     * Getters
     */
    public BigDecimal getExpenseEarnedAmount() {
        return expenseEarnedAmount;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public String getCustomerStatus() {return customerStatus;}

    public BigDecimal getCashbackAmount() {return (cashbackAmount == null) ? BigDecimal.ZERO : cashbackAmount;}

    /**
     * Builder
     */
    public static class Builder {

        private final CashbackDTO cashbackDTO;

        public Builder () {
            cashbackDTO = new CashbackDTO();
        }

        public Builder expenseAmount(BigDecimal expenseAmount) {
            cashbackDTO.expenseAmount = expenseAmount;
            return this;
        }

        public Builder cashbackAmount(BigDecimal cashbackAmount) {
            cashbackDTO.cashbackAmount = cashbackAmount;
            return this;
        }

        public Builder customerStatus(String customerStatus) {
            cashbackDTO.customerStatus = customerStatus;
            return this;
        }

        public CashbackDTO build() {
            return cashbackDTO;
        }
    }
}
