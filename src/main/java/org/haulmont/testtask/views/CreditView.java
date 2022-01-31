package org.haulmont.testtask.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.haulmont.testtask.Entities.Client;
import org.haulmont.testtask.Entities.Credit;
import org.haulmont.testtask.Services.ClientService;
import org.haulmont.testtask.Services.CreditService;

import javax.swing.text.html.ListView;

@Route(value="credits", layout = MainLayout.class)

@PageTitle("Кредиты | Работа с кредитами")
public class CreditView extends VerticalLayout {
    Grid <Credit> grid = new Grid<>(Credit.class);
    CreditForm form;
    CreditService creditService;

    public CreditView(CreditService creditService){
        this.creditService = creditService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolBar(),getContent());

        updateList();

        closeEditor();
    }

    private void closeEditor() {
        form.setCredit(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems((creditService.findAll()));
    }

    private void configureForm() {
        form = new CreditForm();
        form.setWidth("25em");

        form.addListener(CreditForm.SaveEvent.class, this::saveContact);
        form.addListener(CreditForm.DeleteEvent.class, this::deleteContact);
        form.addListener(CreditForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveContact(CreditForm.SaveEvent event) {
        creditService.save(event.getContact());
        updateList();
        closeEditor();
    }

    private  void deleteContact(CreditForm.DeleteEvent event) {
        creditService.delete(event.getContact());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid,form);
        content.setFlexGrow(2,grid);
        content.setFlexGrow(1,form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolBar() {

        Button addCreditButton = new Button("Добавить кредит");
        addCreditButton.addClickListener(click -> addCredit());

        HorizontalLayout toolbar = new HorizontalLayout(addCreditButton);
        return toolbar;
    }

    public void editCredit(Credit credit) {
        if (credit == null) {
            closeEditor();
        } else {
            form.setCredit(credit);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addCredit() {
        grid.asSingleSelect().clear();
        editCredit(new Credit());
    }

    private void configureGrid() {
        grid.addClassNames("credit-grid");
        grid.setSizeFull();
        grid.setColumns("creditLimit", "creditProcent");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCredit(event.getValue()));
    }

}
