package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class GridComponent extends Grid<Integer> {

    public GridComponent() {
        setItems(0, 1);
        addComponentColumn(i -> createButton(i));
    }

    private Button createButton(Integer i) {
        return new Button("Test Button " + i, e -> {
            RouteChangingDialog dialog = new RouteChangingDialog();
            dialog.open();
        });
    }

}
