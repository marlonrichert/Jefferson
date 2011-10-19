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
package org.vaadin.jefferson.demo;

import java.util.Iterator;

import org.vaadin.jefferson.Presentation;
import org.vaadin.jefferson.content.ButtonControl;
import org.vaadin.jefferson.content.LabelControl;
import org.vaadin.jefferson.content.SelectionControl;
import org.vaadin.jefferson.content.UIElement;
import org.vaadin.jefferson.content.View;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

public class JeffersonDemo extends Application {
    @Override
    public void init() {
        setTheme("chameleon");
        Presentation presentation = getPresentation();
        UIElement content = getContent();

        Window mainWindow = new Window("Jefferson Demo");
        try {
            mainWindow.addComponent(presentation.render(content));
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setMainWindow(mainWindow);
    }

    private static Presentation getPresentation() {
        Presentation presentation = new Presentation() {
            {
                define("tabs", TabSegment.class);
            }

            public Component create(SelectionControl content) {
                return new Table();
            }
        };

        return presentation;
    }

    private static UIElement getContent() {
        LabelControl title = new LabelControl("title");
        SelectionControl tabs = new SelectionControl("tabs");
        ButtonControl newEstate = new ButtonControl("new-item");
        ButtonControl user = new ButtonControl("user");
        ButtonControl signOut = new ButtonControl("sign-out");

        SelectionControl estates = new SelectionControl("estates");

        LabelControl name = new LabelControl("name");
        ButtonControl open = new ButtonControl("open");
        SelectionControl expenses = new SelectionControl("expenses");
        ButtonControl addExpense = new ButtonControl("add-expense");

        UIElement max = new PropertyView("max");
        UIElement date = new PropertyView("date");
        UIElement state = new PropertyView("state");

        View properties = new View("properties", max, date, state);
        View details = new View("details", name, open, properties, expenses,
                addExpense);
        View nav = new View("nav", title, tabs, newEstate, user, signOut);
        View main = new View("main", estates, details);
        return new View("root", nav, main);
    }

    public static class PropertyView extends View {

        public PropertyView(String name) {
            super(name, new LabelControl("key"), new LabelControl("value"));
        }
    }

    public static class TabSegment extends HorizontalLayout {

        public TabSegment() {
            addStyleName("segment");
            addButton(new Button("Open"));
            addButton(new Button("Ongoing"));
        }

        public TabSegment addButton(Button b) {
            addComponent(b);
            b.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    if (event.getButton().getStyleName().indexOf("down") == -1) {
                        event.getButton().addStyleName("down");
                    } else {
                        event.getButton().removeStyleName("down");
                    }
                }
            });
            updateButtonStyles();
            return this;
        }

        private void updateButtonStyles() {
            int i = 0;
            Component c = null;
            for (Iterator<Component> iterator = getComponentIterator(); iterator
                    .hasNext();) {
                c = iterator.next();
                c.removeStyleName("first");
                c.removeStyleName("last");
                if (i == 0) {
                    c.addStyleName("first");
                }
                i++;
            }
            if (c != null) {
                c.addStyleName("last");
            }
        }
    }
}
