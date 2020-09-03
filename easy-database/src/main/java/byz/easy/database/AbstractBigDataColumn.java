package byz.easy.database;

/**
 * @author 
 * @since 2019年11月4日
 */
public abstract class AbstractBigDataColumn<T> extends AbstractColumn<T> {

	protected Long length;

	public AbstractBigDataColumn(String columnName, String comments, T defaultValue) {
		super(columnName, comments, defaultValue);
		//TODO 待完成
	}

	public Long getLength() {
		return length;
	}

	public abstract void setLength(Long length);

}
