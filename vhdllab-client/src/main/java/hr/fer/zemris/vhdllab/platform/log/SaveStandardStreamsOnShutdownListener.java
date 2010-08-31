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
package hr.fer.zemris.vhdllab.platform.log;

import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownAdapter;
import hr.fer.zemris.vhdllab.service.ClientLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveStandardStreamsOnShutdownListener extends ShutdownAdapter {

    @Autowired
    private ClientLogService clientLogService;

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
        clientLogService.save(sb.toString());
    }

}
