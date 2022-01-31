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

    PaymentScheduleService scheduleService;

    CreditOfferService creditOfferService;

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    public CreditOfferForm(BankService bankService,PaymentScheduleService scheduleService, CreditOfferService creditOfferService ) {

        this.bankService = bankService;
        this.scheduleService = scheduleService;
        this.creditOfferService = creditOfferService;
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


        save.addClickListener(event -> saveCredit(client.getValue(), creditInBank.getValue(),creditInBank.getValue().getCreditLimit(),creditPeriod.getValue()));
        close.addClickListener(e -> UI.getCurrent().navigate(BankView.class));
        return new HorizontalLayout(save, close);
    }



    private void saveCredit(Client client, Credit credit, double creditAmount, int creditPeriod ) {

        try {
            Bank bank = new Bank(client, credit);
            bankService.save(bank);

            LocalDateTime localDateTime = LocalDateTime.now();
            Date date = Date.valueOf(localDateTime.toLocalDate());
            double scale = Math.pow(10, 2);

            double ostatok = creditAmount * 0.8;
            double procent = credit.getCreditProcent();
            int period = creditPeriod * 12;
            double paymentBody = Math.ceil(ostatok / period * scale) / scale;

            for (int i = 0; i < creditPeriod * 12; i++) {
                double paymentProcent = Math.ceil(((ostatok * (procent / 100)) / period) * scale) / scale;
                double paymentPerMonth = Math.ceil((paymentBody + paymentProcent) * scale) / scale;
                if(paymentProcent<0) paymentProcent=0;
                PaymentSchedule schedule = new PaymentSchedule(date, paymentPerMonth, paymentBody, paymentProcent);
                ostatok -= paymentPerMonth;
                localDateTime = localDateTime.plusMonths(1);
                date = Date.valueOf(localDateTime.toLocalDate());

                System.out.println("------------  "+schedule);


                scheduleService.save(schedule);
                CreditOffer creditOffer = new CreditOffer(client, credit, creditAmount, schedule, bank.getId());
                System.out.println("============  "+creditOffer);
                creditOfferService.save(creditOffer);
            }
            Notification.show("Операция завершена успешно!");


        } catch (Exception e) {
            Notification.show("Не удалось завершить операцию, попробуйте снова!");

            e.printStackTrace();
        }
    }

}
