package org.vaadin.jefferson;

import org.vaadin.jefferson.content.UIElement;

import com.vaadin.ui.Component;

/**
 * Callback interface for receiving registrations of content rendered by a
 * {@link Presentation}.
 * 
 * @author Marlon Richert
 */
public interface Presenter {

    /**
     * Registers the rendering of the given content as the give component.
     * 
     * @param content
     *            The content that was rendered.
     * @param component
     *            The component as which the given content was rendered.
     */
    void register(UIElement<?> content, Component component);
}
