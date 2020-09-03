package byz.easy.database.oracle;

import java.sql.SQLException;
import java.util.Map.Entry;

import byz.easy.database.AbstractNumberColumn;

/**
 * @author
 * @since 2019年11月3日
 */
public class NumberColumn extends AbstractNumberColumn {

	public NumberColumn(String columnName, String comments, Number defaultValue) {
		super(columnName, comments, defaultValue);
		// TODO 待完成
	}

	@Override
	public String getUpdateCommentsSQL() {
		return null;
	}

	@Override
	public NumberColumn setLength(Integer length) throws SQLException {
		if (length != null && length > 0 && length < 39)
			this.length = length;
		else
			throw new SQLException("长度过长");
		return this;
	}

	@Override
	public NumberColumn setPrecision(Integer precision) throws SQLException {
		if (precision != null && precision > 0
				&& precision + Math.abs(scale == null ? 0 : scale) <= (length == null ? 38 : length))
			this.precision = precision;
		else
			throw new SQLException("位数 + 精度 > 长度");
		return this;
	}

	@Override
	public NumberColumn setScale(Integer scale) throws SQLException {
		if (scale != null && precision + Math.abs(scale) <= (length == null ? 38 : length))
			this.scale = scale;
		else
			throw new SQLException("位数 + 精度 > 长度");
		return this;
	}

	@Override
	public String getCreateColumnSQL() {
		StringBuilder builder = new StringBuilder();
		builder.append(columnName).append(' ').append("NUMBER");
		if (precision != null) {
			builder.append('(').append(getPrecision());
			if (scale != null)
				builder.append(',').append(scale);
			builder.append(')');
		}
		if (unique)
			builder.append(" UNIQUE");
		if (notNull)
			builder.append(" NOT NULL");
		if (defalultValue != null)
			builder.append(" DEFAULT ").append(defalultValue);
		return builder.toString();
	}

}
