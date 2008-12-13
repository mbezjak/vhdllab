package hr.fer.zemris.vhdllab.platform.manager.view;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentIdentifier;

public class ViewIdentifier extends ComponentIdentifier {

    public ViewIdentifier(Class<? extends View> componentClass) {
        super(componentClass);
    }

}
