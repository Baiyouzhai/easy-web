package byz.easy.database;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author
 * @since 2019年11月3日
 */
public abstract class AbstractTable {

	protected String tableName;
	protected String comments;
	protected List<AbstractColumn<?>> columns;
	protected List<AbstractPrimaryKey<?>> primaryKeys;
	protected List<AbstractForeignKey<?>> foreignKeys;

	public AbstractTable(String tableName) {
		this.tableName = tableName;
		this.columns = new LinkedList<AbstractColumn<?>>();
		this.primaryKeys = new LinkedList<AbstractPrimaryKey<?>>();
		this.foreignKeys = new LinkedList<AbstractForeignKey<?>>();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<AbstractColumn<?>> getColumns() {
		return columns;
	}

	public void setColumns(List<AbstractColumn<?>> columns) {
		this.columns = columns;
	}

	public List<AbstractPrimaryKey<?>> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(List<AbstractPrimaryKey<?>> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public List<AbstractForeignKey<?>> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(List<AbstractForeignKey<?>> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	/**
	 * 获取建表SQL
	 * @return
	 * @throws SQLException
	 */
	public abstract String getCreateTableSQL() throws SQLException;

	/**
	 * 获取创建主键SQL
	 * @return
	 */
	public abstract String getCreatePrimaryKeySQL();

	/**
	 * 获取创建外键SQL
	 * @return
	 */
	public abstract String getCreateForeignKeySQL();

}