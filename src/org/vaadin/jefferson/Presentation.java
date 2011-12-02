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

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;


import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.Component;

/**
 * A presentation of some Vaadin content. The normal usage is to define some
 * rules using its various <code>define(…)</code> methods and then call
 * {@link #render(View)}.
 * 
 * @author Marlon Richert @ Vaadin
 */
public class Presentation {
    private static final String INVALID_CSS = "[^-_a-zA-Z]";
    private static final String WHITESPACE = "\\s+";
    private final Map<String, Class<? extends Component>> nameClasses = new HashMap<String, Class<? extends Component>>();
    private final Map<Class<? extends Component>, Class<? extends Component>> typeClasses = new HashMap<Class<? extends Component>, Class<? extends Component>>();
    private final Map<Class<? extends Component>, Method[]> typeMethods = new HashMap<Class<? extends Component>, Method[]>();
    private final Map<String, Method[]> nameMethods = new HashMap<String, Method[]>();

    /**
     * Defines a rule that a {@link View} with the given name should be rendered
     * as an instance of the given subclass of {@link Component}. If this is not
     * possible at run-time, the given rule will be ignored. Instantiation is
     * done by calling the String-arg or (if that fails) no-args constructor of
     * the given class.
     * <p>
     * Rules defined with this method take precedence over those defined with
     * {@link #define(Class, Class)}.
     * 
     * @param name
     *            The content's name.
     * @param impl
     *            The class to use for rendering.
     */
    public void define(String name, Class<? extends Component> impl) {
        nameClasses.put(name, impl);
    }

    /**
     * Defines a rule that a {@link View} with the given base rendition type
     * should be rendered as an instance of the given subclass of
     * {@link Component}. If this is not possible at run-time, the given rule
     * will be ignored. Instantiation is done by calling the String-arg or (if
     * that fails) no-args constructor of the given class.
     * <p>
     * Rules defined with {@link #define(String, Class)} take precedence over
     * those defined with this method.
     * <p>
     * 
     * @param base
     *            The content's base rendition class.
     * @param impl
     *            The class to actually use for rendering.
     */
    public void define(Class<? extends Component> base,
            Class<? extends Component> impl) {
        if (!base.isAssignableFrom(impl)) {
            throw new IllegalArgumentException(base
                    + " is not a superclass of " + impl);
        }
        typeClasses.put(base, impl);
    }

    /**
     * Returns the method in this presentation that has the signature
     * <code>name(View, Component)</code>. Convenience method for use with
     * {@link #define(Class, Method)} and {@link #define(String, Method)}.
     * 
     * @param name
     *            The name of the method to return.
     * @param base
     *            The type of rendition this method will accept.
     * @return A method in this presentation.
     */
    protected Method method(String name, Class<? extends Component> base) {
        Method method = ReflectTools.findMethod(getClass(), name, base);
        return AccessController.doPrivileged(new AccessibleMethod(method));
    }

    /**
     * Defines a rule that a {@link View} with the given name should be
     * initialized by a method with the given name in this presentation. The
     * actual method should be defined in this presentation by sub-classing and
     * should have the signature <code>methodName(View, Component)</code>.
     * <p>
     * Rules defined by this method are applied <i>after</i> those defined by
     * {@link #define(Class, String)}.
     * 
     * @param name
     *            The content's name.
     * @param methods
     *            Methods in this presentation.
     * @see #method(String)
     */
    public void define(String name, Method... methods) {
        nameMethods.put(name, methods);
    }

    /**
     * Defines a rule that a {@link View} with the given base rendition type
     * should be initialized by a method with the given name in this
     * presentation. The actual method should be defined in this presentation by
     * sub-classing and should have the signature
     * <code>methodName(View, Component)</code>.
     * <p>
     * Rules defined by this method are applied <i>before</i> those defined by
     * {@link #define(Class, String)}.
     * 
     * @param base
     *            The content's base rendition class.
     * @param methods
     *            Methods in this presentation.
     * @see #method(String)
     */
    public void define(Class<? extends Component> base, Method... methods) {
        typeMethods.put(base, methods);
    }

    /**
     * Renders the given {@link View content} according to the rules defined in
     * this presentation.
     * <p>
     * After instantiating the rendition, it passes it to
     * {@link View#update(Component, Presentation)}. It then sets the size of
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
     * @param content
     *            The content hierarchy to render.
     * @return The top-level rendition component of the hierarchy.
     */
    public <T extends Component> T render(View<T> content) {
        String name = content.getName();

        @SuppressWarnings("rawtypes")
        Class<? extends View> type = content.getClass();

        T rendition = create(content);

        content.update(rendition, this);

        rendition.addStyleName(style(name));

        rendition.setSizeUndefined();

        init(content, rendition, typeMethods.get(type));
        init(content, rendition, nameMethods.get(name));

        return rendition;
    }

    private String style(String name) {
        return name.replaceAll(WHITESPACE, "-").replaceAll(INVALID_CSS, "")
                .toLowerCase();
    }

    /**
     * Instantiates a rendition for the given content. If any rules are defined
     * for the given content, it will use those rules to instantiate the
     * rendition (if possible). If not, it will instantiate the content's
     * {@link View#getFallback() fall-back rendering class}.
     * 
     * @param content
     *            The content node to render.
     * @return An uninitialized rendition of the given content node.
     */
    protected <T extends Component> T create(View<T> content) {
        Class<T> impl = getRenderingClass(content);

        try {
            try {
                return impl.getConstructor(String.class).newInstance(
                        content.getName());
            } catch (NoSuchMethodException e) {
                return impl.getConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Gets the given content's rendering class. If there is a class defined for
     * the content's name, it will return that. If not, it will the class
     * defined for implementing the content's base rendering class. If that
     * fails, too, it simply return the content's fallback.
     * 
     * @param content
     *            The view to render.
     * @return A subclass of the view's base rendering class.
     */
    @SuppressWarnings("unchecked")
    protected <T extends Component> Class<T> getRenderingClass(View<T> content) {
        Class<T> base = content.getBase();
        Class<T> impl = (Class<T>) nameClasses.get(content.getName());
        if (impl == null || !base.isAssignableFrom(impl)) {
            impl = (Class<T>) typeClasses.get(content.getClass());
            if (impl == null || !base.isAssignableFrom(impl)) {
                impl = (Class<T>) content.getFallback();
            }
        }
        return impl;
    }

    /**
     * Initializes the given component by calling
     * <code>Presentation.this.methodName(content, component)</code>.
     * 
     * @param content
     *            The content for which the given component was rendered.
     * @param component
     *            The component used to render the given content.
     * @param methodName
     *            The name of the method with which to initialize the given
     *            component.
     */
    protected void init(View<?> content, Component component, Method... methods) {
        if (methods != null) {
            for (Method method : methods) {
                try {
                    method.invoke(Presentation.this, component);
                } catch (IllegalArgumentException e) {
                    // ignore this method and try the next one
                } catch (Exception e) {
                    throw new ExceptionInInitializerError(e);
                }
            }
        }
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
