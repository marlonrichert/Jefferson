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

import org.vaadin.jefferson.content.ButtonControl;
import org.vaadin.jefferson.content.LabelControl;
import org.vaadin.jefferson.content.SelectionControl;
import org.vaadin.jefferson.content.UIElement;
import org.vaadin.jefferson.content.View;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class JeffersonDemo extends Application {
    @Override
    public void init() {
        Presentation presentation = getPresentation();
        UIElement content = getContent();

        Window mainWindow = new Window("Jefferson Demo");
        mainWindow.addComponent(presentation.render(content));
        setMainWindow(mainWindow);
    }

    private static Presentation getPresentation() {
        Presentation presentation = new Presentation();
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

        UIElement max = property("max");
        UIElement date = property("date");
        UIElement state = property("state");

        View properties = new View("properties", max, date, state);
        View details = new View("details", name, open, properties, expenses,
                addExpense);
        View nav = new View("nav", title, tabs, newEstate, user, signOut);
        View main = new View("main", estates, details);
        return new View("root", nav, main);
    }

    private static UIElement property(String name) {
        return new View(name, new LabelControl("key"),
                new LabelControl("value"));
    }
}
