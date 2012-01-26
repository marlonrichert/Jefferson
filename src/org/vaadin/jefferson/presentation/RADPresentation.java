package org.vaadin.jefferson.presentation;

import org.vaadin.jefferson.Composite;
import org.vaadin.jefferson.Presentation;
import org.vaadin.jefferson.View;

import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class RADPresentation extends Presentation {
    public enum Orientation {
        HORIZONTAL, VERTICAL,
    }

    private Orientation orientation;

    public RADPresentation(Orientation orientation) {
        this.orientation = orientation;
    }

    @Override
    protected void style(View<?> view) {
        Component rendition = getRendition(view);
        Component parentRendition = getRendition(view).getParent();
        if (parentRendition instanceof AbstractSplitPanel) {
            rendition.setSizeFull();
        }
    }

    void render(Composite<ComponentContainer> view) {
        setRendition(view,
                isVertical(view.getParent())
                        ? getHorizontal(view)
                        : getVertical(view));
    }

    void style(Composite<?> view) {
        ComponentContainer rendition = getRendition(view);
        if (rendition instanceof AbstractSplitPanel) {
            ((AbstractSplitPanel) rendition).setSplitPosition(50);
        } else {
            View<?>[] children = view.getChildren();
            expand(getRendition(children[children.length - 1]));
        }
        rendition.setSizeFull();
        expand(rendition);
    }

    private ComponentContainer getVertical(Composite<ComponentContainer> view) {
        return view.getChildren().length == 2
                ? new VerticalSplitPanel()
                : new VerticalLayout();
    }

    private ComponentContainer getHorizontal(Composite<ComponentContainer> view) {
        return view.getChildren().length == 2
                ? new HorizontalSplitPanel()
                : new HorizontalLayout();
    }

    private boolean isVertical(Composite<?> view) {
        if (view == null) {
            return orientation == Orientation.HORIZONTAL;
        }
        ComponentContainer rendition = getRendition(view);
        return rendition instanceof VerticalLayout
                || rendition instanceof VerticalSplitPanel;
    }
}
