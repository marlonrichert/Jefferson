package org.vaadin.jefferson.content;

import org.vaadin.jefferson.Presentation;
import org.vaadin.jefferson.View;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;

public class ButtonView extends View<Button> {
    private ClickListener listener;

    public ButtonView(String name, ClickListener listener) {
        super(name, Button.class);
        this.listener = listener;
    }

    @Override
    protected Button accept(Presentation presentation) {
        Button rendition = super.accept(presentation);
        rendition.addListener(listener);
        return rendition;
    }

    @Override
    protected boolean setRendition(Button rendition) {
        Button oldRendition = getRendition();
        if (oldRendition != null) {
            oldRendition.removeListener(listener);
        }
        return super.setRendition(rendition);
    }

    @Override
    public Button createFallback() {
        return new NativeButton();
    }
}
