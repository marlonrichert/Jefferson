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
 * @author Marlon Richert
 */
public abstract class UIElement<T extends Component> {
    private final String name;
    private T rendition;

    /**
     * Creates a new content node with the given name. It is preferred to use
     * only lower-case letters and hyphens (-), but this is not enforced.
     * 
     * @param name
     *            A preferably (but not enforcedly) unique name.
     */
    public UIElement(String name) {
        this.name = name;
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
     * Gets the default class used for rendering this content node. Called by
     * {@link Presentation#render(UIElement)} if it cannot find any rules to
     * instantiate this content with.
     * 
     * @return A class that can be instantiated as a fall-back.
     */
    public abstract Class<? extends T> getDefaultRenditionClass();

    /**
     * Sets the component that currently is used for rendering this content
     * node. Called by {@link Presentation#render(UIElement)}
     * 
     * @param component
     *            The component that currently renders this content.
     */
    public void setRendition(T component) {
        rendition = component;
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
