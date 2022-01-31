package org.haulmont.testtask.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.haulmont.testtask.Entities.Bank;
import org.haulmont.testtask.Repositories.CreditOfferRepository;
import org.haulmont.testtask.Services.BankService;

import java.util.Collections;

@Route(value="bank", layout = MainLayout.class)

@PageTitle("Клиенты | Работа с кредитами")
public class BankView extends VerticalLayout {
    Grid<Bank> grid = new Grid<>(Bank.class);
    TextField filterText = new TextField();
    BankForm form;
    BankService bankService;
    CreditOfferRepository creditOfferRepository;

    public BankView(BankService bankService,CreditOfferRepository creditOfferRepository) {
        this.bankService = bankService;
        this.creditOfferRepository = creditOfferRepository;
        addClassName("bank-view");
        setSizeFull();
        configureGrid();
        configureForm();
        
        add(new H3("Действующие кредиты"), getToolbar(), getContent());
        updateList();

        closeEditor();
    }


    private void updateList() {
        grid.setItems(bankService.findAll(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new BankForm(bankService.findAllClients(), bankService.findAllCredits());
        form.setWidth("25em");

        form.addListener(BankForm.SaveEvent.class, this::saveBank);
        form.addListener(BankForm.DeleteEvent.class, this::deleteBank);
        form.addListener(BankForm.CloseEvent.class, e -> closeEditor());
    }
    private void saveBank(BankForm.SaveEvent event) {
        bankService.save(event.getBank());
        updateList();
        closeEditor();
    }

    private void deleteBank(BankForm.DeleteEvent event) {
        creditOfferRepository.deleteAllOffersForClient(event.getBank().getId());
        bankService.delete(event.getBank());


        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCreditButton = new Button("Добавить кредит");
        Button PaymentScheduledetails = new Button("График платежей");

        Button button = new Button("Open confirm dialog");

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCreditButton, PaymentScheduledetails);
        toolbar.addClassName("toolbar");

        filterText.addValueChangeListener(e -> updateList());
        //addCreditButton.addClickListener(click -> addBank());
        addCreditButton.addClickListener(e -> UI.getCurrent().navigate(CreditOfferForm.class));
        PaymentScheduledetails.addClickListener(e -> UI.getCurrent().navigate(PaymentScheduleView.class,  grid.asSingleSelect().getValue().getId()));
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("client", "creditInBank");
        //grid.addColumn(bank -> bank.getCreditInBank().toString());
        //grid.addColumn(credit -> credit.toString());
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editBank(event.getValue()));
    }

    private void closeEditor() {
        form.setBank(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    public void editBank(Bank bank) {
        if (bank == null) {
            closeEditor();
        } else {
            form.setBank(bank);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addBank() {
        grid.asSingleSelect().clear();
        editBank(new Bank());
    }

}
