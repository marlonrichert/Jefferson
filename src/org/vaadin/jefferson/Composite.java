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
package org.vaadin.jefferson;

import java.util.LinkedHashSet;
import java.util.Set;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * A {@link View} with child views, rendered as a subclass of
 * {@link com.vaadin.ui.ComponentContainer}.
 * 
 * @param <T>
 *            This view's base rendering class.
 * @author Marlon Richert @ Vaadin
 */
public abstract class Composite<T extends ComponentContainer> extends View<T> {
    private Set<View<?>> children = new LinkedHashSet<View<?>>();

    public Composite(String name, Class<T> base) {
        super(name, base);
    }

    public Composite(String name, Class<T> base, View<?>... children) {
        this(name, base);
        setChildren(children);
    }

    /**
     * Sets this view's children.
     * 
     * @param children
     *            This view's child content nodes.
     */
    public Composite<T> setChildren(View<?>... children) {
        for (View<?> child : this.children) {
            child.setParent(null);
        }
        this.children.clear();
        for (View<?> child : children) {
            this.children.add(child);
            child.setParent(this);
        }

        T ownRendition = getRendition();
        if (ownRendition != null) {
            ownRendition.removeAllComponents();
            for (View<?> child : children) {
                Component childRendition = child.getRendition();
                if (childRendition != null) {
                    ownRendition.addComponent(childRendition);
                }
            }
        }

        return this;
    }

    public <X extends Component, Y extends Component> boolean replaceChild(
            View<X> oldChild, View<Y> newChild) {
        if (oldChild == newChild) {
            return false;
        }

        newChild.setRendition(newChild.createFallback());

        Component oldRendition = oldChild.getRendition();
        Component newRendition = newChild.getRendition();

        if (children.contains(oldChild)) {
            children.remove(oldChild);
            oldChild.setParent(null);
        }
        update(oldRendition, newRendition);
        children.add(newChild);
        newChild.setParent(this);

        Presentation presentation = getPresentation();
        if (presentation != null) {
            presentation.visit(newChild);
        }
        return true;
    }

    void update(Component oldRendition, Component newRendition) {
        T ownRendition = getRendition();
        if (ownRendition != null) {
            if (oldRendition != null
                    && oldRendition.getParent() == ownRendition) {
                if (newRendition != null) {
                    ownRendition.replaceComponent(oldRendition, newRendition);
                } else {
                    ownRendition.removeComponent(oldRendition);
                }
            } else if (newRendition != null) {
                ownRendition.addComponent(newRendition);
            }
        }
    }

    /**
     * Sets this view's rendering component and calls
     * {@link Presentation#visit(View)} for each of its children.
     */
    @Override
    protected T accept(Presentation presentation) {
        T rendition = super.accept(presentation);
        for (View<?> child : children) {
            presentation.visit(child);
        }
        return rendition;
    }

    /**
     * Gets this view's children.
     * 
     * @return This view's child content nodes.
     */
    public View<?>[] getChildren() {
        return children.toArray(new View<?>[children.size()]);
    }

    @Override
    protected boolean setRendition(T rendition) {
        if (rendition == null) {
            for (View<?> child : getChildren()) {
                child.setRendition(null);
            }
        }
        return super.setRendition(rendition);
    }
}
