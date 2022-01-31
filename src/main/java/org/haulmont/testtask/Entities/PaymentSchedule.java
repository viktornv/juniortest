package org.haulmont.testtask.Entities;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table (name = "PAYMENT_SCHEDULES")
@Data
public class PaymentSchedule extends AbstractModelClass {

    @NotNull
    @Column(name = "DAY_OF_PAYMENT")
    private Date dayOfPayment;

    @NotNull
    @Column(name = "PAYMENT_AMOUNT")
    private double paymentAmount;

    @NotNull
    @Column(name = "REPAYMENT_BODY")
    private double paymentBody;

    @NotNull
    @Column(name = "REPAYMENT_PROCENT")
    private double paymentProcent;


    public PaymentSchedule(Date dayOfPayment, double paymentAmount, double paymentBody,
                           double paymentProcent) {
        this.dayOfPayment = dayOfPayment;
        this.paymentAmount = paymentAmount;
        this.paymentBody = paymentBody;
        this.paymentProcent = paymentProcent;
    }

    public PaymentSchedule() {}

    @Override
    public String toString() {
        return "Дата платежа: " + this.dayOfPayment + " Сумма платежа: " + paymentAmount
                + " Тело займа: " + paymentBody + " Проценты: " + paymentProcent;
    }
}