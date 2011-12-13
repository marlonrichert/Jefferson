package org.vaadin.jefferson.content;

import org.vaadin.jefferson.Composite;
import org.vaadin.jefferson.View;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public class SimpleComposite extends Composite<ComponentContainer> {

    public SimpleComposite(String name) {
        super(name, ComponentContainer.class, CssLayout.class);
    }

    public SimpleComposite(String name, View<?>... children) {
        super(name, ComponentContainer.class, CssLayout.class, children);
    }
}
