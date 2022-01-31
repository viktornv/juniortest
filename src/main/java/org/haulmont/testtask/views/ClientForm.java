package org.haulmont.testtask.views;


import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.haulmont.testtask.Entities.Client;


public class ClientForm extends FormLayout {

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField middlename = new TextField("Middle name");
    TextField passport = new TextField("Passport");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    private Client client;

    Binder<Client> binder = new BeanValidationBinder<>(Client.class);

    public ClientForm() {
        addClassName("client-form"); // CSS class name

        binder.bindInstanceFields(this);

        add(firstName,
                lastName,
                middlename,
                passport,
                createButtonsLayout());
    }

    public void setClient(Client client) {
        this.client = client;
        binder.readBean(client);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, client)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(client);
            fireEvent(new SaveEvent(this, client));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class ContactFormEvent extends ComponentEvent<ClientForm> {
        private Client client;

        protected ContactFormEvent(ClientForm source, Client contact) {
            super(source, false);
            this.client = contact;
        }

        public Client getClient() {
            return client;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ClientForm source, Client client) {
            super(source, client);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ClientForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

