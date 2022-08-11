package org.acme.cashback;

import io.quarkus.panache.common.Parameters;
import org.acme.cashback.model.Cashback;
import org.acme.cashback.model.CashbackDTO;
import org.acme.cashback.model.Expense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class CashbackService {

    private static final Logger log = LoggerFactory.getLogger(CashbackService.class);

    @Inject
    CashbackCalculator cashbackCalculator;

    @Transactional
    public void processEventMessage(Long saleId, String operation) {
        // find expense and cashback (cashback may be null)
        Expense expense = Expense.findById(saleId);
        if (expense == null) {
            log.warn("Expense with id " + saleId + " not found in the cashback database");
            return;
        }
        Cashback cashback = Cashback.find("#Cashback.getByCustomer", Parameters.with("customerId", expense.customer.customerId))
                .firstResult();
        log.debug("Found expense with id " + saleId);
        log.debug("Customer id " + expense.customer.customerId);
        if (cashback != null) {
            log.debug("Cashback id " + cashback.cashbackId);
        }
        if ("c".equalsIgnoreCase(operation) && cashback == null) {
            log.debug("No cashback wallet exists. Creating new cashback for customer " + expense.customer.customerId);
            cashback = new Cashback.Builder(expense.customer.customerId).build();
            cashback.persist();
            log.debug("created cashback with id " + cashback.cashbackId);
        }
        expense.cashback = cashback.cashbackId;
        CashbackDTO cashbackDTO = cashbackCalculator.calculate(expense, cashback, operation);
        expense.earnedCashback = cashbackDTO.getExpenseEarnedAmount();
        cashback.amount = cashbackDTO.getCashbackAmount();
    }

}
