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

import java.lang.reflect.*;

import com.vaadin.ui.*;
import com.vaadin.ui.Field;

/**
 * A {@link View} that can register a controller to its presentation.
 * 
 * @param <C>
 *            The interface of this control's controller.
 * @author Marlon Richert @ Vaadin
 */
public abstract class Control<P extends Component, C> extends View<P> {
    private C controller;
    private Method removeListener;
    private Method addListener;

    public Control(String name, Class<P> base, Class<C> controllerBase) {
        super(name, base);
        try {
            removeListener = base.getMethod("removeListener", controllerBase);
            addListener = base.getMethod("addListener", controllerBase);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Control(String name, Class<P> base, Class<C> listenerBase, C listener) {
        this(name, base, listenerBase);
        setController(listener);
    }

    public void setController(C listener) {
        P presentation = getPresentation();
        removeListener(presentation, this.controller);
        addListener(presentation, listener);
        this.controller = listener;
    }

    public C getController() {
        return controller;
    }

    @Override
    protected P accept(Presenter presenter) {
        P presentation = super.accept(presenter);
        setController(controller);
        return presentation;
    }

    @Override
    protected boolean setPresentation(P presentation) {
        removeListener(getPresentation(), controller);
        return super.setPresentation(presentation);
    }

    private void removeListener(P presentation, C listener) {
        try {
            if (presentation != null && listener != null) {
                removeListener.invoke(presentation, listener);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void addListener(P presentation, C listener) {
        try {
            if (presentation != null) {
                if (presentation instanceof AbstractComponent) {
                    ((AbstractComponent) presentation).setImmediate(true);
                }
                if (presentation instanceof Field) {
                    ((Field) presentation).setWriteThrough(true);
                }
                if (listener != null) {
                    addListener.invoke(presentation, listener);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
