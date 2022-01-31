package org.haulmont.testtask.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.haulmont.testtask.Entities.CreditOffer;
import org.haulmont.testtask.Services.CreditOfferService;
import org.haulmont.testtask.Services.PaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="paymentSchedule", layout = MainLayout.class)
@PageTitle("Кредиты | Работа с кредитами")
public class PaymentScheduleView extends VerticalLayout implements HasUrlParameter<Long> {

    Grid<CreditOffer> grid = new Grid<>(CreditOffer.class);
    @Autowired
    CreditOfferService creditOfferService;

    public PaymentScheduleView(PaymentScheduleService paymentScheduleService) {
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(new H3("График платежей"), getToolbar(), grid);
        //updateList();
    }

    private void configureGrid() {
        grid.addClassNames("paymentScheduleView-grid");
        grid.setSizeFull();
        grid.setColumns("client", "credit", "creditAmount", "paymentSchedule");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        Button closeButton = new Button("Close");
        closeButton.addClickListener(e -> UI.getCurrent().navigate(BankView.class));

        HorizontalLayout toolbar = new HorizontalLayout(closeButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList( long bank_id) {
        grid.setItems(creditOfferService.findAllOffersForClient(bank_id));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long bank_id) {
        updateList(bank_id);
    }
}
