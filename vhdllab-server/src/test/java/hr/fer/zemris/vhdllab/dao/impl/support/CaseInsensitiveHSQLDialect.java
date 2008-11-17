package hr.fer.zemris.vhdllab.dao.impl.support;

import java.sql.Types;

import org.hibernate.dialect.HSQLDialect;

/**
 * HSQL DB dialect whose string columns are case insensitive.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class CaseInsensitiveHSQLDialect extends HSQLDialect {

    public CaseInsensitiveHSQLDialect() {
        super();
        registerColumnType(Types.VARCHAR, "varchar_ignorecase($l)");
    }

}
