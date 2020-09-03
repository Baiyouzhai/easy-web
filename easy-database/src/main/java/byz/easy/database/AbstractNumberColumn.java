package byz.easy.database;

import java.sql.SQLException;

/**
 * @author 
 * @since 2019年11月4日
 */
public abstract class AbstractNumberColumn extends AbstractColumn<Number> {

	protected Integer length;
	protected Integer precision;
	protected Integer scale;

	public AbstractNumberColumn(String columnName, String comments, Number defaultValue) {
		super(columnName, comments, defaultValue);
		//TODO 待完成
	}

	public Integer getLength() {
		return length;
	}

	public abstract AbstractNumberColumn setLength(Integer length) throws SQLException;

	public Integer getPrecision() {
		return precision;
	}

	public abstract AbstractNumberColumn setPrecision(Integer precision) throws SQLException;

	public Integer getScale() {
		return scale;
	}

	public abstract AbstractNumberColumn setScale(Integer scale) throws SQLException;

}
