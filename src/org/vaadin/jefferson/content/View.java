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
 * @author Marlon Richert @ Vaadin
 */
public class View<T extends Component> {
    private final String name;
    private final Class<T> base;
    private final Class<? extends T> fallback;

    private T rendition;

    /**
     * Creates a new content node with the given name. It is preferred to use
     * only lower-case letters and hyphens (-), but this is not enforced.
     * 
     * @param name
     *            A preferably (but not enforcedly) unique name.
     */
    protected View(String name, Class<T> base, Class<? extends T> impl) {
        this.name = name;
        this.base = base;
        this.fallback = impl;
    }

    public static <S extends Component> View<S> create(String name,
            Class<S> base, Class<? extends S> impl) {
        return new View<S>(name, base, impl);
    }

    public void accept(T component, Presentation presentation) {
        setRendition(component);
    }

    /**
     * Gets the class whose interface renditions of this content node should
     * implement.
     * 
     * @return The return type of {@link #getRendition()}.
     */
    public Class<T> getBase() {
        return base;
    }

    /**
     * Gets the default class used for rendering this content node. Called by
     * {@link Presentation#visit(View)} if it cannot find any rules to
     * instantiate this content with.
     * 
     * @return A class that can be instantiated as a fall-back rendition.
     */
    public Class<? extends T> getFallback() {
        return fallback;
    }

    /**
     * Gets this content node's name.
     * 
     * @return A name that unambiguously (but not necessarily uniquely)
     *         identifies this content node.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the component that currently is used for rendering this content
     * node. Called by {@link Presentation#visit(View)}
     * 
     * @param rendition
     *            The component that currently renders this content.
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
     * Gets the component that currently is used for rendering this content
     * node.
     * 
     * @return The component that currently renders this content.
     */
    public T getRendition() {
        return rendition;
    }
}
