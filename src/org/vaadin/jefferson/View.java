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

import com.vaadin.ui.Component;

/**
 * A content node that allows itself to be rendered as a Vaadin
 * {@link Component}. Convenience implementations for specific types of
 * renditions can be found in the {@link org.vaadin.jefferson.content} package.
 * 
 * @param <T>
 *            The base class of this view's rendition.
 * @author Marlon Richert @ Vaadin
 */
public abstract class View<T extends Component> {
    private String name;
    private Class<T> base;

    private T rendition;
    private Presentation presentation;
    private Composite<?> parent;

    /**
     * Creates a new view.
     * 
     * @see #getName()
     * @see #getBase()
     */
    public View(String name, Class<T> base) {
        this.name = name;
        this.base = base;
    }

    /**
     * Creates a fallback rendition, in case the {@link Presentation} visiting
     * this view does not know how to render it.
     * 
     * @return A newly-created rendition.
     */
    public abstract T createFallback();

    /**
     * Gets this view's name.
     * 
     * @return A human-readable name that identifies this view.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the class from which this view's rendition should inherit.
     * 
     * @return The return type of {@link #getRendition()}.
     */
    public Class<T> getBase() {
        return base;
    }

    /**
     * Accepts the given presentation and returns this view's rendition.
     */
    protected T accept(Presentation p) {
        setPresentation(p);
        return rendition;
    }

    void setParent(Composite<?> parent) {
        this.parent = parent;
        if (parent == null) {
            setRendition(null);
        }
    }

    /**
     * Gets this view's parent view.
     * 
     * @return <code>null</code>, if this view does not belong to any parent.
     */
    public Composite<?> getParent() {
        return parent;
    }

    private void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    /**
     * Gets the presentation that most recently visited this view.
     * 
     * @see #accept(Presentation)
     */
    protected Presentation getPresentation() {
        return presentation;
    }

    /**
     * Sets this view's rendition. Does nothing and returns <code>false</code>
     * if the given rendition is the same as this view' current one; otherwise,
     * replaces this view's rendition with the given one and returns
     * <code>true</code>.
     * 
     * @param rendition
     *            The new rendering component.
     * @return <code>true</code> if this action resulted in any changes;
     *         <code>false</code> if it did not.
     */
    protected boolean setRendition(T rendition) {
        if (this.rendition == rendition) {
            return false;
        }
        if (rendition != null) {
            Class<? extends Component> renditionClass = rendition.getClass();
            if (!base.isAssignableFrom(renditionClass)) {
                throw new IllegalArgumentException(
                        base + " is not a superclass of " + renditionClass);
            }
        }
        if (parent != null) {
            parent.update(this.rendition, rendition);
        }
        this.rendition = rendition;
        return true;
    }

    /**
     * Gets the component that is used for rendering this view.
     * 
     * @return The component that renders this content.
     */
    protected T getRendition() {
        return rendition;
    }
}
