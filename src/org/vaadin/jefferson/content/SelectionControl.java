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

import org.vaadin.jefferson.Control;
import org.vaadin.jefferson.Presentation;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;

public class SelectionControl
        extends Control<AbstractSelect, ValueChangeListener> {

    public SelectionControl(String name) {
        super(name, AbstractSelect.class, ValueChangeListener.class);
    }

    public SelectionControl(String name, ValueChangeListener listener) {
        this(name);
        setListener(listener);
    }

    @Override
    public AbstractSelect createFallback() {
        return new NativeSelect(getName());
    }

    @Override
    protected AbstractSelect accept(Presentation p) {
        AbstractSelect rendition = super.accept(p);
        if (rendition instanceof Table) {
            ((Table) rendition).setSelectable(true);
        }
        return rendition;
    }
}
