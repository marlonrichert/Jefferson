package org.vaadin.jefferson.content;

import org.vaadin.jefferson.View;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;

public class ButtonView extends View<Button> {
    private ClickListener listener;

    public ButtonView(String name, ClickListener listener) {
        super(name, Button.class, new NativeButton());
        this.listener = listener;
    }

    @Override
    protected boolean setRendition(Button rendition) {
        if (!super.setRendition(rendition)) {
            return false;
        }

        rendition.addListener(listener);

        return true;
    }
}
