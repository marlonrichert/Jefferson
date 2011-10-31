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

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * A composite {@link UIElement}, typically rendered as a subclass of
 * {@link ComponentContainer}.
 * 
 * @author Marlon Richert
 */
public class View extends UIElement<ComponentContainer> {

    private final Map<String, View> views = new HashMap<String, View>();

    private UIElement<?>[] children;

    /**
     * Creates a new view with the given name. Children can be added through
     * {@link #setChildren(UIElement...)}.
     * 
     * @param name
     *            This view's name.
     */
    protected View(String name) {
        this(name, new UIElement[] {});
    }

    /**
     * Creates a new view with the given name and children.
     * 
     * @param name
     *            This view's name.
     * @param children
     *            This view's child content nodes.
     */
    private View(String name, UIElement<?>... children) {
        super(name);
        this.children = children;
    }

    /**
     * Creates a new View and adds it to this view's registry.
     * 
     * @param viewName
     *            The name of the new View.
     * @param children
     *            The new view's children.
     * @return The new View.
     */
    protected View view(String viewName, UIElement<?>... viewChildren) {
        View view = new View(viewName, viewChildren);
        views.put(viewName, view);
        return view;
    }

    /**
     * Gets the View with the given name from this view's registry.
     * 
     * @param viewName
     *            The name of the View to get.
     * @return A View created with this view's
     *         {@link #view(String, UIElement...)} method.
     */
    protected View getView(String viewName) {
        return views.get(viewName);
    }

    /**
     * Gets this view's children.
     * 
     * @return This view's child content nodes.
     */
    public UIElement<?>[] getChildren() {
        return children;
    }

    /**
     * Sets this view's children.
     * 
     * @param children
     *            This view's child content nodes.
     */
    public void setChildren(UIElement<?>... children) {
        this.children = children;
    }

    @Override
    public Class<? extends ComponentContainer> getDefaultRenditionClass() {
        return CssLayout.class;
    }
}
