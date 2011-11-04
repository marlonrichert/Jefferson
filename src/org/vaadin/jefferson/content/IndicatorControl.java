package org.vaadin.jefferson.content;

import com.vaadin.ui.ProgressIndicator;

public class IndicatorControl extends Control<ProgressIndicator> {

    public IndicatorControl(String name) {
        super(name);
    }

    @Override
    public Class<? extends ProgressIndicator> getDefaultRenditionClass() {
        return ProgressIndicator.class;
    }

    @Override
    public Class<ProgressIndicator> getRenditionInterface() {
        return ProgressIndicator.class;
    }
}
