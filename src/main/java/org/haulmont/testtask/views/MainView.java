package org.haulmont.testtask.views;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route(value="", layout = MainLayout.class)
@PWA(name = "Test", shortName = "Test", backgroundColor = "#227aef", themeColor = "#227aef")
public class MainView extends VerticalLayout {

    public MainView() {

    }
}
