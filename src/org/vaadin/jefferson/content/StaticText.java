package org.vaadin.jefferson.content;

import org.vaadin.jefferson.View;

import com.vaadin.ui.Label;

public class StaticText extends View<Label> {

    public StaticText(String name) {
        super(name, Label.class);
    }

    @Override
    public Label createFallback() {
        return new Label(getName());
    }
}
