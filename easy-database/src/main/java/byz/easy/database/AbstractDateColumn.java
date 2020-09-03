package byz.easy.database;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author 
 * @since 2019年11月4日
 */
public abstract class AbstractDateColumn extends AbstractColumn<Date> {

	protected Integer length;
	protected Integer scale;

	public AbstractDateColumn(String columnName, String comments, Date defaultValue) {
		super(columnName, comments, defaultValue);
		//TODO 待完成
	}

	public Integer getLength() {
		return length;
	}

	public abstract AbstractDateColumn setLength(Integer length) throws SQLException;

	public Integer getScale() {
		return scale;
	}

	public abstract AbstractDateColumn setScale(Integer scale);

}
