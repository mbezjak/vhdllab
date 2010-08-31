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
package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.ClientLogDao;
import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.service.ClientLogService;
import hr.fer.zemris.vhdllab.service.util.SecurityUtils;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientLogServiceImpl implements ClientLogService {

    @Autowired
    private ClientLogDao dao;

    @Override
    public void save(String data) {
        Validate.notNull(data, "Data can't be null");
        ClientLog log = new ClientLog(SecurityUtils.getUser(), data);
        dao.persist(log);
    }

}
