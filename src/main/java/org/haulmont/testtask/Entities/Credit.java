package org.haulmont.testtask.Entities;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;



@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CREDITS")
@Data
public class Credit extends AbstractModelClass implements Comparable<Credit> {

    @NotNull
    @Column(name = "CREDIT_LIMIT")
    private Double creditLimit;

    @NotNull
    @Column(name = "CREDIT_PROCENT")
    private Double creditProcent;


    public Credit() {
    }

    public Credit(Double creditLimit, Double creditProcent) {
        this.creditLimit = creditLimit;
        this.creditProcent = creditProcent;
    }

    @Override
    public String toString() {
        return this.creditLimit + " руб.,  " + this.creditProcent + " %";
    }

    @Override
    public int compareTo(Credit o) {
        return o.getCreditLimit().compareTo(this.getCreditLimit());
    }
}