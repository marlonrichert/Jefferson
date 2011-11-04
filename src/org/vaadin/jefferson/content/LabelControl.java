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

import com.vaadin.ui.Label;

/**
 * A static text label, typically rendered as a {@link Label}.
 * 
 * @author Marlon Richert
 */
public class LabelControl extends Control<Label> {

    /**
     * Creates a new text label with the given name.
     * 
     * @param name
     *            This label's name.
     */
    public LabelControl(String name) {
        super(name);
    }

    @Override
    public Class<? extends Label> getDefaultRenditionClass() {
        return Label.class;
    }

    @Override
    public Class<Label> getRenditionInterface() {
        return Label.class;
    }
}
