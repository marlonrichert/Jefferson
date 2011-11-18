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

import java.util.Arrays;

import org.vaadin.jefferson.Presentation;

import com.vaadin.ui.ComponentContainer;

/**
 * A {@link View} with child views, rendered as a subclass of
 * {@link com.vaadin.ui.ComponentContainer}.
 * 
 * @param <T>
 *            This view's base rendering class.
 * @author Marlon Richert @ Vaadin
 */
public class Composite<T extends ComponentContainer> extends View<T> {
    private View<?>[] children;

    /**
     * @see #create(String, Class, Class)
     */
    protected Composite(String name, Class<T> base, Class<? extends T> impl) {
        this(name, base, impl, new View[] {});
    }

    /**
     * @see #create(String, Class, Class, View...)
     */
    protected Composite(String name, Class<T> base, Class<? extends T> impl,
            View<?>... children) {
        super(name, base, impl);
        this.children = children;
    }

    /**
     * Creates a new view with the given name. Children can be added through
     * {@link #setChildren(View...)}.
     * 
     * @param <B>
     *            The view's base rendering class.
     * @param name
     *            The view's name.
     * @param base
     *            The view's base rendering class.
     * @param impl
     *            The view's fall-back rendering class.
     * @see #getName()
     * @see #getBase()
     * @see #getFallback()
     */
    public static <B extends ComponentContainer> Composite<B> create(
            String name, Class<B> base, Class<? extends B> impl) {
        return new Composite<B>(name, base, impl);
    }

    /**
     * Creates a new view with the given name and children.
     * 
     * @param <B>
     *            The view's base rendering class.
     * @param name
     *            The view's name.
     * @param base
     *            The view's base rendering class.
     * @param impl
     *            The view's fall-back rendering class.
     * @param children
     *            The view's child content nodes.
     * @see #getName()
     * @see #getBase()
     * @see #getFallback()
     */
    public static <B extends ComponentContainer> Composite<B> create(
            String name, Class<B> base, Class<? extends B> impl,
            View<?>... children) {
        return new Composite<B>(name, base, impl, children);
    }

    /**
     * Sets this view's rendering component and calls
     * {@link Presentation#visit(View)} for each of its children.
     */
    @Override
    public void accept(T rendition, Presentation presentation) {
        super.accept(rendition, presentation);
        for (View<?> child : children) {
            rendition.addComponent(presentation.visit(child));
        }
    }

    /**
     * Gets this view's children.
     * 
     * @return This view's child content nodes.
     */
    public View<?>[] getChildren() {
        return Arrays.copyOf(children, children.length);
    }

    /**
     * Sets this view's children.
     * 
     * @param children
     *            This view's child content nodes.
     */
    public Composite<T> setChildren(View<?>... children) {
        this.children = children;
        return this;
    }
}
