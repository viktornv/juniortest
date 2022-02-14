package org.haulmont.testtask.Entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "BANKS")
@Data
public class Bank extends AbstractModelClass {

    public Bank() {}

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Credit creditInBank;

    public Bank(Client client, Credit creditInBank) {
        this.client = client;
        this.creditInBank = creditInBank;
    }

    @Override
    public String toString() {
       return this.client +" " + this.creditInBank;
    }

}