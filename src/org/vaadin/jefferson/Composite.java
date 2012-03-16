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
package org.vaadin.jefferson;

import java.util.LinkedHashSet;
import java.util.Set;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * A {@link View} that can contain child views, rendered as a subclass of
 * {@link com.vaadin.ui.ComponentContainer}.
 * 
 * @param <T>
 *            This view's base rendering class.
 * @author Marlon Richert @ Vaadin
 */
public abstract class Composite<T extends ComponentContainer> extends View<T> {
    private Set<View<?>> children = new LinkedHashSet<View<?>>();

    /**
     * Creates a new composite view without children.
     * 
     * @see View#View(String, Class)
     */
    public Composite(String name, Class<T> base) {
        super(name, base);
    }

    /**
     * Creates a new composite view containing the given children.
     * 
     * @param children
     *            This composite view's content.
     * @see View#View(String, Class)
     */
    public Composite(String name, Class<T> base, View<?>... children) {
        this(name, base);
        setChildren(children);
    }

    /**
     * Sets this composite's child views. Returns a convenience reference to
     * this composite, so you can easily do things like this:
     * 
     * <pre>
     * parent.setChildren(
     *     child1.setChildren(
     *         grandChild1,
     *         grandChild2)
     *     child2)
     * </pre>
     * 
     * @param children
     *            This composite view's content.
     * @return This composite.
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

    /**
     * Accepts the given presentation and calls {@link Presentation#visit(View)}
     * for each of this composite's children.
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
     * Sets this composite's rendition. If the given rendition is
     * <code>null</code>, it will also clear the rendition of each of this
     * composite's children.
     */
    @Override
    protected boolean setRendition(T rendition) {
        if (rendition == null) {
            for (View<?> child : getChildren()) {
                child.setRendition(null);
            }
        }
        return super.setRendition(rendition);
    }

    /**
     * Replaces the given existing child in this composite view with the given
     * replacement child.
     * 
     * @param existing
     *            The old child to replace.
     * @param replacement
     *            The new child with which to replace the old one.
     * @return <code>true</code> if this resulted in any changes; otherwise,
     *         <code>false</code>
     */
    public <X extends Component, Y extends Component> boolean replaceChild(
            View<X> existing, View<Y> replacement) {
        if (existing == replacement) {
            return false;
        }

        replacement.setRendition(replacement.createFallback());

        Component oldRendition = existing.getRendition();
        Component newRendition = replacement.getRendition();

        if (children.contains(existing)) {
            children.remove(existing);
            existing.setParent(null);
        }
        update(oldRendition, newRendition);
        children.add(replacement);
        replacement.setParent(this);

        Presentation presentation = getPresentation();
        if (presentation != null) {
            presentation.visit(replacement);
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
     * Gets this view's children.
     * 
     * @return This view's child content nodes.
     */
    public View<?>[] getChildren() {
        return children.toArray(new View<?>[children.size()]);
    }
}
