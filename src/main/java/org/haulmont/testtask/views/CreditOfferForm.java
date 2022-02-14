package org.haulmont.testtask.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.haulmont.testtask.Entities.*;
import org.haulmont.testtask.Services.BankService;
import org.haulmont.testtask.Services.CreditOfferService;
import org.haulmont.testtask.Services.PaymentScheduleService;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Route(value="creditOffer", layout = MainLayout.class)
@PageTitle("Клиенты | Работа с кредитами")

public class CreditOfferForm extends VerticalLayout {
    ComboBox<Client> client = new ComboBox<>("Клиент");
    ComboBox<Credit> creditInBank = new ComboBox<>("Кредит");
    ComboBox<Integer> creditPeriod = new ComboBox<Integer>("Период",new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });

    BankService bankService;
    PaymentScheduleService paymentScheduleService;

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    public CreditOfferForm(BankService bankService,PaymentScheduleService paymentScheduleService ) {

        this.bankService = bankService;
        this.paymentScheduleService = paymentScheduleService;

        List<Client> clients = bankService.findAllClients();
        List<Credit> credits = bankService.findAllCredits();
        addClassName("creditOffer-view");
        setSizeFull();

        client.setItems(clients);
        client.setItemLabelGenerator(Client::toString);

        creditInBank.setItems(credits);
        creditInBank.setItemLabelGenerator(Credit::toString);

        add(new H3("Оформление кредита"),
                client,
                amountAndPeriodLayout(),
                createButtonsLayout());
    }


    private HorizontalLayout amountAndPeriodLayout(){

        return new HorizontalLayout(creditInBank, creditPeriod);
    }
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);


        save.addClickListener(event -> paymentScheduleService.saveCredit(client.getValue(), creditInBank.getValue(),creditInBank.getValue().getCreditLimit(),creditPeriod.getValue()));
        close.addClickListener(e -> UI.getCurrent().navigate(BankView.class));
        return new HorizontalLayout(save, close);
    }





}
