package org.vaadin.jefferson.content;

import org.vaadin.jefferson.View;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.NativeSelect;

public class SelectionView extends View<AbstractSelect> {

    public SelectionView(String name) {
        super(name, AbstractSelect.class);
    }

    @Override
    public AbstractSelect createFallback() {
        return new NativeSelect();
    }
}
