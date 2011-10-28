package org.vaadin.jefferson.content;

import com.vaadin.ui.Component;
import com.vaadin.ui.ProgressIndicator;

public class IndicatorControl extends Control {

    public IndicatorControl(String name) {
        super(name);
    }

    @Override
    public Class<? extends Component> getDefaultRenditionClass() {
        return ProgressIndicator.class;
    }

}
