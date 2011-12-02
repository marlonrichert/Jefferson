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
package org.vaadin.jefferson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * A {@link View} with child views, rendered as a subclass of
 * {@link com.vaadin.ui.ComponentContainer}.
 * 
 * @param <T>
 *            This view's base rendering class.
 * @author Marlon Richert @ Vaadin
 */
public class Composite<T extends ComponentContainer> extends View<T> {
    private List<View<?>> children = new ArrayList<View<?>>();

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
        setChildren(children);
    }

    public static Composite<ComponentContainer> create(String name) {
        return new Composite<ComponentContainer>(name,
                ComponentContainer.class, CssLayout.class);
    }

    public static Composite<ComponentContainer> create(String name,
            View<?>... children) {
        return new Composite<ComponentContainer>(name,
                ComponentContainer.class, CssLayout.class, children);
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
     * {@link Presentation#render(View)} for each of its children.
     */
    @Override
    protected void update(T rendition, Presentation presentation) {
        super.update(rendition, presentation);
        for (View<?> child : children) {
            rendition.replaceComponent(child.getRendition(),
                    presentation.render(child));
        }
    }

    /**
     * Gets this view's children.
     * 
     * @return This view's child content nodes.
     */
    public View<?>[] getChildren() {
        return children.toArray(new View<?>[children.size()]);
    }

    /**
     * Sets this view's children.
     * 
     * @param children
     *            This view's child content nodes.
     */
    public Composite<T> setChildren(View<?>... children) {
        this.children = Arrays.asList(children);
        return this;
    }

    public boolean replaceChild(View<?> existing, View<?> replacement) {
        int i = children.indexOf(existing);
        if (i == -1) {
            return false;
        }
        children.set(i, replacement);
        getRendition().replaceComponent(existing.getRendition(),
                replacement.getRendition());
        return true;
    }
}
