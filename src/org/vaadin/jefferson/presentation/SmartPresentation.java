package org.vaadin.jefferson.presentation;

import org.vaadin.jefferson.Composite;
import org.vaadin.jefferson.Presentation;
import org.vaadin.jefferson.View;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class SmartPresentation extends Presentation {
    public enum Orientation {
        HORIZONTAL, VERTICAL,
    }

    private Orientation defaultOrientation;

    public SmartPresentation(Orientation orientation) {
        defaultOrientation = orientation;
    }

    @Override
    protected void style(View<?> view) {
        Component rendition = getRendition(view);
        Component parentRendition = getRendition(view).getParent();

        if (parentRendition instanceof AbstractSplitPanel) {
            rendition.setSizeFull();
        } else if (stretches(view)) {
            rendition.setSizeFull();
            expand(rendition);
        } else if (parentRendition instanceof HorizontalLayout) {
            // ((HorizontalLayout) parentRendition).setComponentAlignment(
            // rendition, Alignment.MIDDLE_LEFT);
        } else if (parentRendition instanceof VerticalLayout) {
            // ((VerticalLayout) parentRendition).setComponentAlignment(
            // rendition, Alignment.TOP_CENTER);
        }
    }

    void style(Composite<?> view) {
        ComponentContainer rendition = getRendition(view);
        if (rendition instanceof AbstractSplitPanel) {
            ((AbstractSplitPanel) rendition).setSplitPosition(50);
        }
        if (!stretches(view)) {
            View<?>[] children = view.getChildren();
            expand(getRendition(children[children.length - 1]));
        }
        style((View<?>) view);
    }

    void render(Composite<ComponentContainer> view) {
        Composite<?> parent = view.getParent();
        setRendition(view, getRendition(view, invert(getOrientation(parent))));
    }

    private Orientation invert(Orientation input) {
        Orientation output = defaultOrientation;
        if (input == Orientation.HORIZONTAL) {
            output = Orientation.VERTICAL;
        } else if (input == Orientation.VERTICAL) {
            output = Orientation.HORIZONTAL;
        }
        return output;
    }

    private ComponentContainer getRendition(
            Composite<ComponentContainer> view, Orientation orientation) {
        View<?>[] children = view.getChildren();
        if (children.length == 2) {
            if (stretches(children[0]) && stretches(children[1])) {
                if (orientation == Orientation.HORIZONTAL) {
                    return new HorizontalSplitPanel();
                } else if (orientation == Orientation.VERTICAL) {
                    return new VerticalSplitPanel();
                }
            }
        }
        if (orientation == Orientation.HORIZONTAL) {
            return new HorizontalLayout();
        } else if (orientation == Orientation.VERTICAL) {
            return new VerticalLayout();
        }
        throw new IllegalArgumentException("orientation cannot be null");
    }

    private boolean stretches(View<?> view) {
        if (view instanceof Composite<?>) {
            boolean stretches = false;
            for (View<?> child : ((Composite<?>) view).getChildren()) {
                stretches |= stretches(child);
            }
            return stretches;
        }
        Component rendition = getRendition(view);
        Class<?> cls = rendition == null
                ? view.getBase()
                : rendition.getClass();
        return AbstractSelect.class.isAssignableFrom(cls)
                || Form.class.isAssignableFrom(cls);
    }

    private Orientation getOrientation(View<?> view) {
        if (view != null) {
            Component rendition = getRendition(view);
            if (rendition instanceof VerticalLayout
                    || rendition instanceof VerticalSplitPanel) {
                return Orientation.VERTICAL;
            } else if (rendition instanceof HorizontalLayout
                    || rendition instanceof HorizontalSplitPanel) {
                return Orientation.HORIZONTAL;
            }
        }
        return null;
    }
}
