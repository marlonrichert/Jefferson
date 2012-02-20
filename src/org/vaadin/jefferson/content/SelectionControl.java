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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import org.vaadin.jefferson.Control;
import org.vaadin.jefferson.Presentation;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;

public class SelectionControl<T>
        extends Control<AbstractSelect, ValueChangeListener> {
    private Class<T> beanType;
    private BeanItemContainer<T> model;
    private T[] selection;

    @SuppressWarnings("unchecked")
    public SelectionControl(String name, Class<T> beanType) {
        super(name, AbstractSelect.class, ValueChangeListener.class);
        this.beanType = beanType;
        model = new BeanItemContainer<T>(beanType);
        selection = (T[]) Array.newInstance(beanType, 0);
    }

    public void setChoices(T... choices) {
        setModel(new BeanItemContainer<T>(beanType, Arrays.asList(choices)));
    }

    public void setModel(BeanItemContainer<T> model) {
        AbstractSelect rendition = getRendition();
        if (rendition != null) {
            rendition.setContainerDataSource(model);
        }
        this.model = model;
    }

    public BeanItemContainer<T> getModel() {
        return model;
    }

    public void setSelection(T... selection) {
        AbstractSelect rendition = getRendition();
        if (rendition != null) {
            switch (selection.length) {
            case 0:
                rendition.setValue(null);
                break;
            case 1:
                rendition.setValue(selection[0]);
                break;
            default:
                rendition.setValue(Arrays.asList(selection));
            }
        }
        this.selection = selection;
    }

    @SuppressWarnings("unchecked")
    public T[] getSelection() {
        AbstractSelect rendition = getRendition();
        if (rendition != null) {
            Object value = rendition.getValue();
            if (value instanceof Collection<?>) {
                Collection<T> collection = (Collection<T>) value;
                selection = collection.toArray(
                        (T[]) Array.newInstance(beanType, collection.size()));
            } else {
                selection = (T[]) Array.newInstance(beanType, 1);
                selection[0] = (T) value;
            }
        }
        return selection;
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
        setModel(model);
        setSelection(selection);
        return rendition;
    }
}
