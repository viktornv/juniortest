package org.haulmont.testtask.views;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.haulmont.testtask.Entities.Bank;
import org.haulmont.testtask.Entities.Client;
import org.haulmont.testtask.Entities.Credit;

import java.util.List;

public class BankForm extends FormLayout {
    private Bank bank;
    ComboBox<Client> client = new ComboBox<>("Клиент");
    ComboBox<Credit> creditInBank = new ComboBox<>("Кредит");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Bank> binder = new BeanValidationBinder<>(Bank.class);

    public BankForm(List<Client> clients, List<Credit> credits) {

        addClassName("bank-form");
        binder.bindInstanceFields(this);

        client.setItems(clients);
        client.setItemLabelGenerator(Client::toString);

        creditInBank.setItems(credits);
        creditInBank.setItemLabelGenerator(Credit::toString);


        add(client,
                creditInBank,
                createButtonsLayout());

    }
    public void setBank(Bank bank) {
        this.bank = bank;
        binder.readBean(bank);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, bank)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(bank);
            fireEvent(new SaveEvent(this, bank));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<BankForm> {
        private Bank bank;

        protected ContactFormEvent(BankForm source, Bank bank) {
            super(source, false);
            this.bank = bank;
        }

        public Bank getBank() {
            return bank;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(BankForm source, Bank bank) {
            super(source, bank);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(BankForm source, Bank bank) {
            super(source, bank);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(BankForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

