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

import com.vaadin.ui.Component;

/**
 * @author Marlon Richert @ Vaadin
 */
public class Presentation {
    private static final String RENDER = "render";
    private static final String STYLE = "style";
    private static final String INVALID_CSS = "[^-_a-zA-Z]";
    private static final String WHITESPACE = "\\s+";

    public <T extends Component> T visit(View<T> view) {
        @SuppressWarnings("unchecked")
        T rendition = (T) call(RENDER, view);

        view.setRendition(rendition);
        view.accept(this);

        call(STYLE, view);

        return rendition;
    }

    protected Component render(View<?> view) {
        return view.createFallback();
    }

    protected void style(View<?> view) {
        Component rendition = view.getRendition();
        rendition.addStyleName(view.getName().replaceAll(
                WHITESPACE, "-").replaceAll(INVALID_CSS, "").toLowerCase());
        rendition.setSizeUndefined();
    }

    private Object call(String name, View<?> view) {
        Method method = getMethod(name, view);
        try {
            return method.invoke(this, view);
        } catch (IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        } catch (InvocationTargetException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Method getMethod(String name, View<?> view) {
        Class<?> presentationCls = getClass();
        while (presentationCls != null) {
            Class<?> viewCls = view.getClass();
            while (viewCls != null) {
                try {
                    Method method = presentationCls.getDeclaredMethod(
                            name, viewCls);
                    return AccessController.doPrivileged(
                            new AccessibleMethod(method));
                } catch (NoSuchMethodException e) {
                    viewCls = viewCls.getSuperclass();
                }
            }
            presentationCls = presentationCls.getSuperclass();
        }
        throw new ExceptionInInitializerError("Can't find any method "
                + getClass() + "." + name + "(" + view.getClass() + ").");
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
