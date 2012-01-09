package org.vaadin.jefferson.content;

import org.vaadin.jefferson.View;

import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.TextField;

public class TextView extends View<AbstractTextField> {

    public TextView(String name) {
        super(name, AbstractTextField.class);
    }

    @Override
    public AbstractTextField createFallback() {
        return new TextField();
    }
}
