/*
 * Copyright 2011 Vaadin Ltd.
 * 
 * Licensed under the GNU Affero General Public License, Version 3 (the 
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

import org.vaadin.jefferson.Control;

import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.TextField;

public class TextControl extends Control<AbstractTextField, TextChangeListener> {

    public TextControl(String name) {
        super(name, AbstractTextField.class, TextChangeListener.class);
    }

    @Override
    public AbstractTextField createFallback() {
        TextField rendition = new TextField();
        rendition.setInputPrompt(getName());
        return rendition;
    }

    public String getText() {
        AbstractTextField rendition = getRendition();
        if (rendition == null) {
            return "";
        }
        Object value = rendition.getValue();
        return value == null ? "" : "" + value;
    }
}
