package org.haulmont.testtask.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.haulmont.testtask.Entities.Client;
import org.haulmont.testtask.Services.ClientService;

@Route(value="clients", layout = MainLayout.class)
@PageTitle("Клиенты | Работа с кредитами")
public class ClientsView extends VerticalLayout {
    Grid<Client> grid = new Grid<>(Client.class);
    TextField filterText = new TextField();
    ClientForm form;
    ClientService clientService;

    public ClientsView(ClientService clientService) {
        this.clientService = clientService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(new H3("Клиенты"), getToolbar(), getContent());
        updateList();
        closeEditor();
        
    }

    private void closeEditor() {
        form.setClient(null);
        form.setVisible(false);
        removeClassName("editing");
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
        form = new ClientForm();
        form.setWidth("25em");

        form.addListener(ClientForm.SaveEvent.class, this::saveClient);
        form.addListener(ClientForm.DeleteEvent.class, this::deleteClient);
        form.addListener(ClientForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveClient(ClientForm.SaveEvent event) {
        clientService.saveClient(event.getClient());
        updateList();
        closeEditor();
    }

    private  void deleteClient(ClientForm.DeleteEvent event) {
        clientService.deleteContact(event.getClient());
        updateList();
        closeEditor();
    }



    private void configureGrid() {
        grid.addClassNames("client-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "middleName", "passport");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editClient(evt.getValue()));
    }

    private void editClient(Client client) {
        if (client == null) {
            closeEditor();
        }else {
            form.setClient(client);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Фильтр...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Добавить клиента");
        addContactButton.addClickListener(click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editClient(new Client());
    }

    private void updateList() {
        grid.setItems(clientService.findAllClients(filterText.getValue()));
    }
}
