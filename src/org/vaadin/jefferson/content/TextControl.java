package org.vaadin.jefferson.content;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public class TextControl extends Control {

    public TextControl(String name) {
        super(name);
    }

    @Override
    public Class<? extends Component> getDefaultRenditionClass() {
        return TextField.class;
    }
}
