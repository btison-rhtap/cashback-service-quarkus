package org.acme.cashback.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "expense")
public class Expense extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    public Long saleId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    public Customer customer;

    @Column(name = "amount")
    public BigDecimal amount;

    @Column(name = "earned_cashback")
    public BigDecimal earnedCashback;

    @Column(name = "date")
    public Date date;

    @Column(name = "cashback_id")
    public Long cashback;
}
