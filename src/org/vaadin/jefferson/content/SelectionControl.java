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

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;

/**
 * A list of data items, typically rendered as a subclass of
 * {@link AbstractSelect}.
 * 
 * @author marlon
 */
public class SelectionControl extends Control {

    /**
     * Creates a new selection with the given name.
     * 
     * @param name
     *            This selection's name.
     */
    public SelectionControl(String name) {
        super(name);
    }

    @Override
    public Class<? extends Component> getDefaultRenditionClass() {
        return NativeSelect.class;
    }
}
