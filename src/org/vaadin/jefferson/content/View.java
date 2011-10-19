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
 * A composite {@link UIElement}.
 * 
 * @author marlon
 */
public class View extends UIElement {

    private UIElement[] children;

    public View(String name, UIElement... children) {
        super(name);
        this.children = children;
    }

    public UIElement[] getChildren() {
        return children;
    }

    public void setChildren(UIElement... children) {
        this.children = children;
    }

    @Override
    public Class<? extends ComponentContainer> getDefault() {
        return CssLayout.class;
    }
}
