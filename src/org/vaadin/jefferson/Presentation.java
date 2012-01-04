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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.Component;

/**
 * A presentation of some Vaadin content. The normal usage is to define some
 * rules using its various <code>define(…)</code> methods and then call
 * {@link #visit(View)}.
 * 
 * @author Marlon Richert @ Vaadin
 */
public class Presentation {
    private static final String INVALID_CSS = "[^-_a-zA-Z]";
    private static final String WHITESPACE = "\\s+";

    /**
     * Renders the given {@link View content} according to the rules defined in
     * this presentation.
     * <p>
     * After instantiating the rendition, it passes it to
     * {@link View#accept(Presentation, Component)}. It then sets the size of
     * the content's rendition to
     * {@link com.vaadin.ui.Component#setSizeUndefined() undefined} and adds a
     * CSS-friendly {@link com.vaadin.ui.Component#addStyleName(String) style
     * name} to it by converting the content's name to lower-case and replacing
     * spaces with hyphens (-). Finally, it passes the rendition any
     * rule-defined methods.
     * <p>
     * 
     * @param <T>
     *            The base type of the rendition.
     * @param view
     *            The content hierarchy to render.
     * @return The top-level rendition component of the hierarchy.
     */
    public <T extends Component> T visit(View<T> view) {
        @SuppressWarnings("unchecked")
        T rendition = (T) call("create", view, view.getFallback());

        view.accept(this, rendition);

        call("visit", view, rendition);

        return rendition;
    }

    protected Component create(View<?> content, Component fallback) {
        return fallback;
    }

    protected void visit(View<?> view, Component rendition) {
        rendition.addStyleName(style(view.getName()));
        rendition.setSizeUndefined();
    }

    private Object call(String name, Object... params) {
        Method method = getMethod(name, getTypes(params));
        try {
            return method.invoke(Presentation.this, params);
        } catch (IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        } catch (InvocationTargetException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Method getMethod(String name, Class<?>... paramTypes) {
        return AccessController.doPrivileged(new AccessibleMethod(
                ReflectTools.findMethod(getClass(), name, paramTypes)));
    }

    private Class<?>[] getTypes(Object... params) {
        Class<?>[] paramTypes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        return paramTypes;
    }

    private String style(String name) {
        return name.replaceAll(WHITESPACE, "-").replaceAll(INVALID_CSS, "")
                .toLowerCase();
    }

    private final static class AccessibleMethod implements
            PrivilegedAction<Method> {
        private final Method method;

        private AccessibleMethod(Method method) {
            this.method = method;
        }

        public Method run() {
            method.setAccessible(true);
            return method;
        }
    }
}
