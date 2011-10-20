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
import java.util.HashMap;
import java.util.Map;

import org.vaadin.jefferson.content.UIElement;
import org.vaadin.jefferson.content.View;

import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

public class Presentation {

    private final Map<String, Class<? extends Component>> nameClasses = new HashMap<String, Class<? extends Component>>();
    private final Map<Class<? extends UIElement>, Class<? extends Component>> typeClasses = new HashMap<Class<? extends UIElement>, Class<? extends Component>>();
    private final Map<Class<? extends UIElement>, String> typeMethods = new HashMap<Class<? extends UIElement>, String>();
    private final Map<String, String> nameMethods = new HashMap<String, String>();

    private Presenter presenter;

    public Presentation() {
        this(new Presenter() {
            @Override
            public void register(UIElement content, Component component) {
                return;
            }
        });
    }

    public Presentation(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void define(String name, Class<? extends Component> rendition) {
        nameClasses.put(name, rendition);
    }

    public void define(Class<? extends UIElement> type,
            Class<? extends Component> rendition) {
        typeClasses.put(type, rendition);
    }

    public void define(String name, String init) {
        nameMethods.put(name, init);
    }

    public void define(Class<? extends UIElement> type, String init) {
        typeMethods.put(type, init);
    }

    public Component render(UIElement content) throws InstantiationException,
            IllegalAccessException, InvocationTargetException {
        String name = content.getName();
        Class<? extends UIElement> type = content.getClass();

        Class<? extends Component> rendition = nameClasses.get(name);
        if (rendition == null) {
            rendition = typeClasses.get(type);
            if (rendition == null) {
                rendition = content.getDefault();
            }
        }
        Component component = rendition.newInstance();

        init(content, component, typeMethods.get(type));
        init(content, component, nameMethods.get(name));

        component.addStyleName(name);
        component.setSizeUndefined();

        if (content instanceof View) {
            for (UIElement child : ((View) content).getChildren()) {
                ((ComponentContainer) component).addComponent(render(child));
            }
        }

        content.setComponent(component);
        presenter.register(content, component);

        return component;
    }

    protected void init(UIElement content, Component component,
            String methodName) throws IllegalAccessException,
            InvocationTargetException {
        if (methodName != null) {
            ReflectTools.findMethod(getClass(), methodName, UIElement.class,
                    Component.class).invoke(Presentation.this, content,
                    component);
        }
    }
}
