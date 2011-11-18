/*
 * Copyright 2011 Vaadin Ltd.
 * 
 * Licensed under the GNU Affero General Public License, Version 2 (the 
 * "License"); you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/agpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.jefferson.content;

import org.vaadin.jefferson.Presentation;

import com.vaadin.ui.Component;

/**
 * A content node.
 * 
 * @param <T>
 *            This view's base rendering class.
 * @author Marlon Richert @ Vaadin
 */
public class View<T extends Component> {
    private final String name;
    private final Class<T> base;
    private final Class<? extends T> fallback;

    private T rendition;

    /**
     * @see #create(String, Class, Class)
     */
    protected View(String name, Class<T> base, Class<? extends T> fallback) {
        this.name = name;
        this.base = base;
        this.fallback = fallback;
    }

    /**
     * Creates a new view.
     * 
     * @param <B>
     *            The view's base rendering class.
     * @param name
     *            The view's name.
     * @param base
     *            The view's base rendering class.
     * @param fallback
     *            The view's fallback rendering class.
     * @see #getName()
     * @see #getBase()
     * @see #getFallback()
     */
    public static <B extends Component> View<B> create(String name,
            Class<B> base, Class<? extends B> impl) {
        return new View<B>(name, base, impl);
    }

    /**
     * Gets this view's name.
     * 
     * @return A human-readable name that identifies this view.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the class whose interface renditions of this view should implement.
     * 
     * @return The return type of {@link #getRendition()}.
     */
    public Class<T> getBase() {
        return base;
    }

    /**
     * Gets the default class used for rendering this view. Called by
     * {@link Presentation#visit(View)} if it cannot find any rules to
     * instantiate this content with.
     * 
     * @return A class that can be instantiated as a fall-back rendition.
     */
    public Class<? extends T> getFallback() {
        return fallback;
    }

    /**
     * Sets this view's rendering component.
     * <p>
     * Called by {@link Presentation#visit(View)}.
     * 
     * @param component
     *            This view's new rendering component.
     * @param presentation
     *            The presentation that called this method.
     * @see #setRendition(Component)
     */
    public void accept(T component, Presentation presentation) {
        setRendition(component);
    }

    /**
     * Sets the component that is used for rendering this view. Called by
     * {@link #accept(Component, Presentation)}
     * 
     * @param rendition
     *            The component that renders this content.
     */
    protected void setRendition(T rendition) {
        Class<? extends Component> renditionClass = rendition.getClass();
        if (!base.isAssignableFrom(renditionClass)) {
            throw new IllegalArgumentException(base
                    + " is not a superclass of " + renditionClass);
        }
        this.rendition = rendition;
    }

    /**
     * Gets the component that is used for rendering this view.
     * 
     * @return The component that renders this content.
     */
    public T getRendition() {
        return rendition;
    }
}
