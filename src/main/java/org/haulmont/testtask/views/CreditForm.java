package org.haulmont.testtask.views;


import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.haulmont.testtask.Entities.Client;
import org.haulmont.testtask.Entities.Credit;

public class CreditForm extends FormLayout {
    NumberField creditLimit = new NumberField("creditLimit");
    NumberField creditProcent = new NumberField("creditProcent");
    
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Credit> binder = new BeanValidationBinder<>(Credit.class);
    private Credit credit;

    public CreditForm() {
        addClassName("credit-form");

        binder.bindInstanceFields(this);
        add(
                creditLimit,
                creditProcent,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, credit)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(credit);
            fireEvent(new SaveEvent(this, credit));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
        binder.readBean(credit);
    }


    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<CreditForm> {
        private Credit credit;

        protected ContactFormEvent(CreditForm source, Credit credit) {
            super(source, false);
            this.credit = credit;
        }

        public Credit getContact() {
            return credit;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(CreditForm source, Credit credit) {
            super(source, credit);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(CreditForm source, Credit contact) {
            super(source, contact);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(CreditForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
