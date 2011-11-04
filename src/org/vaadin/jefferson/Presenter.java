package org.vaadin.jefferson;

import org.vaadin.jefferson.content.UIElement;


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
     */
    void register(UIElement<?> content);
}
