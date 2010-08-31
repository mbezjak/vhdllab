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

import hr.fer.zemris.vhdllab.entity.History;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.time.DateFormatUtils;

public class HistoryStub extends History {

    private static final long serialVersionUID = 1L;

    public static final Integer INSERT_VERSION = 147;
    public static final Integer INSERT_VERSION_2 = 369;
    public static final Integer UPDATE_VERSION = 741;
    public static final Integer UPDATE_VERSION_2 = 963;
    public static final Date CREATED_ON;
    public static final Date DELETED_ON;
    public static final Date DELETED_ON_BEFORE_CREATED_ON;
    public static final Date CREATED_ON_2;
    public static final Date DELETED_ON_2;

    static {
        SimpleDateFormat formatter = new SimpleDateFormat(
                DateFormatUtils.ISO_DATE_FORMAT.getPattern());
        try {
            CREATED_ON = formatter.parse("2008-01-05");
            DELETED_ON = formatter.parse("2008-03-12");
            DELETED_ON_BEFORE_CREATED_ON = formatter.parse("2007-08-21");
            CREATED_ON_2 = formatter.parse("2007-07-11");
            DELETED_ON_2 = formatter.parse("2007-11-18");
        } catch (ParseException e) {
            throw new UnhandledException(e);
        }
    }

    public HistoryStub() {
        setInsertVersion(INSERT_VERSION);
        setUpdateVersion(UPDATE_VERSION);
        setCreatedOn(CREATED_ON);
        setDeletedOn(DELETED_ON);
    }

}
