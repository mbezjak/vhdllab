package hr.fer.zemris.vhdllab.platform.support;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContext;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.context.Environment;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;

public final class CommandLineArgumentProcessor {

    private static final String ENV_OPTION = "env";
    private static final String ENV_DEV = "dev";
    private static final String ENV_PROD = "prod";

    public void processCommandLine(String[] args) {
        CommandLine cmd;
        try {
            cmd = createParser().parse(createOptions(), args);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
        ApplicationContext context = ApplicationContextHolder.getContext();
        String env = cmd.getOptionValue(ENV_OPTION, ENV_DEV);
        if (env.equals(ENV_DEV)) {
            context.setEnvironment(Environment.DEVELOPMENT);
        } else if (env.equals(ENV_PROD)) {
            context.setEnvironment(Environment.PRODUCTION);
        } else {
            throw new IllegalStateException("Illegal environment is set. Only "
                    + ENV_DEV + " and " + ENV_PROD + " are permitted!");
        }
    }

    private Parser createParser() {
        return new PosixParser();
    }

    @SuppressWarnings("static-access")
    private Options createOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt(ENV_OPTION).hasArg()
                .withValueSeparator().create());
        return options;
    }

}
