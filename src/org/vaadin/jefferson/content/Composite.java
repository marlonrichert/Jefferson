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
 * A composite {@link View}, typically rendered as a subclass of
 * {@link com.vaadin.ui.ComponentContainer}.
 * 
 * @author Marlon Richert @ Vaadin
 */
public class Composite<T extends ComponentContainer> extends View<T> {

    private View<?>[] children;

    /**
     * Creates a new view with the given name. Children can be added through
     * {@link #setChildren(View...)}.
     * 
     * @param name
     *            This view's name.
     */
    protected Composite(String name, Class<T> base, Class<? extends T> impl) {
        this(name, base, impl, new View[] {});
    }

    /**
     * Creates a new view with the given name and children.
     * 
     * @param name
     *            This view's name.
     * @param children
     *            This view's child content nodes.
     */
    protected Composite(String name, Class<T> base, Class<? extends T> impl,
            View<?>... children) {
        super(name, base, impl);
        this.children = children;
    }

    public static <S extends ComponentContainer> Composite<S> create(
            String name, Class<S> base, Class<? extends S> impl) {
        return new Composite<S>(name, base, impl);
    }

    public static <S extends ComponentContainer> Composite<S> create(
            String name, Class<S> base, Class<? extends S> impl,
            View<?>... children) {
        return new Composite<S>(name, base, impl, children);
    }

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
