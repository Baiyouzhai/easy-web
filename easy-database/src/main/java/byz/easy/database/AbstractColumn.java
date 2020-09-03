package byz.easy.database;

import java.sql.SQLException;

/**
 * @author
 * @since 2019年11月3日
 */
public abstract class AbstractColumn<T> {

	protected String columnName;
	protected String comments;
	protected T defalultValue;
	protected boolean unique = false;
	protected boolean notNull = false;
	protected boolean isPrimaryKey = false;
	protected boolean isForeignKey = false;
	
	public AbstractColumn(String columnName, String comments, T defaultValue) {
		this.columnName = columnName;
		this.comments = comments;
		this.defalultValue = defaultValue;
	}

	public String getColumnName() {
		return columnName;
	}

	public AbstractColumn<T> setColumnName(String columnName) {
		this.columnName = columnName;
		return this;
	}

	public String getComments() {
		return comments;
	}

	public AbstractColumn<T> setComments(String comments) {
		this.comments = comments;
		return this;
	}

	public boolean isUnique() {
		return unique;
	}

	public AbstractColumn<T> setUnique(boolean unique) {
		this.unique = unique;
		return this;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public AbstractColumn<T> setNotNull(boolean notNull) {
		this.notNull = notNull;
		return this;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public AbstractColumn<T> setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
		return this;
	}

	public boolean isForeignKey() {
		return isForeignKey;
	}

	public AbstractColumn<T> setForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
		return this;
	}

	public abstract String getCreateColumnSQL() throws SQLException;

	public abstract String getUpdateCommentsSQL();

}