package com.ttsr.springshop.frontend;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("main")
public class MainView extends VerticalLayout {
    public MainView(){
        var mainTitle = new Text("Spring Product Shop");
        add(mainTitle);
    }
}
