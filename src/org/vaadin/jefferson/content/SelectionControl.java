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

public class SelectionControl<B>
        extends Control<AbstractSelect, ValueChangeListener> {
    private Class<B> beanType;
    private BeanItemContainer<B> model;
    private B[] selection;

    @SuppressWarnings("unchecked")
    public SelectionControl(String name, Class<B> beanType) {
        super(name, AbstractSelect.class, ValueChangeListener.class);
        this.beanType = beanType;
        model = new BeanItemContainer<B>(beanType);
        selection = (B[]) Array.newInstance(beanType, 0);
    }

    public void setChoices(B... choices) {
        setModel(new BeanItemContainer<B>(beanType, Arrays.asList(choices)));
    }

    public void setModel(BeanItemContainer<B> model) {
        AbstractSelect rendition = getRendition();
        if (rendition != null) {
            rendition.setContainerDataSource(model);
        }
        this.model = model;
    }

    public BeanItemContainer<B> getModel() {
        return model;
    }

    public void setSelection(B... selection) {
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
    public B[] getSelection() {
        AbstractSelect rendition = getRendition();
        if (rendition != null) {
            Object value = rendition.getValue();
            if (value instanceof Collection<?>) {
                Collection<B> collection = (Collection<B>) value;
                selection = collection.toArray(
                        (B[]) Array.newInstance(beanType, collection.size()));
            } else {
                selection = (B[]) Array.newInstance(beanType, 1);
                selection[0] = (B) value;
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
