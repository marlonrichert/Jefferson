package org.vaadin.jefferson.demo;

import org.vaadin.jefferson.Presentation;
import org.vaadin.jefferson.content.ButtonControl;
import org.vaadin.jefferson.content.LabelControl;
import org.vaadin.jefferson.content.SelectionControl;
import org.vaadin.jefferson.content.UIElement;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public final class DemoPresentation extends Presentation {
    public DemoPresentation() {
        define(SelectionControl.class, Table.class);

        define("root", VerticalLayout.class);
        define("nav", HorizontalLayout.class);
        define("main", VerticalSplitPanel.class);
        define("properties", HorizontalLayout.class);
        define("tabs", TabSegment.class);

        define(ButtonControl.class, "initControl");
        define(LabelControl.class, "initControl");

        define("root", "initRoot");
        define("nav", "initNav");
        define("main", "initMain");
    }

    public void initControl(UIElement content, Component component) {
        component.setCaption(content.getName());
    }

    public void initNav(UIElement content, Component component) {
        component.setWidth(100, Sizeable.UNITS_PERCENTAGE);
    }

    public void initRoot(UIElement content, Component component) {
        component.setHeight(100, Sizeable.UNITS_PERCENTAGE);
    }

    public void initMain(UIElement content, Component component) {
        component.setHeight(100, Sizeable.UNITS_PERCENTAGE);
    }
}