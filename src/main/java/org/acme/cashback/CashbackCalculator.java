package org.acme.cashback;

import org.acme.cashback.enums.CustomerStatus;
import org.acme.cashback.model.Cashback;
import org.acme.cashback.model.CashbackDTO;
import org.acme.cashback.model.Expense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.math.RoundingMode;

@ApplicationScoped
public class CashbackCalculator {

    private static final Logger log = LoggerFactory.getLogger(CashbackCalculator.class);

    public CashbackDTO calculate(Expense expense, Cashback cashback, String operation) {
        BigDecimal newEarnedCashbackForSingleExpense;
        BigDecimal earnedCashbackForExpense;
        CashbackDTO cashbackDTO = buildCashbackDTO(expense, cashback);

        if("c".equalsIgnoreCase(operation)){
            earnedCashbackForExpense = calculateAmountCashback(cashbackDTO);

            // updates single expense incoming cashback value
            cashbackDTO.setExpenseEarnedAmount(earnedCashbackForExpense);

            // updates cashback wallet
            cashbackDTO.addToCashbackAmount(earnedCashbackForExpense);

        } else if("u".equalsIgnoreCase(operation)){
            BigDecimal originalSingleExpenseCashback = expense.earnedCashback;
            newEarnedCashbackForSingleExpense = calculateAmountCashback(cashbackDTO);

            // updates single expense cashback value
            cashbackDTO.setExpenseEarnedAmount(newEarnedCashbackForSingleExpense);

            // updates wallet values
            cashbackDTO.subtractFromCashbackAmount(originalSingleExpenseCashback); //subtracts outdated value
            cashbackDTO.addToCashbackAmount(newEarnedCashbackForSingleExpense);
        }

        log.debug("Adding to cashback " + cashbackDTO.getExpenseEarnedAmount());
        return cashbackDTO;
    }

    /**
     * Returns x percent of input expenseAmount.
     * Output format is rounded as:  11.12345 returned as 11.12, 11.12556, returned as 11.13
     */
    private BigDecimal calculateAmountCashback(CashbackDTO cashbackDTO) {
        BigDecimal percentage = CustomerStatus.get(cashbackDTO.getCustomerStatus().toLowerCase()).getCashbackPercentage();
        return percentage.multiply(cashbackDTO.getExpenseAmount()).setScale(2, RoundingMode.HALF_EVEN);
    }

    private CashbackDTO buildCashbackDTO(Expense expense, Cashback cashback) {

        return new CashbackDTO.Builder()
                        .expenseAmount(expense.amount)
                        .cashbackAmount(cashback.amount)
                        .customerStatus(expense.customer.status)
                        .build();
    }
}

