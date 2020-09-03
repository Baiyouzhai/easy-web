package byz.easy.database.oracle;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map.Entry;

import byz.easy.database.AbstractDateColumn;

/**
 * @author 
 * @since 2019年11月3日
 */
public class DateColumn extends AbstractDateColumn {

	public DateColumn(String columnName, String comments, Date defaultValue) {
		super(columnName, comments, defaultValue);
	}

	@Override
	public DateColumn setLength(Integer length) throws SQLException {
		if (length != null && length > 0 && length < 9)
			this.length = length;
		else
			throw new SQLException("长度过长");
		return this;
	}

	@Override
	public DateColumn setScale(Integer scale) {
		return this;
	}

	@Override
	public String getCreateColumnSQL() {
		return null;
	}

	@Override
	public String getUpdateCommentsSQL() {
		return null;
	}

}
