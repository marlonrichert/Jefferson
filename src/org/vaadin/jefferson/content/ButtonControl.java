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

import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;

/**
 * A button, typically rendered as a {@link com.vaadin.ui.Button} or
 * {@link com.vaadin.ui.NativeButton}.
 * 
 * @author Marlon Richert
 */
public class ButtonControl extends Control<Button> {

    /**
     * Creates a button with the given name.
     * 
     * @param name
     *            This button's name.
     */
    public ButtonControl(String name) {
        super(name);
    }

    @Override
    public Class<? extends Button> getDefaultRenderingClass() {
        return NativeButton.class;
    }

    @Override
    public Class<Button> getRenderingInterface() {
        return Button.class;
    }
}
