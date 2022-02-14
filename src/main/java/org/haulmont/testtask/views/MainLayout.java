package org.haulmont.testtask.views;



import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;



public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("БАНК");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Клиенты", ClientsView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink
        ));

        addToDrawer(new VerticalLayout(
                listLink,
                new RouterLink("Банк - действующие кредиты", BankView.class)
        ));

        addToDrawer(new VerticalLayout(
                listLink,
                new RouterLink("Настройка кредитов", CreditView.class)
        ));

        addToDrawer(new VerticalLayout(
                listLink,
                new RouterLink("Оформление кредита", CreditOfferForm.class)
        ));
    }
}
