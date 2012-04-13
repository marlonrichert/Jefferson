package org.vaadin.jefferson;

import java.lang.reflect.*;

import com.vaadin.ui.*;
import com.vaadin.ui.Field;

/**
 * A {@link View} that can register a listener object to its rendition.
 * 
 * @author Marlon Richert @ Vaadin
 * @param <L>
 *            The interface of this control's listener.
 */
public abstract class Control<T extends Component, L> extends View<T> {
    private L listener;
    private Method removeListener;
    private Method addListener;

    public Control(String name, Class<T> base, Class<L> listenerBase) {
        super(name, base);
        try {
            removeListener = base.getMethod("removeListener", listenerBase);
            addListener = base.getMethod("addListener", listenerBase);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Control(String name, Class<T> base, Class<L> listenerBase, L listener) {
        this(name, base, listenerBase);
        setListener(listener);
    }

    public void setListener(L listener) {
        T rendition = getRendition();
        removeListener(rendition, this.listener);
        addListener(rendition, listener);
        this.listener = listener;
    }

    public L getListener() {
        return listener;
    }

    @Override
    protected T accept(Presentation presentation) {
        T rendition = super.accept(presentation);
        setListener(listener);
        return rendition;
    }

    @Override
    protected boolean setRendition(T rendition) {
        removeListener(getRendition(), listener);
        return super.setRendition(rendition);
    }

    private void removeListener(T rendition, L handler) {
        try {
            if (rendition != null && handler != null) {
                removeListener.invoke(rendition, handler);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void addListener(T rendition, L handler) {
        try {
            if (rendition != null) {
                if (rendition instanceof AbstractComponent) {
                    ((AbstractComponent) rendition).setImmediate(true);
                }
                if (rendition instanceof Field) {
                    ((Field<?>) rendition).setBuffered(false);
                }
                if (handler != null) {
                    addListener.invoke(rendition, handler);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
