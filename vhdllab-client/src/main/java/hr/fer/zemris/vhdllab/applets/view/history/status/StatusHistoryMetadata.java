package hr.fer.zemris.vhdllab.applets.view.history.status;

import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractViewMetadata;

public class StatusHistoryMetadata extends AbstractViewMetadata {

    public StatusHistoryMetadata() {
        super(StatusHistoryView.class);
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

}
