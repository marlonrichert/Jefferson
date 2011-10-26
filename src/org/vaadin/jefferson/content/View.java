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

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * A composite {@link UIElement}, typically rendered as a subclass of
 * {@link ComponentContainer}.
 * 
 * @author Marlon Richert
 */
public class View extends UIElement {

    private UIElement[] children;

    /**
     * Creates a new view with the given name and children.
     * 
     * @param name
     *            This view's name.
     * @param children
     *            This view's child content nodes.
     */
    public View(String name, UIElement... children) {
        super(name);
        this.children = children;
    }

    /**
     * Gets this view's children.
     * 
     * @return This view's child content nodes.
     */
    public UIElement[] getChildren() {
        return children;
    }

    /**
     * Sets this view's children.
     * 
     * @param children
     *            This view's child content nodes.
     */
    public void setChildren(UIElement... children) {
        this.children = children;
    }

    @Override
    public Class<? extends ComponentContainer> getDefaultRenditionClass() {
        return CssLayout.class;
    }
}
