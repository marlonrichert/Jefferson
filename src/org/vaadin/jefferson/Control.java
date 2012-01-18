package org.vaadin.jefferson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

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
        if (rendition != null) {
            if (this.listener != null) {
                removeListener(rendition, this.listener);
            }
            addListener(rendition, this.listener);
        }
        this.listener = listener;
    }

    public L getListener() {
        return listener;
    }

    @Override
    protected T accept(Presentation presentation) {
        T rendition = super.accept(presentation);
        if (listener != null) {
            addListener(rendition, listener);
        }
        return rendition;
    }

    @Override
    protected boolean setRendition(T rendition) {
        T oldRendition = getRendition();
        if (oldRendition != null) {
            removeListener(oldRendition, listener);
        }
        return super.setRendition(rendition);
    }

    private void removeListener(T rendition, L l) {
        try {
            removeListener.invoke(rendition, l);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void addListener(T rendition, L l) {
        try {
            if (rendition instanceof AbstractComponent) {
                ((AbstractComponent) rendition).setImmediate(true);
            }
            addListener.invoke(rendition, l);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
