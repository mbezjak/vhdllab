/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.platform.support;

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
        String env = cmd.getOptionValue(ENV_OPTION, ENV_DEV);
        if (env.equals(ENV_DEV)) {
            Environment.setCurrentEnvironment(Environment.DEVELOPMENT);
        } else if (env.equals(ENV_PROD)) {
            Environment.setCurrentEnvironment(Environment.PRODUCTION);
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
        options.addOption(OptionBuilder.withLongOpt(ENV_OPTION)
                                        .hasArg()
                                        .withValueSeparator()
                                        .create());
        return options;
    }

}
