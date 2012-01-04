package org.vaadin.jefferson.content;

import org.vaadin.jefferson.Presentation;
import org.vaadin.jefferson.View;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;

public class ButtonView extends View<Button> {
    private ClickListener listener;

    public ButtonView(String name, ClickListener listener) {
        super(name, Button.class, NativeButton.class);
        this.listener = listener;
    }

    @Override
    public void accept(Presentation presentation, Button component) {
        super.accept(presentation, component);
        component.addListener(listener);
    }
}
