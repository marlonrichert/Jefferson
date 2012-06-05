package org.vaadin.jefferson.presentation;

import org.vaadin.jefferson.Composite;
import org.vaadin.jefferson.Presenter;
import org.vaadin.jefferson.View;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class SmartPresentation extends Presenter {
    private static final double GOLDEN_RATIO = 1.61803399;

    public enum Orientation {
        HORIZONTAL, VERTICAL,
    }

    private Orientation defaultOrientation;

    public SmartPresentation(Orientation orientation) {
        defaultOrientation = orientation;
    }

    protected void render(Composite<ComponentContainer> view) {
        Composite<?> parent = view.getParent();
        setRendition(view, getRendition(view, invert(getOrientation(parent))));
    }

    protected void style(Composite<?> view) {
        ComponentContainer rendition = getRendition(view);
        View<?>[] children = view.getChildren();
        if (rendition instanceof AbstractSplitPanel) {
            double weightA = 1;
            double weightB = 1;
            if (rendition instanceof HorizontalSplitPanel) {
                if (containsTable(children[0])) {
                    weightA = GOLDEN_RATIO;
                }
                if (containsTable(children[1])) {
                    weightB = GOLDEN_RATIO;
                }
            }
            ((AbstractSplitPanel) rendition).setSplitPosition(
                    (int) (100 * weightA / (weightA + weightB)));
        }
        if (!stretches(view)) {
            expand(getRendition(children[children.length - 1]));
        }
        style((View<?>) view);
    }

    private boolean containsTable(View<?> view) {
        if (!(view instanceof Composite<?>)) {
            return getRendition(view) instanceof Table;
        }
        Composite<?> composite = (Composite<?>) view;
        for (View<?> child : composite.getChildren()) {
            if (containsTable(child)) {
                return true;
            }
        }
        return false;
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
        } else {
            rendition.setSizeUndefined();
            Orientation parentOrientation = getOrientation(view.getParent());
            if (parentOrientation == Orientation.VERTICAL) {
                rendition.setSizeUndefined();
                if (parentRendition instanceof VerticalLayout) {
                    ((VerticalLayout) parentRendition).setComponentAlignment(
                            rendition, Alignment.TOP_LEFT);
                }
            } else if (parentOrientation == Orientation.HORIZONTAL) {
                if (!(rendition instanceof TextField || rendition instanceof PasswordField)) {
                    rendition.setHeight(100, Sizeable.UNITS_PERCENTAGE);
                }
                if (parentRendition instanceof HorizontalLayout) {
                    ((HorizontalLayout) parentRendition).setComponentAlignment(
                            rendition, Alignment.MIDDLE_LEFT);
                }
            }

        }
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
        if (orientation == Orientation.HORIZONTAL
                && !(getRendition(view.getParent()) instanceof AbstractSplitPanel)) {
            return new HorizontalLayout();
        }
        return new VerticalLayout();
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
        if (rendition == null) {
            call(MethodName.RENDER, view);
            rendition = getRendition(view);
        }
        return rendition instanceof Form
                || rendition instanceof ListSelect
                || rendition instanceof TwinColSelect
                || rendition instanceof Table
                || rendition instanceof Tree;
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
