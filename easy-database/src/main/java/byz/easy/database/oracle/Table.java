package byz.easy.database.oracle;

import java.sql.SQLException;

import byz.easy.database.AbstractTable;

/**
 * @author
 * @since 2019年11月3日
 */
public class Table extends AbstractTable {

	public Table(String tableName) {
		super(tableName);
	}

	@Override
	public String getCreateTableSQL() throws SQLException {
		StringBuilder builder = new StringBuilder("CREATE TABLE ");
		builder.append(tableName).append(" (\n");
		for (int i = 0; i < columns.size(); i++)
			builder.append('\t').append(columns.get(i).getCreateColumnSQL()).append(',');
		if (builder.charAt(builder.length() - 1) == ',')
			builder.deleteCharAt(builder.length() - 1);
		builder.append("\n)");
		return builder.toString();
	}

	@Override
	public String getCreatePrimaryKeySQL() {
		return null;
	}

	@Override
	public String getCreateForeignKeySQL() {
		return null;
	}

}