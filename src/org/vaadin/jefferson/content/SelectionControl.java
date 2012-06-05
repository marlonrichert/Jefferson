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

import java.lang.reflect.Array;
import java.util.*;

import org.vaadin.jefferson.*;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;

public class SelectionControl<T>
        extends Control<AbstractSelect, ValueChangeListener> {
    private Class<T> beanType;
    private BeanItemContainer<T> model;
    private T[] selection;

    public SelectionControl(String name, Class<T> beanType) {
        super(name, AbstractSelect.class, ValueChangeListener.class);
        this.beanType = beanType;
        model = new BeanItemContainer<>(beanType);
        selection = (T[]) Array.newInstance(beanType, 0);
    }

    @SafeVarargs
    public final void setChoices(T... choices) {
        setModel(new BeanItemContainer<>(beanType, Arrays.asList(choices)));
    }

    public void setModel(BeanItemContainer<T> model) {
        AbstractSelect presentation = getPresentation();
        if (presentation != null) {
            presentation.setContainerDataSource(model);
        }
        this.model = model;
    }

    public BeanItemContainer<T> getModel() {
        return model;
    }

    @SafeVarargs
    public final void setSelection(T... selection) {
        AbstractSelect presentation = getPresentation();
        if (presentation != null) {
            switch (selection.length) {
            case 0:
                presentation.setValue(null);
                break;
            case 1:
                presentation.setValue(selection[0]);
                break;
            default:
                presentation.setValue(Arrays.asList(selection));
            }
        }
        this.selection = selection;
    }

    @SuppressWarnings("unchecked")
    public T[] getSelection() {
        AbstractSelect presentation = getPresentation();
        if (presentation != null) {
            Object value = presentation.getValue();
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
    protected AbstractSelect accept(Presenter p) {
        AbstractSelect presentation = super.accept(p);
        if (presentation instanceof Table) {
            ((Table) presentation).setSelectable(true);
        }
        setModel(model);
        setSelection(selection);
        return presentation;
    }
}
