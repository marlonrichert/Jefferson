/*
 * Copyright 2011, 2012 Vaadin Ltd.
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

import com.vaadin.ui.Component;

/**
 * A content node that allows itself to be presented as a Vaadin
 * {@link Component}. Convenience implementations for specific types of views
 * can be found in the {@link org.vaadin.jefferson.content} package.
 * 
 * @param <P>
 *            This view's base presentation class.
 * @author Marlon Richert @ Vaadin
 */
public abstract class View<P extends Component> {
    private String name;
    private Class<P> presentationBase;

    private P presentation;
    private Presenter presenter;
    private Composite<?> parent;

    /**
     * Creates a new view.
     * 
     * @see #getName()
     * @see #getPresentationBase()
     */
    public View(String name, Class<P> presentationBase) {
        this.name = name;
        this.presentationBase = presentationBase;
    }

    /**
     * Creates a fallback presentation, in case the {@link Presenter} visiting
     * this view does not know how to present it.
     * 
     * @return A newly-created presentation.
     */
    public abstract P createFallback();

    /**
     * Gets this view's name.
     * 
     * @return A human-readable name that identifies this view.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the class from which this view's presentation should inherit.
     * 
     * @return The return type of {@link #getPresentation()}.
     */
    public Class<P> getPresentationBase() {
        return presentationBase;
    }

    /**
     * Accepts the given presenter and returns this view's presentation.
     */
    protected P accept(Presenter p) {
        setPresenter(p);
        return presentation;
    }

    void setParent(Composite<?> parent) {
        this.parent = parent;
        if (parent == null) {
            setPresentation(null);
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

    private void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Gets the presenter that most recently visited this view.
     * 
     * @see #accept(Presenter)
     */
    protected Presenter getPresenter() {
        return presenter;
    }

    /**
     * Sets this view's presentation.
     * 
     * @return <code>true</code> if this action resulted in any changes;
     *         <code>false</code> if it did not.
     */
    protected boolean setPresentation(P presentation) {
        if (this.presentation == presentation) {
            return false;
        }
        if (presentation != null) {
            Class<? extends Component> presentationClass = presentation
                    .getClass();
            if (!presentationBase.isAssignableFrom(presentationClass)) {
                throw new IllegalArgumentException(
                        presentationBase + " is not a superclass of "
                                + presentationClass);
            }
        }
        if (parent != null) {
            parent.update(this.presentation, presentation);
        }
        this.presentation = presentation;
        return true;
    }

    protected P getPresentation() {
        return presentation;
    }
}
