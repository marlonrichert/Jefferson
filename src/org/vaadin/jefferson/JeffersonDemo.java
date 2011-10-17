package org.vaadin.jefferson;

import org.vaadin.jefferson.content.Bar;
import org.vaadin.jefferson.content.ButtonControl;
import org.vaadin.jefferson.content.SelectionControl;
import org.vaadin.jefferson.content.Text;
import org.vaadin.jefferson.content.View;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class JeffersonDemo extends Application {
    @Override
    public void init() {
        Window mainWindow = new Window("Jefferson Demo");
        Bar navbar = new Bar("nav-bar", new Text("title"),
                new SelectionControl("tabs"), new ButtonControl("add-new"),
                new ButtonControl("user"), new ButtonControl("sign-out"));
        View main = new View("main");
        View content = new View("root", navbar, main);

        mainWindow.addComponent(new Presentation(content));
        setMainWindow(mainWindow);
    }
}
