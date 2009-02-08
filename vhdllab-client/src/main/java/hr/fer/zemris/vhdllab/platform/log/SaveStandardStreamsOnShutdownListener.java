package hr.fer.zemris.vhdllab.platform.log;

import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownAdapter;
import hr.fer.zemris.vhdllab.service.LibraryFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveStandardStreamsOnShutdownListener extends ShutdownAdapter {

    @Autowired
    private LibraryFileService libraryFileService;

    @Override
    public void shutdownWithoutGUI() {
        String stdOut = StdOutConsumer.instance().toString();
        String stdErr = StdErrConsumer.instance().toString();
        StringBuilder sb = new StringBuilder(stdOut.length() + stdErr.length()
                + 200);
        String separator = "*******************************\n";
        sb.append("Standard output:\n").append(separator);
        sb.append(stdOut).append("\n").append(separator).append("\n\n");
        sb.append("Standard error:\n").append(separator);
        sb.append(stdErr).append("\n").append(separator);
        libraryFileService.saveClientLog(sb.toString());
    }

}
