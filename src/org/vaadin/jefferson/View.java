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
 * A content node.
 * 
 * @param <T>
 *            This view's base rendering class.
 * @author Marlon Richert @ Vaadin
 */
public abstract class View<T extends Component> {
    private String name;
    private Class<T> base;

    private T rendition;
    private Presentation presentation;
    private Composite<?> parent;

    public View(String name, Class<T> base) {
        this.name = name;
        this.base = base;
    }

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
     * Gets the class whose interface renditions of this view should implement.
     * 
     * @return The return type of {@link #getRendition()}.
     */
    public Class<T> getBase() {
        return base;
    }

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

    public Composite<?> getParent() {
        return parent;
    }

    private void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

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
                throw new IllegalArgumentException(base
                        + " is not a superclass of " + renditionClass);
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
    public T getRendition() {
        return rendition;
    }
}
