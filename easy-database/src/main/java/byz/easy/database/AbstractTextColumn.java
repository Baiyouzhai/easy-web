package byz.easy.database;

/**
 * @author 
 * @since 2019年11月4日
 */
public abstract class AbstractTextColumn extends AbstractColumn<CharSequence> {

	protected Long length;

	public AbstractTextColumn(String columnName, String comments, CharSequence defaultValue) {
		super(columnName, comments, defaultValue);
		//TODO 待完成
	}

	public Long getLength() {
		return length;
	}

	public abstract void setLength(Long length);

}
