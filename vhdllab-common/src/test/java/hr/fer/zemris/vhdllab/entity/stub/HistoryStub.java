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
    public static final Integer INSERT_VERSION_DIFFERENT = 369;
    public static final Integer UPDATE_VERSION = 741;
    public static final Integer UPDATE_VERSION_DIFFERENT = 963;
    public static final Date CREATED_ON;
    public static final Date DELETED_ON;
    public static final Date DELETED_ON_BEFORE_CREATED_ON;
    public static final Date CREATED_ON_DIFFERENT;
    public static final Date DELETED_ON_DIFFERENT;

    static {
        SimpleDateFormat formatter = new SimpleDateFormat(
                DateFormatUtils.ISO_DATE_FORMAT.getPattern());
        try {
            CREATED_ON = formatter.parse("2008-01-05");
            DELETED_ON = formatter.parse("2008-03-12");
            DELETED_ON_BEFORE_CREATED_ON = formatter.parse("2007-08-21");
            CREATED_ON_DIFFERENT = formatter.parse("2007-07-11");
            DELETED_ON_DIFFERENT = formatter.parse("2007-11-18");
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
