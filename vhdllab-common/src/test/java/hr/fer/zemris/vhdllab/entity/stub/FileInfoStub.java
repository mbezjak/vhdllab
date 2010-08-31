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
package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.FileInfo;
import hr.fer.zemris.vhdllab.entity.FileType;

import org.apache.commons.beanutils.BeanUtils;

public class FileInfoStub extends FileInfo {

    private static final long serialVersionUID = 1L;

    public static final FileType TYPE = FileType.SOURCE;
    public static final String DATA = "a data of a file";

    public FileInfoStub() throws Exception {
        BeanUtils.copyProperties(this, new NamedEntityStub());
        setType(TYPE);
        setData(DATA);
    }

}
