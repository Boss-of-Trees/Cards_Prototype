package ExternalLibraries.org.sqlite.jdbc3;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ExternalLibraries.org.sqlite.core.CoreResultSet;
import ExternalLibraries.org.sqlite.core.CoreStatement;
import ExternalLibraries.org.sqlite.core.DB;
import ExternalLibraries.org.sqlite.date.FastDateFormat;

public abstract class JDBC3ResultSet extends CoreResultSet {
    // ResultSet Functions //////////////////////////////////////////

    protected JDBC3ResultSet(CoreStatement stmt) {
        super(stmt);
    }

    /**
     * returns col in [1,x] form
     *
     * @see ResultSet#findColumn(String)
     */
    public int findColumn(String col) throws SQLException {
        checkOpen();
        Integer index = findColumnIndexInCache(col);
        if (index != null) {
            return index;
        }
        for (int i = 0; i < cols.length; i++) {
            if (col.equalsIgnoreCase(cols[i])) {
                return addColumnIndexInCache(col, i + 1);
            }
        }
        throw new SQLException("no such column: '" + col + "'");
    }

    /** @see ResultSet#next() */
    public boolean next() throws SQLException {
        if (!open) {
            return false; // finished ResultSet
        }
        lastCol = -1;

        // first row is loaded by execute(), so do not step() again
        if (row == 0) {
            row++;
            return true;
        }

        // check if we are row limited by the statement or the ResultSet
        if (maxRows != 0 && row == maxRows) {
            return false;
        }

        // do the real work
        int statusCode = getDatabase().step(stmt.pointer);
        switch (statusCode) {
            case SQLITE_DONE:
                close(); // agressive closing to avoid writer starvation
                return false;
            case SQLITE_ROW:
                row++;
                return true;
            case SQLITE_BUSY:
            default:
                getDatabase().throwex(statusCode);
                return false;
        }
    }

    /** @see ResultSet#getType() */
    public int getType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    /** @see ResultSet#getFetchSize() */
    public int getFetchSize() throws SQLException {
        return limitRows;
    }

    /** @see ResultSet#setFetchSize(int) */
    public void setFetchSize(int rows) throws SQLException {
        if (0 > rows || (maxRows != 0 && rows > maxRows)) {
            throw new SQLException("fetch size " + rows + " out of bounds " + maxRows);
        }
        limitRows = rows;
    }

    /** @see ResultSet#getFetchDirection() */
    public int getFetchDirection() throws SQLException {
        checkOpen();
        return ResultSet.FETCH_FORWARD;
    }

    /** @see ResultSet#setFetchDirection(int) */
    public void setFetchDirection(int d) throws SQLException {
        checkOpen();
        // Only FORWARD_ONLY ResultSets exist in SQLite, so only FETCH_FORWARD is permitted
        if (
        /*getType() == ResultSet.TYPE_FORWARD_ONLY &&*/
        d != ResultSet.FETCH_FORWARD) {
            throw new SQLException("only FETCH_FORWARD direction supported");
        }
    }

    /** @see ResultSet#isAfterLast() */
    public boolean isAfterLast() throws SQLException {
        return !open;
    }

    /** @see ResultSet#isBeforeFirst() */
    public boolean isBeforeFirst() throws SQLException {
        return open && row == 0;
    }

    /** @see ResultSet#isFirst() */
    public boolean isFirst() throws SQLException {
        return row == 1;
    }

    /** @see ResultSet#isLast() */
    public boolean isLast() throws SQLException { // FIXME
        throw new SQLException("function not yet implemented for SQLite");
    }

    /** @see ResultSet#getRow() */
    public int getRow() throws SQLException {
        return row;
    }

    /** @see ResultSet#wasNull() */
    public boolean wasNull() throws SQLException {
        return getDatabase().column_type(stmt.pointer, markCol(lastCol)) == SQLITE_NULL;
    }

    // DATA ACCESS FUNCTIONS ////////////////////////////////////////

    /** @see ResultSet#getBigDecimal(int) */
    public BigDecimal getBigDecimal(int col) throws SQLException {
        final int columnType = getColumnType(col);

        if (columnType == Types.INTEGER) {
            long decimal = getLong(col);
            return BigDecimal.valueOf(decimal);
        } else if (columnType == Types.FLOAT || columnType == Types.DOUBLE) {
            final double decimal = getDouble(col);
            if (Double.isNaN(decimal)) {
                throw new SQLException("Bad value for type BigDecimal : Not a Number");
            } else {
                return BigDecimal.valueOf(decimal);
            }
        } else {
            final String stringValue = getString(col);
            if (stringValue == null) {
                return null;
            } else {
                try {
                    return new BigDecimal(stringValue);
                } catch (NumberFormatException e) {
                    throw new SQLException("Bad value for type BigDecimal : " + stringValue);
                }
            }
        }
    }

    /** @see ResultSet#getBigDecimal(String) */
    public BigDecimal getBigDecimal(String col) throws SQLException {
        return getBigDecimal(findColumn(col));
    }

    /** @see ResultSet#getBoolean(int) */
    public boolean getBoolean(int col) throws SQLException {
        return getInt(col) == 0 ? false : true;
    }

    /** @see ResultSet#getBoolean(String) */
    public boolean getBoolean(String col) throws SQLException {
        return getBoolean(findColumn(col));
    }

    /** @see ResultSet#getBinaryStream(int) */
    public InputStream getBinaryStream(int col) throws SQLException {
        byte[] bytes = getBytes(col);
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        } else {
            return null;
        }
    }

    /** @see ResultSet#getBinaryStream(String) */
    public InputStream getBinaryStream(String col) throws SQLException {
        return getBinaryStream(findColumn(col));
    }

    /** @see ResultSet#getByte(int) */
    public byte getByte(int col) throws SQLException {
        return (byte) getInt(col);
    }

    /** @see ResultSet#getByte(String) */
    public byte getByte(String col) throws SQLException {
        return getByte(findColumn(col));
    }

    /** @see ResultSet#getBytes(int) */
    public byte[] getBytes(int col) throws SQLException {
        return getDatabase().column_blob(stmt.pointer, markCol(col));
    }

    /** @see ResultSet#getBytes(String) */
    public byte[] getBytes(String col) throws SQLException {
        return getBytes(findColumn(col));
    }

    /** @see ResultSet#getCharacterStream(int) */
    public Reader getCharacterStream(int col) throws SQLException {
        String string = getString(col);
        return string == null ? null : new StringReader(string);
    }

    /** @see ResultSet#getCharacterStream(String) */
    public Reader getCharacterStream(String col) throws SQLException {
        return getCharacterStream(findColumn(col));
    }

    /** @see ResultSet#getDate(int) */
    public Date getDate(int col) throws SQLException {
        DB db = getDatabase();
        switch (db.column_type(stmt.pointer, markCol(col))) {
            case SQLITE_NULL:
                return null;

            case SQLITE_TEXT:
                try {
                    return new Date(
                            getConnectionConfig()
                                    .getDateFormat()
                                    .parse(db.column_text(stmt.pointer, markCol(col)))
                                    .getTime());
                } catch (Exception e) {
                    SQLException error = new SQLException("Error parsing date");
                    error.initCause(e);

                    throw error;
                }

            case SQLITE_FLOAT:
                return new Date(
                        julianDateToCalendar(db.column_double(stmt.pointer, markCol(col)))
                                .getTimeInMillis());

            default: // SQLITE_INTEGER:
                return new Date(
                        db.column_long(stmt.pointer, markCol(col))
                                * getConnectionConfig().getDateMultiplier());
        }
    }

    /** @see ResultSet#getDate(int, Calendar) */
    public Date getDate(int col, Calendar cal) throws SQLException {
        checkCalendar(cal);

        DB db = getDatabase();
        switch (db.column_type(stmt.pointer, markCol(col))) {
            case SQLITE_NULL:
                return null;

            case SQLITE_TEXT:
                try {
                    FastDateFormat dateFormat =
                            FastDateFormat.getInstance(
                                    getConnectionConfig().getDateStringFormat(), cal.getTimeZone());

                    return new Date(
                            dateFormat.parse(db.column_text(stmt.pointer, markCol(col))).getTime());
                } catch (Exception e) {
                    SQLException error = new SQLException("Error parsing time stamp");
                    error.initCause(e);

                    throw error;
                }

            case SQLITE_FLOAT:
                return new Date(
                        julianDateToCalendar(db.column_double(stmt.pointer, markCol(col)), cal)
                                .getTimeInMillis());

            default: // SQLITE_INTEGER:
                cal.setTimeInMillis(
                        db.column_long(stmt.pointer, markCol(col))
                                * getConnectionConfig().getDateMultiplier());
                return new Date(cal.getTime().getTime());
        }
    }

    /** @see ResultSet#getDate(String) */
    public Date getDate(String col) throws SQLException {
        return getDate(findColumn(col), Calendar.getInstance());
    }

    /** @see ResultSet#getDate(String, Calendar) */
    public Date getDate(String col, Calendar cal) throws SQLException {
        return getDate(findColumn(col), cal);
    }

    /** @see ResultSet#getDouble(int) */
    public double getDouble(int col) throws SQLException {
        DB db = getDatabase();
        if (db.column_type(stmt.pointer, markCol(col)) == SQLITE_NULL) {
            return 0;
        }
        return db.column_double(stmt.pointer, markCol(col));
    }

    /** @see ResultSet#getDouble(String) */
    public double getDouble(String col) throws SQLException {
        return getDouble(findColumn(col));
    }

    /** @see ResultSet#getFloat(int) */
    public float getFloat(int col) throws SQLException {
        DB db = getDatabase();
        if (db.column_type(stmt.pointer, markCol(col)) == SQLITE_NULL) {
            return 0;
        }
        return (float) db.column_double(stmt.pointer, markCol(col));
    }

    /** @see ResultSet#getFloat(String) */
    public float getFloat(String col) throws SQLException {
        return getFloat(findColumn(col));
    }

    /** @see ResultSet#getInt(int) */
    public int getInt(int col) throws SQLException {
        DB db = getDatabase();
        return db.column_int(stmt.pointer, markCol(col));
    }

    /** @see ResultSet#getInt(String) */
    public int getInt(String col) throws SQLException {
        return getInt(findColumn(col));
    }

    /** @see ResultSet#getLong(int) */
    public long getLong(int col) throws SQLException {
        DB db = getDatabase();
        return db.column_long(stmt.pointer, markCol(col));
    }

    /** @see ResultSet#getLong(String) */
    public long getLong(String col) throws SQLException {
        return getLong(findColumn(col));
    }

    /** @see ResultSet#getShort(int) */
    public short getShort(int col) throws SQLException {
        return (short) getInt(col);
    }

    /** @see ResultSet#getShort(String) */
    public short getShort(String col) throws SQLException {
        return getShort(findColumn(col));
    }

    /** @see ResultSet#getString(int) */
    public String getString(int col) throws SQLException {
        DB db = getDatabase();
        return db.column_text(stmt.pointer, markCol(col));
    }

    /** @see ResultSet#getString(String) */
    public String getString(String col) throws SQLException {
        return getString(findColumn(col));
    }

    /** @see ResultSet#getTime(int) */
    public Time getTime(int col) throws SQLException {
        DB db = getDatabase();
        switch (db.column_type(stmt.pointer, markCol(col))) {
            case SQLITE_NULL:
                return null;

            case SQLITE_TEXT:
                try {
                    return new Time(
                            getConnectionConfig()
                                    .getDateFormat()
                                    .parse(db.column_text(stmt.pointer, markCol(col)))
                                    .getTime());
                } catch (Exception e) {
                    SQLException error = new SQLException("Error parsing time");
                    error.initCause(e);

                    throw error;
                }

            case SQLITE_FLOAT:
                return new Time(
                        julianDateToCalendar(db.column_double(stmt.pointer, markCol(col)))
                                .getTimeInMillis());

            default: // SQLITE_INTEGER
                return new Time(
                        db.column_long(stmt.pointer, markCol(col))
                                * getConnectionConfig().getDateMultiplier());
        }
    }

    /** @see ResultSet#getTime(int, Calendar) */
    public Time getTime(int col, Calendar cal) throws SQLException {
        checkCalendar(cal);
        DB db = getDatabase();
        switch (db.column_type(stmt.pointer, markCol(col))) {
            case SQLITE_NULL:
                return null;

            case SQLITE_TEXT:
                try {
                    FastDateFormat dateFormat =
                            FastDateFormat.getInstance(
                                    getConnectionConfig().getDateStringFormat(), cal.getTimeZone());

                    return new Time(
                            dateFormat.parse(db.column_text(stmt.pointer, markCol(col))).getTime());
                } catch (Exception e) {
                    SQLException error = new SQLException("Error parsing time");
                    error.initCause(e);

                    throw error;
                }

            case SQLITE_FLOAT:
                return new Time(
                        julianDateToCalendar(db.column_double(stmt.pointer, markCol(col)), cal)
                                .getTimeInMillis());

            default: // SQLITE_INTEGER
                cal.setTimeInMillis(
                        db.column_long(stmt.pointer, markCol(col))
                                * getConnectionConfig().getDateMultiplier());
                return new Time(cal.getTime().getTime());
        }
    }

    /** @see ResultSet#getTime(String) */
    public Time getTime(String col) throws SQLException {
        return getTime(findColumn(col));
    }

    /** @see ResultSet#getTime(String, Calendar) */
    public Time getTime(String col, Calendar cal) throws SQLException {
        return getTime(findColumn(col), cal);
    }

    /** @see ResultSet#getTimestamp(int) */
    public Timestamp getTimestamp(int col) throws SQLException {
        DB db = getDatabase();
        switch (db.column_type(stmt.pointer, markCol(col))) {
            case SQLITE_NULL:
                return null;

            case SQLITE_TEXT:
                try {
                    return new Timestamp(
                            getConnectionConfig()
                                    .getDateFormat()
                                    .parse(db.column_text(stmt.pointer, markCol(col)))
                                    .getTime());
                } catch (Exception e) {
                    SQLException error = new SQLException("Error parsing time stamp");
                    error.initCause(e);

                    throw error;
                }

            case SQLITE_FLOAT:
                return new Timestamp(
                        julianDateToCalendar(db.column_double(stmt.pointer, markCol(col)))
                                .getTimeInMillis());

            default: // SQLITE_INTEGER:
                return new Timestamp(
                        db.column_long(stmt.pointer, markCol(col))
                                * getConnectionConfig().getDateMultiplier());
        }
    }

    /** @see ResultSet#getTimestamp(int, Calendar) */
    public Timestamp getTimestamp(int col, Calendar cal) throws SQLException {
        if (cal == null) {
            return getTimestamp(col);
        }

        DB db = getDatabase();
        switch (db.column_type(stmt.pointer, markCol(col))) {
            case SQLITE_NULL:
                return null;

            case SQLITE_TEXT:
                try {
                    FastDateFormat dateFormat =
                            FastDateFormat.getInstance(
                                    getConnectionConfig().getDateStringFormat(), cal.getTimeZone());

                    return new Timestamp(
                            dateFormat.parse(db.column_text(stmt.pointer, markCol(col))).getTime());
                } catch (Exception e) {
                    SQLException error = new SQLException("Error parsing time stamp");
                    error.initCause(e);

                    throw error;
                }

            case SQLITE_FLOAT:
                return new Timestamp(
                        julianDateToCalendar(db.column_double(stmt.pointer, markCol(col)), cal)
                                .getTimeInMillis());

            default: // SQLITE_INTEGER
                cal.setTimeInMillis(
                        db.column_long(stmt.pointer, markCol(col))
                                * getConnectionConfig().getDateMultiplier());

                return new Timestamp(cal.getTime().getTime());
        }
    }

    /** @see ResultSet#getTimestamp(String) */
    public Timestamp getTimestamp(String col) throws SQLException {
        return getTimestamp(findColumn(col));
    }

    /** @see ResultSet#getTimestamp(String, Calendar) */
    public Timestamp getTimestamp(String c, Calendar ca) throws SQLException {
        return getTimestamp(findColumn(c), ca);
    }

    /** @see ResultSet#getObject(int) */
    public Object getObject(int col) throws SQLException {
        switch (getDatabase().column_type(stmt.pointer, markCol(col))) {
            case SQLITE_INTEGER:
                long val = getLong(col);
                if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE) {
                    return new Long(val);
                } else {
                    return new Integer((int) val);
                }
            case SQLITE_FLOAT:
                return new Double(getDouble(col));
            case SQLITE_BLOB:
                return getBytes(col);
            case SQLITE_NULL:
                return null;
            case SQLITE_TEXT:
            default:
                return getString(col);
        }
    }

    /** @see ResultSet#getObject(String) */
    public Object getObject(String col) throws SQLException {
        return getObject(findColumn(col));
    }

    /** @see ResultSet#getStatement() */
    public Statement getStatement() {
        return (Statement) stmt;
    }

    /** @see ResultSet#getCursorName() */
    public String getCursorName() throws SQLException {
        return null;
    }

    /** @see ResultSet#getWarnings() */
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    /** @see ResultSet#clearWarnings() */
    public void clearWarnings() throws SQLException {}

    // ResultSetMetaData Functions //////////////////////////////////

    /** Pattern used to extract the column type name from table column definition. */
    protected static final Pattern COLUMN_TYPENAME = Pattern.compile("([^\\(]*)");

    /** Pattern used to extract the column type name from a cast(col as type) */
    protected static final Pattern COLUMN_TYPECAST =
            Pattern.compile("cast\\(.*?\\s+as\\s+(.*?)\\s*\\)");

    /**
     * Pattern used to extract the precision and scale from column meta returned by the JDBC driver.
     */
    protected static final Pattern COLUMN_PRECISION = Pattern.compile(".*?\\((.*?)\\)");

    // we do not need to check the RS is open, only that colsMeta
    // is not null, done with checkCol(int).

    /** @see ResultSet#getMetaData() */
    public ResultSetMetaData getMetaData() throws SQLException {
        return (ResultSetMetaData) this;
    }

    /** @see ResultSetMetaData#getCatalogName(int) */
    public String getCatalogName(int col) throws SQLException {
        return getDatabase().column_table_name(stmt.pointer, checkCol(col));
    }

    /** @see ResultSetMetaData#getColumnClassName(int) */
    public String getColumnClassName(int col) throws SQLException {
        checkCol(col);
        return "java.lang.Object";
    }

    /** @see ResultSetMetaData#getColumnCount() */
    public int getColumnCount() throws SQLException {
        checkCol(1);
        return colsMeta.length;
    }

    /** @see ResultSetMetaData#getColumnDisplaySize(int) */
    public int getColumnDisplaySize(int col) throws SQLException {
        return Integer.MAX_VALUE;
    }

    /** @see ResultSetMetaData#getColumnLabel(int) */
    public String getColumnLabel(int col) throws SQLException {
        return getColumnName(col);
    }

    /** @see ResultSetMetaData#getColumnName(int) */
    public String getColumnName(int col) throws SQLException {
        return getDatabase().column_name(stmt.pointer, checkCol(col));
    }

    /** @see ResultSetMetaData#getColumnType(int) */
    public int getColumnType(int col) throws SQLException {
        String typeName = getColumnTypeName(col);
        int valueType = getDatabase().column_type(stmt.pointer, checkCol(col));

        if (valueType == SQLITE_INTEGER || valueType == SQLITE_NULL) {
            if ("BOOLEAN".equals(typeName)) {
                return Types.BOOLEAN;
            }

            if ("TINYINT".equals(typeName)) {
                return Types.TINYINT;
            }

            if ("SMALLINT".equals(typeName) || "INT2".equals(typeName)) {
                return Types.SMALLINT;
            }

            if ("BIGINT".equals(typeName)
                    || "INT8".equals(typeName)
                    || "UNSIGNED BIG INT".equals(typeName)) {
                return Types.BIGINT;
            }

            if ("DATE".equals(typeName) || "DATETIME".equals(typeName)) {
                return Types.DATE;
            }

            if ("TIMESTAMP".equals(typeName)) {
                return Types.TIMESTAMP;
            }

            if (valueType == SQLITE_INTEGER
                    || "INT".equals(typeName)
                    || "INTEGER".equals(typeName)
                    || "MEDIUMINT".equals(typeName)) {
                return Types.INTEGER;
            }
        }

        if (valueType == SQLITE_FLOAT || valueType == SQLITE_NULL) {
            if ("DECIMAL".equals(typeName)) {
                return Types.DECIMAL;
            }

            if ("DOUBLE".equals(typeName) || "DOUBLE PRECISION".equals(typeName)) {
                return Types.DOUBLE;
            }

            if ("NUMERIC".equals(typeName)) {
                return Types.NUMERIC;
            }

            if ("REAL".equals(typeName)) {
                return Types.REAL;
            }

            if (valueType == SQLITE_FLOAT || "FLOAT".equals(typeName)) {
                return Types.FLOAT;
            }
        }

        if (valueType == SQLITE_TEXT || valueType == SQLITE_NULL) {
            if ("CHARACTER".equals(typeName)
                    || "NCHAR".equals(typeName)
                    || "NATIVE CHARACTER".equals(typeName)
                    || "CHAR".equals(typeName)) {
                return Types.CHAR;
            }

            if ("CLOB".equals(typeName)) {
                return Types.CLOB;
            }

            if ("DATE".equals(typeName) || "DATETIME".equals(typeName)) {
                return Types.DATE;
            }

            if (valueType == SQLITE_TEXT
                    || "VARCHAR".equals(typeName)
                    || "VARYING CHARACTER".equals(typeName)
                    || "NVARCHAR".equals(typeName)
                    || "TEXT".equals(typeName)) {
                return Types.VARCHAR;
            }
        }

        if (valueType == SQLITE_BLOB || valueType == SQLITE_NULL) {
            if ("BINARY".equals(typeName)) {
                return Types.BINARY;
            }

            if (valueType == SQLITE_BLOB || "BLOB".equals(typeName)) {
                return Types.BLOB;
            }
        }

        return Types.NUMERIC;
    }

    /**
     * @return The data type from either the 'create table' statement, or CAST(expr AS TYPE)
     *     otherwise sqlite3_value_type.
     * @see ResultSetMetaData#getColumnTypeName(int)
     */
    public String getColumnTypeName(int col) throws SQLException {
        String declType = getColumnDeclType(col);

        if (declType != null) {
            Matcher matcher = COLUMN_TYPENAME.matcher(declType);

            matcher.find();
            return matcher.group(1).toUpperCase(Locale.ENGLISH);
        }

        switch (getDatabase().column_type(stmt.pointer, checkCol(col))) {
            case SQLITE_INTEGER:
                return "INTEGER";
            case SQLITE_FLOAT:
                return "FLOAT";
            case SQLITE_BLOB:
                return "BLOB";
            case SQLITE_NULL:
                return "NUMERIC";
            case SQLITE_TEXT:
                return "TEXT";
            default:
                return "NUMERIC";
        }
    }

    /** @see ResultSetMetaData#getPrecision(int) */
    public int getPrecision(int col) throws SQLException {
        String declType = getColumnDeclType(col);

        if (declType != null) {
            Matcher matcher = COLUMN_PRECISION.matcher(declType);

            return matcher.find() ? Integer.parseInt(matcher.group(1).split(",")[0].trim()) : 0;
        }

        return 0;
    }

    private String getColumnDeclType(int col) throws SQLException {
        DB db = getDatabase();
        String declType = db.column_decltype(stmt.pointer, checkCol(col));

        if (declType == null) {
            Matcher matcher = COLUMN_TYPECAST.matcher(db.column_name(stmt.pointer, checkCol(col)));
            declType = matcher.find() ? matcher.group(1) : null;
        }

        return declType;
    }
    /** @see ResultSetMetaData#getScale(int) */
    public int getScale(int col) throws SQLException {
        String declType = getColumnDeclType(col);

        if (declType != null) {
            Matcher matcher = COLUMN_PRECISION.matcher(declType);

            if (matcher.find()) {
                String array[] = matcher.group(1).split(",");

                if (array.length == 2) {
                    return Integer.parseInt(array[1].trim());
                }
            }
        }

        return 0;
    }

    /** @see ResultSetMetaData#getSchemaName(int) */
    public String getSchemaName(int col) throws SQLException {
        return "";
    }

    /** @see ResultSetMetaData#getTableName(int) */
    public String getTableName(int col) throws SQLException {
        final String tableName = getDatabase().column_table_name(stmt.pointer, checkCol(col));
        if (tableName == null) {
            // JDBC specifies an empty string instead of null
            return "";
        }
        return tableName;
    }

    /** @see ResultSetMetaData#isNullable(int) */
    public int isNullable(int col) throws SQLException {
        checkMeta();
        return meta[checkCol(col)][1]
                ? ResultSetMetaData.columnNoNulls
                : ResultSetMetaData.columnNullable;
    }

    /** @see ResultSetMetaData#isAutoIncrement(int) */
    public boolean isAutoIncrement(int col) throws SQLException {
        checkMeta();
        return meta[checkCol(col)][2];
    }

    /** @see ResultSetMetaData#isCaseSensitive(int) */
    public boolean isCaseSensitive(int col) throws SQLException {
        return true;
    }

    /** @see ResultSetMetaData#isCurrency(int) */
    public boolean isCurrency(int col) throws SQLException {
        return false;
    }

    /** @see ResultSetMetaData#isDefinitelyWritable(int) */
    public boolean isDefinitelyWritable(int col) throws SQLException {
        return true;
    } // FIXME: check db file constraints?

    /** @see ResultSetMetaData#isReadOnly(int) */
    public boolean isReadOnly(int col) throws SQLException {
        return false;
    }

    /** @see ResultSetMetaData#isSearchable(int) */
    public boolean isSearchable(int col) throws SQLException {
        return true;
    }

    /** @see ResultSetMetaData#isSigned(int) */
    public boolean isSigned(int col) throws SQLException {
        String typeName = getColumnTypeName(col);

        return "NUMERIC".equals(typeName) || "INTEGER".equals(typeName) || "REAL".equals(typeName);
    }

    /** @see ResultSetMetaData#isWritable(int) */
    public boolean isWritable(int col) throws SQLException {
        return true;
    }

    /** @see ResultSet#getConcurrency() */
    public int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    /** @see ResultSet#rowDeleted() */
    public boolean rowDeleted() throws SQLException {
        return false;
    }

    /** @see ResultSet#rowInserted() */
    public boolean rowInserted() throws SQLException {
        return false;
    }

    /** @see ResultSet#rowUpdated() */
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    /** Transforms a Julian Date to java.util.Calendar object. */
    private Calendar julianDateToCalendar(Double jd) {
        return julianDateToCalendar(jd, Calendar.getInstance());
    }

    /**
     * Transforms a Julian Date to java.util.Calendar object. Based on Guine Christian's function
     * found here:
     * http://java.ittoolbox.com/groups/technical-functional/java-l/java-function-to-convert-julian-date-to-calendar-date-1947446
     */
    private Calendar julianDateToCalendar(Double jd, Calendar cal) {
        if (jd == null) {
            return null;
        }

        int yyyy, dd, mm, hh, mn, ss, ms, A;

        double w = jd + 0.5;
        int Z = (int) w;
        double F = w - Z;

        if (Z < 2299161) {
            A = Z;
        } else {
            int alpha = (int) ((Z - 1867216.25) / 36524.25);
            A = Z + 1 + alpha - (int) (alpha / 4.0);
        }

        int B = A + 1524;
        int C = (int) ((B - 122.1) / 365.25);
        int D = (int) (365.25 * C);
        int E = (int) ((B - D) / 30.6001);

        //  month
        mm = E - ((E < 13.5) ? 1 : 13);

        // year
        yyyy = C - ((mm > 2.5) ? 4716 : 4715);

        // Day
        double jjd = B - D - (int) (30.6001 * E) + F;
        dd = (int) jjd;

        // Hour
        double hhd = jjd - dd;
        hh = (int) (24 * hhd);

        // Minutes
        double mnd = (24 * hhd) - hh;
        mn = (int) (60 * mnd);

        // Seconds
        double ssd = (60 * mnd) - mn;
        ss = (int) (60 * ssd);

        // Milliseconds
        double msd = (60 * ssd) - ss;
        ms = (int) (1000 * msd);

        cal.set(yyyy, mm - 1, dd, hh, mn, ss);
        cal.set(Calendar.MILLISECOND, ms);

        if (yyyy < 1) {
            cal.set(Calendar.ERA, GregorianCalendar.BC);
            cal.set(Calendar.YEAR, -(yyyy - 1));
        }

        return cal;
    }

    public void checkCalendar(Calendar cal) throws SQLException {
        if (cal != null) return;

        SQLException e = new SQLException("Expected a calendar instance.");
        e.initCause(new NullPointerException());

        throw e;
    }
}