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

import java.util.HashMap;
import java.util.Map;

import org.vaadin.jefferson.content.UIElement;
import org.vaadin.jefferson.content.View;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

public class Presentation {

    private final Map<String, Class<? extends Component>> nameRules = new HashMap<String, Class<? extends Component>>();
    private final Map<Class<? extends UIElement>, Class<? extends Component>> typeRules = new HashMap<Class<? extends UIElement>, Class<? extends Component>>();

    public void define(String name, Class<? extends Component> rendition) {
        nameRules.put(name, rendition);
    }

    public void define(Class<? extends UIElement> type,
            Class<? extends Component> rendition) {
        typeRules.put(type, rendition);
    }

    public Component render(UIElement content) throws InstantiationException,
            IllegalAccessException {
        Class<? extends Component> rendition1 = nameRules
                .get(content.getName());
        if (rendition1 == null) {
            rendition1 = typeRules.get(content.getClass());
            if (rendition1 == null) {
                rendition1 = content.getDefault();
            }
        }
        Class<? extends Component> rendition = rendition1;
        Component component = rendition.newInstance();
        String name = content.getName();

        component.addStyleName(name);
        component.setCaption(name);
        content.setComponent(component);

        if (content instanceof View) {
            for (UIElement child : ((View) content).getChildren()) {
                ((ComponentContainer) component).addComponent(render(child));
            }
        }

        return component;
    }
}
