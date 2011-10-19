package org.vaadin.jefferson;

import org.vaadin.jefferson.content.UIElement;

import com.vaadin.ui.Component;

public interface Presenter {

    void register(UIElement content, Component component);

}
